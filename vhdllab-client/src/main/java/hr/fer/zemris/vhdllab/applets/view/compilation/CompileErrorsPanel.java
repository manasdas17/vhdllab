package hr.fer.zemris.vhdllab.applets.view.compilation;

import hr.fer.zemris.vhdllab.api.results.CompilationMessage;
import hr.fer.zemris.vhdllab.api.results.CompilationResult;
import hr.fer.zemris.vhdllab.applets.main.componentIdentifier.ComponentIdentifierFactory;
import hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IView;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.client.core.log.ResultTarget;
import hr.fer.zemris.vhdllab.client.core.log.SystemLog;
import hr.fer.zemris.vhdllab.client.core.log.SystemLogAdapter;
import hr.fer.zemris.vhdllab.client.core.log.SystemLogListener;
import hr.fer.zemris.vhdllab.entities.FileType;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Panel koji sadrzi mozebitne greske prilikom kompajliranja VHDL koda.
 * 
 * @author Boris Ozegovic
 * @version 1.0
 * @since 22.12.2006.
 */
public class CompileErrorsPanel extends JPanel implements IView {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -7361269803493786758L;

	/** DefaultListModel */
	private DefaultListModel model;

	/** JList komponenta u koju ce se potrpati sve greske */
	private JList listContent;

	/** Panel sadrzi JScrollPane komponentu cime je omoguceno scrollanje */
	private JScrollPane scrollPane;

	/** SystemContainer */
	private ISystemContainer container;

	private ResultTarget<CompilationResult> resultTarget;

	private SystemLogListener systemLogListener;

	/**
	 * Constructor
	 * 
	 * Kreira objekt i dovodi ga u pocetno stanje ciji kontekst sadrzi prazan
	 * string
	 */
	public CompileErrorsPanel() {
	}

	/**
	 * Glavna metoda koja uzima neki rezultat dobiven od strane servera.
	 * 
	 * @param resultTarget
	 *            rezultat koji ce ciniti kontekst panela s greskama
	 */
	public void setContent(ResultTarget<CompilationResult> resultTarget) {
		this.resultTarget = resultTarget;
		CompilationResult result = resultTarget.getResult();
		if (result.isSuccessful()) {
			Format formatter = new SimpleDateFormat("HH:mm:ss");
			String time = formatter.format(resultTarget.getDate());
			model.addElement(time + "  Compilation finished successfully.");
			return;
		}

		model.clear();
		for (CompilationMessage msg : result.getMessages()) {
			StringBuilder sb = new StringBuilder(
					msg.getMessageText().length() + 20);
			sb.append(msg.getEntityName()).append(":").append(msg.getRow())
					.append(":").append(msg.getColumn()).append(":").append(
							msg.getMessageText());
			model.addElement(sb.toString());
		}
	}

	/**
	 * Uzima string te se taj string usporeduje s uzorkom. Ako uzorak postoji
	 * unutar tog stringa, iz njega se vade ime datoteke i linija u kojoj se
	 * dogodila greska i pozivaju se odgovarajuce metode za pozicioniranje na
	 * konkretnu datoteku i liniju u kojoj se dogodila greska. Ako uzorak ne
	 * postoji ne dogada se nista.
	 * 
	 * @param error
	 *            Linija koju je generira VHDL simulator
	 */
	public void highlightError(String error) {
		Pattern pattern = Pattern.compile("([^:]+):([^:]+):([^:]+):(.+)");
		Matcher matcher = pattern.matcher(error);
		if (matcher.matches()) {
			FileIdentifier resource = resultTarget.getResource();
			IEditor editor;
			if (container.getResourceManager().getFileType(
					resource.getProjectName(), resource.getFileName()).equals(
					FileType.SOURCE)) {
				IComponentIdentifier<FileIdentifier> identifier = ComponentIdentifierFactory
						.createFileEditorIdentifier(resource);
				editor = container.getEditorManager().getOpenedEditor(
						identifier);
				if(editor == null) {
					editor = container.getEditorManager().openEditorByResource(identifier);
				}
			} else {
				IComponentIdentifier<FileIdentifier> identifier = ComponentIdentifierFactory
						.createViewVHDLIdentifier(resource);
				editor = container.getEditorManager().viewVHDLCode(identifier);
			}
			int temp = Integer.valueOf(matcher.group(2));
			editor.highlightLine(temp);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IView#init()
	 */
	@Override
	public void init() {
		model = new DefaultListModel();
		listContent = new JList(model);
		listContent.setFixedCellHeight(15);
		listContent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if (event.getClickCount() == 2) {
					String selectedValue = (String) listContent
							.getSelectedValue();
					highlightError(selectedValue);
				}
			}
		});
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				scrollPane.setPreferredSize(CompileErrorsPanel.this.getSize());
			}
		});

		scrollPane = new JScrollPane(listContent);

		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);

		systemLogListener = new SystemLogAdapter() {
			@Override
			public void compilationTargetAdded(
					ResultTarget<CompilationResult> result) {
				setContent(result);
			}
		};
		SystemLog.instance().addSystemLogListener(systemLogListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IView#dispose()
	 */
	@Override
	public void dispose() {
		SystemLog.instance().removeSystemLogListener(systemLogListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.view.IView#setProjectContainer(hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer)
	 */
	public void setSystemContainer(ISystemContainer container) {
		this.container = container;
	}

	@Override
	public <T> T asInterface(Class<T> clazz) {
		return null;
	}

}