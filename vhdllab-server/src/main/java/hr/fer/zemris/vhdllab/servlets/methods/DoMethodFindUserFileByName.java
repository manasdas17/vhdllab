package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.UserFile;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "find user files by name"
 * request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodFindUserFileByName extends AbstractRegisteredMethod {

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab
     * .api.comm.Method, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void run(Method<Serializable> method, HttpServletRequest request) {
        Caseless fileName = method.getParameter(Caseless.class, PROP_FILE_NAME);
		if (fileName == null) {
			return;
		}
		Caseless userId = method.getUserId();
		UserFile file;
		try {
			file = container.getUserFileManager().findByName(userId, fileName);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_FIND_FILE, "userId=" + userId + ", name="
					+ fileName);
			return;
		}
		method.setResult(file.getId());
	}

}