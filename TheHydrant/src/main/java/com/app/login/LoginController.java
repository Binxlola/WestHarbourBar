package com.app.login;

import com.app.App;
import com.app.admin.AdminController;
import com.app.entities.Member;
import com.app.user.UserController;
import com.app.util.CommonUtil;
import com.app.util.HibernateUtil;
import com.app.util.PasswordUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends AnchorPane implements Initializable {

    @FXML private GridPane userLoginForm, adminLoginForm, numPad;
    @FXML private TextField userID, adminID;
    @FXML private PasswordField adminPassword;
    @FXML private Label errorBox;
    @FXML private Button loginBtn, loginSwitch;
    private boolean isAdminLogin = false;
    private final App _Main = App.getInstance();


    public LoginController() {
        CommonUtil.buildView(this,"fxml/Login.fxml");

        // Catch first time app startup
        if (HibernateUtil.getSessionFactory() != null && HibernateUtil.isTableEmpty("Member", Member.class)) {
            Member defaultAdmin = new Member();
            defaultAdmin.setAdmin(true);
            defaultAdmin.setPassword("admin");
            defaultAdmin.setId(0000);
            defaultAdmin.setFirstName("Admin");
            defaultAdmin.setLastName("Admin");

            HibernateUtil.saveOrRemove(defaultAdmin, true);
        }
    }

    /**
     * Runs logic required to validate and login a user with the currently typed ID
     */
    private void login() {
        GridPane currentForm = isAdminLogin ? adminLoginForm : userLoginForm;

        if (!this.validateForm(currentForm)) {
            long loginId = Long.parseLong(isAdminLogin ? adminID.getText() : userID.getText());
            Member user = HibernateUtil.getMember(loginId);

            if(user != null) {
                this._Main.setUser(user);
                if (!isAdminLogin) {
                    clearInputFields();
                    this._Main.setScene(new Scene(new UserController()), 300000);
                } else if (user.getPassword() != null &&
                        user.getSalt() != null &&
                        PasswordUtil.verifyPassword(adminPassword.getText(), user.getPassword(), user.getSalt())) {
                    clearInputFields();
                    this._Main.setScene(new Scene(new AdminController()), 600000);
                } else {
                    errorBox.setText("Incorrect Login Details");
                }
            }
        }
    }

    /**
     * Will clear all the input fields of the login screen
     */
    private void clearInputFields() {
        userID.clear();
        adminID.clear();
        adminPassword.clear();
    }

    /**
     * Change the text and icon based on the current login screen
     *
     * @param isAdmin A boolean describing if the user is on the admin screen
     */
    private void setSwitch(boolean isAdmin) {
        Image image = new Image(isAdmin ? "images/admin_white.png" : "images/user_white.png");
        loginSwitch.setGraphic(new ImageView(image));
        loginSwitch.setText(isAdmin ? "Admin" : "User");
    }

    /**
     * Check the different input fields for the specific login form being used.
     * If there are validation issues the user will be informed and no further login logic will execute.
     *
     * @param form The form requiring the validation of type GridPane
     * @return A boolean value stating is there were any validation issues found.
     */
    private boolean validateForm(GridPane form) {
        boolean hasFailed = false;
        Tooltip tooltip = new Tooltip();
        if (form.equals(userLoginForm)) {
            if (userID.getText().equals("")) {
                tooltip.setText("Cannot be empty");
                errorBox.setText("The fire ID field must not be blank.");
                hasFailed = true;
            }
        } else if (form.equals(adminLoginForm)) {
            if (adminID.getText().equals("") || adminPassword.getText().equals("")) {
                errorBox.setText("The username and password fields must not be blank.");
                hasFailed = true;
            }
        }

        // Empty any error message
        if(!hasFailed) {errorBox.setText("");}

        return hasFailed;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Link enter press to login
        this.setOnKeyPressed((KeyEvent e) -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                login();
            }
        });

        loginBtn.setOnAction(ActionEvent -> login());

        // Login screen change login
        loginSwitch.setOnAction(ActionEvent -> {
            userLoginForm.setVisible(isAdminLogin);
            userLoginForm.setManaged(isAdminLogin);
            setSwitch(isAdminLogin);

            isAdminLogin = !isAdminLogin;

            adminLoginForm.setVisible(isAdminLogin);
            adminLoginForm.setManaged(isAdminLogin);
        });
        loginSwitch.toFront();

        // MAIN LOGO
        setSwitch(true);

        // Set the login on each numpad button
        for (Node node : numPad.getChildren()) {
            ((Button) node).setOnAction((ActionEvent e) -> {
                Button btn = (Button) e.getSource();
                String text = userID.getText();

                if (btn.getText().equals("del")) {
                    userID.setText(text.substring(0, text.length() - 1));
                } else {
                    userID.setText(text + btn.getText());
                }

                this.requestFocus();
            });
        }

        // Remove default focus actions on components
        Platform.runLater(this::requestFocus);
    }
}
