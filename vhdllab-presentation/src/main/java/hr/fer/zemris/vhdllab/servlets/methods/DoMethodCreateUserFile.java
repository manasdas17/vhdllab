package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.entities.UserFile;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "create user file" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodCreateUserFile extends AbstractRegisteredMethod {

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab
     * .api.comm.Method, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void run(Method<Serializable> method, HttpServletRequest request) {
		String fileName = method.getParameter(String.class, PROP_FILE_NAME);
		String fileType = method.getParameter(String.class, PROP_FILE_TYPE);
		if (fileName == null || fileType == null) {
			return;
		}
		String userId = method.getUserId();
		UserFile file;
		try {
		    file = new UserFile(userId, fileName, fileType);
			container.getUserFileManager().save(file);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_CREATE_FILE, "userId=" + userId
					+ ", name=" + fileName + ", type=" + fileType);
			return;
		}
		method.setResult(file.getId());
	}

}