package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractMethod;
import hr.fer.zemris.vhdllab.entities.Caseless;

/**
 * @author Miro Bezjak
 * 
 */
public final class IsSuperuserMethod extends AbstractMethod<Boolean> {

    private static final long serialVersionUID = 1L;

    public IsSuperuserMethod(Caseless userId) {
        super("is.superuser", userId);
    }

}
