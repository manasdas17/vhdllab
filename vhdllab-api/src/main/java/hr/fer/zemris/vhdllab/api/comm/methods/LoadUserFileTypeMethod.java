package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class LoadUserFileTypeMethod extends AbstractIdParameterMethod<String> {

	private static final long serialVersionUID = 1L;

	public LoadUserFileTypeMethod(Long id, String userId) {
		super("load.user.file.type", userId, id);
	}

}