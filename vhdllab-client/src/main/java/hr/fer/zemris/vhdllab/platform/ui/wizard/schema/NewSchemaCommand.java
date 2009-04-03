package hr.fer.zemris.vhdllab.platform.ui.wizard.schema;

import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.support.WorkspaceInitializer;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractShowWizardDialogCommand;
import hr.fer.zemris.vhdllab.platform.ui.wizard.support.WorkspaceNotEmptyCommandGuard;

import org.springframework.richclient.wizard.Wizard;

public class NewSchemaCommand extends AbstractShowWizardDialogCommand {

    @Override
    protected Class<? extends Wizard> getWizardClass() {
        return NewSchemaWizard.class;
    }

    @Override
    protected void installCommandGuard(WorkspaceManager wm,
            WorkspaceInitializer wi, SimulationManager sm) {
        new WorkspaceNotEmptyCommandGuard(wm, wi, this);
    }

}
