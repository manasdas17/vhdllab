package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;

import java.util.Properties;

/**
 * This class represents a registered method for "load project name" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodLoadProjectName implements RegisteredMethod {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.ajax.shared.RegisteredMethod#run(java.util.Properties, hr.fer.zemris.vhdllab.service.VHDLLabManager)
	 */
	public Properties run(Properties p, VHDLLabManager labman) {
		String projectID = p.getProperty(MethodConstants.PROP_PROJECT_ID,null);
		if(projectID == null) return errorProperties(MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No project ID specified!");

		Long id = null;
		try {
			id = Long.parseLong(projectID);
		} catch (NumberFormatException e) {
			return errorProperties(MethodConstants.SE_PARSE_ERROR,"Unable to parse project ID!");
		}
		
		// Load project
		Project project = null;
		try {
			project = labman.loadProject(id);
		} catch (ServiceException e) {
			project = null;
		}
		if(project==null) return errorProperties(MethodConstants.SE_NO_SUCH_PROJECT,"Project not found!");

		// Prepare response
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,MethodConstants.MTD_LOAD_PROJECT_NAME);
		resProp.setProperty(MethodConstants.PROP_STATUS,MethodConstants.STATUS_OK);
		resProp.setProperty(MethodConstants.PROP_PROJECT_NAME,project.getProjectName());
		return resProp;
	}
	
	/**
	 * This method is called if errors occur.
	 * @param errNo error message number
	 * @param errorMessage error message to pass back to caller
	 * @return a response Properties
	 */
	private Properties errorProperties(String errNo, String errorMessage) {
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_STATUS,errNo);
		resProp.setProperty(MethodConstants.PROP_STATUS_CONTENT,errorMessage);
		return resProp;
	}
}