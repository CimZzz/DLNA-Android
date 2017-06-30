package com.virtualightning.dlna;

import java.io.IOException;

import com.virtualightning.dlna.constant.ErrorCode;

public abstract class BaseServer {
    private boolean isRun;
    DLNAContext dlnaContext;

    BaseServer(DLNAContext dlnaContext) {
        isRun = false;
        this.dlnaContext = dlnaContext;
    }

    final void startServer(final TaskStatistic statist){
        isRun = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    onStart();
                    statist.complete();
                } catch (IOException e) {
                    if(isRun)
                        statist.failed();
                    if(isRun && dlnaContext != null)
                        dlnaContext.error(ErrorCode.SERVER_BOOTSTRAP_ERROR,e);
                    return;
                }
                while (isRun) {
                    onLoop();
                }
            }
        }).start();
    }

    boolean isRun() {
        return isRun;
    }

    final void closeServer() {
        isRun = false;
        onClose();
    }

    protected abstract void onStart() throws IOException;
    protected abstract void onLoop();
    protected abstract void onClose();

}
