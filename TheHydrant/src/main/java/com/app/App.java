package com.app;

import com.app.login.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.app.entities.Member;
import com.app.tasks.IdleController;
import com.app.tasks.TaskTimer;
import com.app.util.HibernateUtil;

public class App extends Application {

    private Stage mainStage;
    public static App APP;
    private Scene login;
    private Scene currentScene;
    private Member user = null;
    private TaskTimer idleMonitor;

    @Override
    public void start(Stage stage) {
        stage.setFullScreen(true);

        // Set "GLOBAL" variables
        APP = this;
        mainStage = stage;
        login = new Scene(new LoginController());

        mainStage.setTitle("The Hydrant");
        mainStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        mainStage.setFullScreen(true);
        mainStage.setScene(login);
        mainStage.initStyle(StageStyle.TRANSPARENT);
        mainStage.initStyle(StageStyle.UNDECORATED);

        setupTimedTasks();

        mainStage.show();
    }

    /**
     * Called when moving to a new screen/scene. The idle monitor is cancelled and has a new task set for the new screen
     * The application current scene is updated to be that of the passed in scene. Then there is an event filter added to the
     * scene which will be used by the idle monitor, anytime there is user interaction the idle monitor will be reset.
     * Finally the new scene in shown and the idle monitor is started
     * @param newScene New scene to display
     */
    public void setScene(Scene newScene, long monitorDelay) {
        // Stop the idle monitor task that was being used on the previous scene
        idleMonitor.cancelTimer();
        idleMonitor.setDelay(monitorDelay);
        idleMonitor.setTask(new IdleController());

        currentScene = newScene;
        mainStage.setScene(newScene);
        mainStage.setFullScreen(true);
        currentScene.addEventFilter(Event.ANY, e -> idleMonitor.resetTimer(new IdleController()));
        mainStage.show();

        idleMonitor.startTimer();
    }

    private void setupTimedTasks() {
        IdleController idleController = new IdleController();
        idleMonitor = new TaskTimer(idleController);

    }

    public Scene getCurrentScene() {
        return this.currentScene;
    }

    public void setUser(Member user) {this.user = user;}
    public Member getUser() {return this.user;}
    public TaskTimer getIdleMonitor() {return idleMonitor;}

    public static App getInstance() {
        return APP;
    }

    public void logout() {
        mainStage.setScene(login);
        mainStage.setFullScreen(true);
        mainStage.show();
        user = null;
        idleMonitor.cancelTimer();
    }

    @Override
    public void stop() {
        HibernateUtil.shutdown();
        Platform.exit();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
