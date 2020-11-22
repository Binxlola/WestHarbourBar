package main.java;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.Login.LoginController;

public class Main extends Application {

    private Stage mainStage;
    public static Main _Main;
    private Scene login;

    @Override
    public void start(Stage stage) throws Exception {
        // Set "GLOBAL" variables
        _Main = this;
        this.mainStage = stage;
        this.login = new Scene(new LoginController());

        mainStage.setTitle("The Hydrant");
        mainStage.setScene(login);
        mainStage.show();
    }

    public void setScene(Scene newScene) {
        mainStage.setScene(newScene);
        mainStage.show();
    }

    public static Main getMain() {
        return _Main;
    }

    public void logout() {
        mainStage.setScene(login);
        mainStage.show();
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