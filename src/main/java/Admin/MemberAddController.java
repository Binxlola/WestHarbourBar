package main.java.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.java.HibernateUtil;
import main.java.Member;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MemberAddController extends AnchorPane implements Initializable {

    private final Stage parentStage;
    private final MemberManagementController parentController;
    @FXML private TextField id, email, firstName, lastName, phone;
    @FXML private Button apply, cancel;

    public MemberAddController(Stage parentStage, MemberManagementController parentController) {
        this.parentStage = parentStage;
        this.parentController = parentController;

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
            parentStage.close();
        } else if(source.equals(apply)) {

            Member newMember = new Member(Long.parseLong(id.getText()),
                    email.getText(),
                    firstName.getText(),
                    lastName.getText(),
                    phone.getText());

            HibernateUtil.saveOrRemove(newMember, true);

            parentController.update();
            parentStage.close();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        apply.setOnAction(this::handler);
        cancel.setOnAction(this::handler);
    }
}
