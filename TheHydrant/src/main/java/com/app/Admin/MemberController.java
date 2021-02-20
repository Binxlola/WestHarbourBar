package main.java.com.app.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.java.com.app.entities.Admin;
import main.java.com.app.util.HibernateUtil;
import main.java.com.app.entities.Member;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MemberController extends AnchorPane implements Initializable {

    private final Stage parentStage;
    private final Pane parentController;
    private boolean isEdit = false;
    private Member member = null;
    @FXML private TextField id, email, firstName, lastName, phone, balance;
    @FXML private Button apply, cancel;
    @FXML private CheckBox isAdmin;
    @FXML private PasswordField password;

    public MemberController(Stage parentStage, Pane parentController) {
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
                member = isAdmin.isSelected() ? new Admin() : new Member();
                member.setId(Long.parseLong(id.getText()));
                member.setFirstName(firstName.getText());
                member.setLastName(lastName.getText());

                if(isAdmin.isSelected()) {
                    ((Admin) member).setPassword(password.getText());
                    ((Admin) member).setAdmin(true);
                }

            }
            member.setEmail(email.getText());
            member.setBalance(Float.parseFloat(balance.getText()));
            member.setPhone(phone.getText());

            HibernateUtil.saveOrRemove(member, true);

            if(parentController instanceof AdminController) {
                ((AdminController) parentController).update();
            }
            parentStage.close();
        }
    }

    public void lockIsAdmin(boolean lock, boolean check) {
        isAdmin.setSelected(check);
        isAdmin.setDisable(lock);
        update();
    }

    public void update() {
        password.setDisable(!isAdmin.isSelected());
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
