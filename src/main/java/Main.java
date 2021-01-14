package main.java;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import main.java.Login.LoginController;

public class Main extends Application {

    private Stage mainStage;
    public static Main _Main;
    private Scene login;
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
        mainStage.show();
    }

    public void setScene(Scene newScene) {
        mainStage.setScene(newScene);
        mainStage.setFullScreen(true);
        mainStage.show();
    }

    public void setUser(Member user) {this.user = user;}
    public Member getUser() {return this.user;}

    public static Main getInstance() {
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
