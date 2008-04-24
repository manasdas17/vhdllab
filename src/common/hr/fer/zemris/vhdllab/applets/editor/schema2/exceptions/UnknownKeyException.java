package hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions;



/**
 * Baca se kad ne postoji takav
 * kljuc u kolekciji.
 * 
 * @author brijest
 *
 */
public class UnknownKeyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnknownKeyException() {
		super();
	}

	public UnknownKeyException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UnknownKeyException(String arg0) {
		super(arg0);
	}

	public UnknownKeyException(Throwable arg0) {
		super(arg0);
	}

}