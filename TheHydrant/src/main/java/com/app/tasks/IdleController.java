package main.java.com.app.tasks;

import javafx.application.Platform;
import main.java.com.app.App;

import java.util.TimerTask;

public class IdleController extends TimerTask implements TimedTask {

    private App APP;

    private TaskTimer parentTimer;

    public IdleController() {
        this.APP = App.getInstance();
    }

    @Override
    public void run() {
        parentTimer.setTask(new IdleController());

        // Terminate the timer thread
        Platform.runLater(() -> {
            APP.logout();
        });
    }

    @Override
    public void setParentTimer(TaskTimer parentTimer) {
        this.parentTimer = parentTimer;
    }
}
