package main.java.com.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import main.java.com.app.Login.LoginController;
import main.java.com.app.entities.Member;
import main.java.com.app.util.HibernateUtil;

public class App extends Application {

    private Stage mainStage;
    public static App _Main;
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
        mainStage.initStyle(StageStyle.TRANSPARENT);
        mainStage.initStyle(StageStyle.UNDECORATED);

        // USED TO MAKE SMOOTH FULLSCREEN TRANSITIONS
//        int width = (int) Screen.getPrimary().getBounds().getWidth();
//        int height = (int) Screen.getPrimary().getBounds().getHeight();
//        mainLayout = new HBox();
//        mainLayout.getChildren().add(new Text("hello!"));
//        myLayout = new MyLayout(this);
//        scene = new Scene(myLayout,width,height);


        mainStage.show();
        ((LoginController) login.getRoot()).initialSetup(login);
    }

    public void setScene(Scene newScene) {
        mainStage.setScene(newScene);
        mainStage.setFullScreen(true);
        mainStage.show();
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
