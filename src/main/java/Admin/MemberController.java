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

public class MemberController extends AnchorPane implements Initializable {

    private final Stage parentStage;
    private final AdminController parentController;
    private boolean isEdit = false;
    private Member member = null;
    @FXML private TextField id, email, firstName, lastName, phone, balance;
    @FXML private Button apply, cancel;

    public MemberController(Stage parentStage, AdminController parentController) {
        this.parentStage = parentStage;
        this.parentController = parentController;
        this.buildView();
    }

    public MemberController(Stage parentStage, AdminController parentController, Member member) {
        this.isEdit = true;
        this.member = member;

        this.parentStage = parentStage;
        this.parentController = parentController;
        this.buildView();


    }

    /**
     * Because there are two ways to construct this controller the FXML build has been pulled out to avoid large duplicated code.
     * This method Builds the FXML from the view part of the controller
     */
    private void buildView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Member.fxml"));
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

            if(!isEdit) {
                member = new Member(Long.parseLong(id.getText()),
                        email.getText(),
                        firstName.getText(),
                        lastName.getText(),
                        phone.getText());

                HibernateUtil.saveOrRemove(member, true);
            } else {
                member.setEmail(email.getText());
                member.setBalance(Float.parseFloat(balance.getText()));
                member.setPhone(phone.getText());

                HibernateUtil.saveOrRemove(member, true);
            }


            parentController.update();
            parentStage.close();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (isEdit) {
            id.setText(String.valueOf(member.getId()));
            id.setDisable(true);

            firstName.setText(member.getFirstName());
            firstName.setDisable(true);

            lastName.setText(member.getLastName());
            lastName.setDisable(true);

            phone.setText(member.getPhone());
            email.setText(member.getEmail());
            balance.setText(String.valueOf(member.getBalance()));
        } else {
            balance.setText("0.0");
            balance.setDisable(true);
        }

        apply.setOnAction(this::handler);
        cancel.setOnAction(this::handler);
    }
}
