package main.java.com.app.login;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import main.java.com.app.App;
import main.java.com.app.admin.AdminController;
import main.java.com.app.entities.Member;
import main.java.com.app.store.StoreController;
import main.java.com.app.util.HibernateUtil;
import main.java.com.app.util.PasswordUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends AnchorPane implements Initializable {

    @FXML private GridPane userLoginForm, adminLoginForm, numPad;
    @FXML private TextField userID, adminID;
    @FXML private PasswordField adminPassword;
    @FXML private Text errorBox;
    @FXML private Button loginBtn, loginSwitch;
    @FXML private ImageView logo;
    private boolean isAdmin = false;
    private final App _Main = App.getInstance();


    public LoginController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
        long loginId = Long.parseLong(isAdmin ? adminID.getText() : userID.getText());
        GridPane currentForm = isAdmin ? adminLoginForm : userLoginForm;

        if (!this.validateForm(currentForm)) {
            Member user = HibernateUtil.getMember(loginId);

            if (!isAdmin && user != null) {
                this._Main.setUser(user);
                this._Main.setScene(new Scene(new StoreController()));
                clearInputFields();
            } else if (isAdmin && PasswordUtil.verifyPassword(adminPassword.getText(), user.getPassword(), user.getSalt())) {
                this._Main.setScene(new Scene(new AdminController()));
                clearInputFields();
            } else {
                errorBox.setText("Incorrect Login Details");
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
        Image image = new Image(isAdmin ? "admin.png" : "user.png");
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
            userLoginForm.setVisible(isAdmin);
            userLoginForm.setManaged(isAdmin);
            setSwitch(isAdmin);

            isAdmin = !isAdmin;

            adminLoginForm.setVisible(isAdmin);
            adminLoginForm.setManaged(isAdmin);
        });
        loginSwitch.toFront();

        // MAIN LOGO
        logo.setImage(new Image("logo.jpg"));
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
