package main.java.Login;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import main.java.Admin.AdminController;
import main.java.HibernateUtil;
import main.java.Main;
import main.java.Member;
import main.java.Store.StoreController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends AnchorPane implements Initializable {

    @FXML private GridPane userLoginForm, adminLoginForm;
    @FXML private TextField userID, adminID;
    @FXML private PasswordField adminPassword;
    @FXML private Text errorBox;
    @FXML private Button loginBtn, adminLogin;
    @FXML private ImageView logo;
    private boolean isAdmin = false;
    private final Main _Main = Main.getInstance();

    public LoginController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void handleLogin(ActionEvent actionEvent) {

        Object source = actionEvent.getSource();
        GridPane currentForm = isAdmin ? adminLoginForm : userLoginForm;

        if(source.equals(loginBtn)) {
            long loginId = Long.parseLong(isAdmin ? adminID.getText() : userID.getText());

            if(!this.validateForm(currentForm)) {
                Member user = HibernateUtil.getMember(loginId);

                if (!isAdmin && user != null) {
                    this._Main.setUser(user);
                    this._Main.setScene(new Scene(new StoreController()));
                } else if (isAdmin) {
                    this._Main.setScene(new Scene(new AdminController()));
                } else {
                    errorBox.setText("Incorrect Login Details");
                }
            }
        }else if(source.equals(adminLogin)) {
            this.userLoginForm.setVisible(isAdmin);
            this.userLoginForm.setManaged(isAdmin);

            isAdmin = !isAdmin;

            this.adminLoginForm.setVisible(isAdmin);
            this.adminLoginForm.setManaged(isAdmin);
        }
    }

    /**
     * Check the different input fields for the specific login form being used.
     * If there are validation issues the user will be informed and no further login logic will execute.
     * @param form The form requiring the validation of type GridPane
     * @return A boolean value stating is there were any validation issues found.
     */
    private boolean validateForm(GridPane form) {
        boolean hasFailed = false;
        Tooltip tooltip = new Tooltip();
        if(form.equals(userLoginForm)) {
            if(userID.getText().equals("")) {
                tooltip.setText("Cannot be empty");
                errorBox.setText("The fire ID field must not be blank.");
                hasFailed = true;
            }
        } else if (form.equals(adminLoginForm)) {
            if(adminID.getText().equals("") || adminPassword.getText().equals("")) {
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
        Image image;

        loginBtn.setOnAction(this::handleLogin);
        adminLogin.setOnAction(this::handleLogin);
        adminLogin.toFront(); // Make sure button always clickable

        // MAIN LOGO
        image = new Image("resources/logo.jpg");
        logo.setImage(image);

        // ADMIN pathway button
        image = new Image("resources/admin.png");
        adminLogin.setGraphic(new ImageView(image));

        // Remove default focus actions on components
        Platform.runLater(this::requestFocus);
    }
}
