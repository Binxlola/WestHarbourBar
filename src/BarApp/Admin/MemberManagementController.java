package BarApp.Admin;

import BarApp.DatabaseConnector;
import BarApp.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class MemberManagementController extends AnchorPane implements Initializable {

    @FXML ScrollPane scrollContainer;
    private final Main _Main = Main.getMain();

    public MemberManagementController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MemberManagement.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        ResultSet members = DatabaseConnector.getAllMembers(this._Main.getConnection());
    }
}
