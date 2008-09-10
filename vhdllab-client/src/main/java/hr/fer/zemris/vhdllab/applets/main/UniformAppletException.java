package hr.fer.zemris.vhdllab.applets.main;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Thrown to indicate any kind of error on client application.
 * 
 * @author Miro Bezjak
 */
public class UniformAppletException extends Exception {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 2592236289892870034L;

	public UniformAppletException() {
		super();
	}
	
	public UniformAppletException(String message) {
		super(message);
	}
	
	public UniformAppletException(Throwable cause) {
		super(cause);
	}

	public UniformAppletException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Returns a string representation of complete stack trace.
	 * @return a string representation of complete stack trace
	 */
	public String getStackTraceAsString() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		printStackTrace(pw);
		return sw.toString(); 
	}

}