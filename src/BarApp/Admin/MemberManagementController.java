package BarApp.Admin;

import BarApp.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MemberManagementController extends AnchorPane implements Initializable {

    @FXML ScrollPane scrollContainer;
    @FXML Button memberAdd;
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

    private void handleMembers(ActionEvent actionEvent) {

        Object source = actionEvent.getSource();
        if(source.equals(memberAdd)) {
            Dialog<ButtonType> root = new Dialog<>();
            root.setHeaderText("hello this is to add a mamber");
            root.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        memberAdd.setOnAction(this::handleMembers);
//        ResultSet members = DatabaseConnector.getAllMembers(this._Main.getConnection());
    }
}
