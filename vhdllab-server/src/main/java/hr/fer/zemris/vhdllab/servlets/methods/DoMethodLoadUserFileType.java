package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.entities.UserFile;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "load user file type" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodLoadUserFileType extends AbstractRegisteredMethod {

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab
     * .api.comm.Method, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void run(Method<Serializable> method, HttpServletRequest request) {
		Long fileId = method.getParameter(Long.class, PROP_ID);
		if (fileId == null) {
			return;
		}
		UserFile file;
		try {
			file = container.getUserFileManager().load(fileId);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_FIND_FILE, "fileId=" + fileId);
			return;
		}
		checkUserFileSecurity(request, method, file);
		method.setResult(file.getType());
	}
	
}