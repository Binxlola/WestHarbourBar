package com.app.tasks;

import javafx.application.Platform;
import com.app.App;

import java.util.TimerTask;

public class IdleController extends TimerTask {

    private App APP;

    private TaskTimer parentTimer;

    public IdleController() {
        this.APP = App.getInstance();
        this.parentTimer = APP.getIdleMonitor();
    }

    @Override
    public void run() {
        // Terminate the timer thread
        parentTimer.cancelTimer();

        Platform.runLater(() -> {
            APP.logout();
        });
    }
}
