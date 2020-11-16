package BarApp;

import BarApp.Login.LoginController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;

public class Main extends Application {

    private Stage mainStage;
    public static Main _Main;
    private Scene login;
    private Connection conn;

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

    public void setConnection(Connection connection) {this.conn = connection;}
    public Connection getConnection() {return this.conn;}

    public static Main getMain() {return _Main;}

    public void logout() {
        mainStage.setScene(login);
        mainStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
