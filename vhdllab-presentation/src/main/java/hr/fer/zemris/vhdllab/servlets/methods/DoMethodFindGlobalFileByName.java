package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.entities.Library;
import hr.fer.zemris.vhdllab.entities.LibraryFile;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "find global files by name" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodFindGlobalFileByName extends AbstractRegisteredMethod {

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
		if (fileName == null) {
			return;
		}
		LibraryFile file;
		try {
		    Library lib = container.getLibraryManager().findByName("preferences");
		    file = container.getLibraryFileManager().findByName(lib.getId(), fileName);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_FIND_FILE, "type=" + fileName);
			return;
		}
		method.setResult(file.getId());
	}
	
}