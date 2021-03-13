package main.java.com.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.com.app.Login.LoginController;
import main.java.com.app.entities.Member;
import main.java.com.app.util.HibernateUtil;

public class App extends Application {

    private Stage mainStage;
    public static App _Main;
    private Scene login;
    private Scene currentScene;
    private Member user = null;

    @Override
    public void start(Stage stage) {
        stage.setFullScreen(true);
        // Set "GLOBAL" variables
        _Main = this;
        mainStage = stage;
        login = new Scene(new LoginController());

        mainStage.setTitle("The Hydrant");
        mainStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        mainStage.setFullScreen(true);
        mainStage.setScene(login);
        mainStage.initStyle(StageStyle.TRANSPARENT);
        mainStage.initStyle(StageStyle.UNDECORATED);

        mainStage.show();
    }

    public void setScene(Scene newScene) {
        currentScene = newScene;
        mainStage.setScene(newScene);
        mainStage.setFullScreen(true);
        mainStage.show();
    }

    public Scene getCurrentScene() {
        return  this.currentScene;
    }

    public void setUser(Member user) {this.user = user;}
    public Member getUser() {return this.user;}

    public static App getInstance() {
        return _Main;
    }

    public void logout() {
        mainStage.setScene(login);
        mainStage.setFullScreen(true);
        mainStage.show();
        user = null;
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
