package hr.fer.zemris.vhdllab.platform.manager.shutdown;

import hr.fer.zemris.vhdllab.platform.gui.dialog.DialogManager;
import hr.fer.zemris.vhdllab.platform.listener.AbstractEventPublisher;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component
public final class ShutdownManager extends
        AbstractEventPublisher<ShutdownListener> {

    /**
     * An exit status ok indicating normal application termination.
     */
    private static final int EXIT_STATUS_OK = 0;

    @Resource(name = "exitApplicationDialogManager")
    private DialogManager<Boolean> dialogManager;

    public ShutdownManager() {
        super(ShutdownListener.class);
    }

    public void shutdownWithConfirmation() {
        if (dialogManager.showDialog()) {
            fireShutdownInProgress();
            System.exit(EXIT_STATUS_OK);
        }
    }

    private void fireShutdownInProgress() {
        for (ShutdownListener l : getListeners()) {
            l.shutdownInProgress();
        }
    }

}