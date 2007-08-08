package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.interfaces.MethodInvoker;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.preferences.DefaultUserPreferences;
import hr.fer.zemris.vhdllab.preferences.IUserPreferences;
import hr.fer.zemris.vhdllab.preferences.PropertyAccessException;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Hierarchy;
import hr.fer.zemris.vhdllab.vhdl.model.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Communicator {

	private String ownerId;
	private MethodInvoker invoker;
	private Cache cache;

	public Communicator(MethodInvoker invoker, String ownerId) {
		if (invoker == null)
			throw new NullPointerException("Method invoker can not be null.");
		if (ownerId == null)
			throw new NullPointerException("Owner identifier can not be null.");
		cache = new Cache();
		this.invoker = invoker;
		this.ownerId = ownerId;
	}

	public void init() throws UniformAppletException {
		loadUserPreferences();
		// FIXME TMP SOLUTION
		loadFileIds();
	}

	private void loadFileIds() throws UniformAppletException {
		/*
		 * this is a fix for loadFileContent method. it breaks when system
		 * container tries to open editors (stored in a session). because
		 * critical methods: getAllProjects and findFilesByProject was still not
		 * called nothing is cached so loadFileContent method assumes that such
		 * file does not exist.
		 */
		for (String projectName : getAllProjects()) {
			for (String fileName : findFilesByProject(projectName)) {
				// this is just extra it has nothing to do with the actual bug
				loadFileType(projectName, fileName);
			}
		}
	}

	public void dispose() throws UniformAppletException {
		IUserPreferences preferences = cache.getUserPreferences();
		for (String key : preferences.propertyKeys()) {
			Long id = cache.getIdentifierForProperty(key);
			String property;
			try {
				property = preferences.getProperty(key);
			} catch (PropertyAccessException e) {
				throw new UniformAppletException(e);
			}
			invoker.saveUserFile(id, property);
		}
	}

	public List<String> getAllProjects() throws UniformAppletException {
		List<Long> projectIdentifiers = invoker.findProjectsByUser(ownerId);

		List<String> projectNames = new ArrayList<String>();
		for (Long id : projectIdentifiers) {
			String projectName = cache.getProjectForIdentifier(id);
			if (projectName == null) {
				String name = invoker.loadProjectName(id);
				cache.cacheItem(name, id);
				projectName = cache.getProjectForIdentifier(id);
			}
			projectNames.add(projectName);
		}
		return projectNames;
	}

	public List<String> findFilesByProject(String projectName)
			throws UniformAppletException {
		if (projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		Long projectIdentifier = cache.getIdentifierFor(projectName);
		if (projectIdentifier == null) {
			throw new UniformAppletException("Project does not exists!");
		}
		List<Long> fileIdentifiers = invoker
				.findFilesByProject(projectIdentifier);

		List<String> fileNames = new ArrayList<String>();
		for (Long id : fileIdentifiers) {
			FileIdentifier identifier = cache.getFileForIdentifier(id);
			if (identifier == null) {
				String name = invoker.loadFileName(id);
				cache.cacheItem(projectName, name, id);
				identifier = cache.getFileForIdentifier(id);
			}
			fileNames.add(identifier.getFileName());
		}
		return fileNames;
	}

	public boolean existsFile(String projectName, String fileName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Long projectIdentifier = cache.getIdentifierFor(projectName);
		if (projectIdentifier == null)
			throw new UniformAppletException("Project does not exists!");
		/* return invoker.existsFile(projectIdentifier, fileName); */
		return cache.containsIdentifierFor(projectName, fileName);
	}

	public boolean existsProject(String projectName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		/*
		 * Long projectIdentifier = cache.getIdentifierFor(projectName);
		 * if(projectIdentifier == null) return false; return
		 * invoker.existsProject(projectIdentifier);
		 */
		return cache.containsIdentifierFor(projectName);
	}

	public void deleteFile(String projectName, String fileName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if (fileIdentifier == null)
			throw new UniformAppletException("File does not exists!");
		cache.removeItem(projectName, fileName);
		invoker.deleteFile(fileIdentifier);
	}

	public void deleteProject(String projectName) throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName);
		if (fileIdentifier == null)
			throw new UniformAppletException("File does not exists!");
		cache.removeItem(projectName);
		invoker.deleteProject(fileIdentifier);
	}

	public void createProject(String projectName) throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		Long projectIdentifier = invoker.createProject(projectName, ownerId);
		cache.cacheItem(projectName, projectIdentifier);
	}

	public void createFile(String projectName, String fileName, String type)
			throws UniformAppletException {
		createFile(projectName, fileName, type, null);
	}

	public void createFile(String projectName, String fileName, String type, String data)
	throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		if (type == null)
			throw new NullPointerException("File type can not be null.");
		Long projectIdentifier = cache.getIdentifierFor(projectName);
		if (projectIdentifier == null)
			throw new UniformAppletException("Project does not exists!");
		Long fileIdentifier = invoker.createFile(projectIdentifier, fileName,
				type);
		cache.cacheItem(projectName, fileName, fileIdentifier);
		if(data != null) {
			saveFile(projectName, fileName, data);
		}
	}
	
	public void saveFile(String projectName, String fileName, String content)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		if (content == null)
			throw new NullPointerException("File content can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if (fileIdentifier == null)
			throw new UniformAppletException("File does not exists!");
		invoker.saveFile(fileIdentifier, content);
	}

	public String loadFileContent(String projectName, String fileName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if (fileIdentifier == null)
			throw new UniformAppletException("File does not exists!");
		return invoker.loadFileContent(fileIdentifier);
	}

	public String loadPredefinedFileContent(String fileName)
			throws UniformAppletException {
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		return invoker.loadPredefinedFileContent(fileName);
	}

	public String loadFileType(String projectName, String fileName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if (fileIdentifier == null) {
			return FileTypes.FT_PREDEFINED;
			// throw new UniformAppletException("File does not exists!");
		}
		String fileType = cache.getFileType(fileIdentifier);
		if(fileType == null) {
			fileType = invoker.loadFileType(fileIdentifier);
			cache.cacheFileType(fileIdentifier, fileType);
		}
		return fileType;
	}

	public Hierarchy extractHierarchy(String projectName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		Long projectIdentifier = cache.getIdentifierFor(projectName);
		if (projectIdentifier == null)
			throw new UniformAppletException("Project does not exists!");

		Hierarchy h = invoker.extractHierarchy(projectIdentifier);
		for (Pair p : h) {
			String fileName = p.getFileName();
			Long id = cache.getIdentifierFor(projectName, fileName);
			if (id == null) {
				if (invoker.existsFile(projectIdentifier, fileName)) {
					Long fileIdentifier = invoker.findFileByName(
							projectIdentifier, fileName);
					cache.cacheItem(projectName, fileName, fileIdentifier);
				} else {
					// invoker.
				}
			}
		}
		return h;
	}

	public String generateVHDL(String projectName, String fileName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if (fileIdentifier == null)
			throw new UniformAppletException("File does not exists!");
		return invoker.generateVHDL(fileIdentifier);
	}

	public CompilationResult compile(String projectName, String fileName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if (fileIdentifier == null)
			throw new UniformAppletException("File does not exists!");
		return invoker.compileFile(fileIdentifier);
	}

	public SimulationResult runSimulation(String projectName, String fileName)
			throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if (fileIdentifier == null)
			throw new UniformAppletException("File does not exists!");
		return invoker.runSimulation(fileIdentifier);
	}

	public CircuitInterface getCircuitInterfaceFor(String projectName,
			String fileName) throws UniformAppletException {
		if (projectName == null)
			throw new NullPointerException("Project name can not be null.");
		if (fileName == null)
			throw new NullPointerException("File name can not be null.");
		Long fileIdentifier = cache.getIdentifierFor(projectName, fileName);
		if (fileIdentifier == null)
			throw new UniformAppletException("File does not exists!");
		return invoker.extractCircuitInterface(fileIdentifier);
	}

	public IUserPreferences getPreferences() {
		return cache.getUserPreferences();
	}

	private void loadUserPreferences() throws UniformAppletException {
		Properties properties = new Properties();
		List<Long> userFileIds = invoker.findUserFilesByOwner(ownerId);
		for (Long id : userFileIds) {
			String name = invoker.loadUserFileName(id);
			cache.cacheUserFileItem(name, id);
			String data = invoker.loadUserFileContent(id);
			properties.setProperty(name, data);
		}
		IUserPreferences preferences = new DefaultUserPreferences(properties);
		cache.cacheUserPreferences(preferences);
	}

}