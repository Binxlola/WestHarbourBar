package BarApp.Admin;

import BarApp.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(new MemberAddController(stage)));
            stage.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        memberAdd.setOnAction(this::handleMembers);
//        ResultSet members = DatabaseConnector.getAllMembers(this._Main.getConnection());
    }
}
