package BarApp.Admin;

import BarApp.DatabaseConnector;
import BarApp.Member;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MemberAddController extends AnchorPane implements Initializable {

    private Stage parent;
    @FXML private TextField id, email, firstName, lastName, phone;
    @FXML private Button apply, cancel;

    public MemberAddController(Stage parent) {
        this.parent = parent;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MemberAdd.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handler(ActionEvent e) {
        Object source = e.getSource();
        if(source.equals(cancel)) {
            parent.close();
        } else if(source.equals(apply)) {
            Member newMember = new Member();
            newMember.setId(Long.parseLong(id.getText()));
            newMember.setEmail(email.getText());
            newMember.setFirstName(firstName.getText());
            newMember.setLastName(lastName.getText());
            newMember.setPhone(phone.getText());

            DatabaseConnector.storeData(newMember);

            parent.close();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        apply.setOnAction(this::handler);
        cancel.setOnAction(this::handler);
    }
}
