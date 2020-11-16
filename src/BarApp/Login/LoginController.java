package BarApp.Login;

import BarApp.Admin.AdminController;
import BarApp.DatabaseConnector;
import BarApp.Main;
import BarApp.MainController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.h2.jdbc.JdbcSQLNonTransientConnectionException;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController extends AnchorPane implements Initializable {

    @FXML private GridPane userLoginForm, adminLoginForm;
    @FXML private TextField userIDField, adminName;
    @FXML private PasswordField adminPassword;
    @FXML private Text errorBox;
    @FXML private Button loginBtn, adminLogin;
    @FXML private ImageView logo;
    private boolean isAdmin = false;
    private final Main _Main = Main.getMain();

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
        boolean isConnected = false;

        if(source.equals(loginBtn)) {
            if(!this.validateForm(currentForm)) {

                try {
                    isConnected = this.connectDB(isAdmin ? adminName.getText() : userIDField.getText(), isAdmin ? adminPassword.getText() : "");
                } catch (Exception e) {
                    errorBox.setText("There was an error connection to the Database");
                }

                if(isConnected) {
                    Parent root = isAdmin ? new AdminController() : new MainController();

                    userIDField.clear();
                    errorBox.setText("");

                    // Change and set new Scene eg. The shop window
                    this._Main.setScene(new Scene(root));
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
     * Attempts to establish a connection to the database, if one does not exist and a admin login path is being used
     * a new database will be created with default values. Otherwise a connection will be made if the user exists;
     * @param username The username of the user trying to connect for non admin user will default to "USER"
     * @param password The password of the user trying to connect for no admin user will default to ""
     * @return A boolean notifying if a connection has been made successfully
     * @throws SQLException An exception thrown from a connection attempt to database
     */
    private boolean connectDB(String username, String password) throws SQLException {
        boolean isConnected = false;
        String url = "jdbc:h2:/" + System.getProperty("user.dir") + "/TheHydrant";
        try {
            Connection conn = DriverManager.getConnection(url + ";IFEXISTS=TRUE", username, password);

            this._Main.setConnection(conn);
            isConnected = true;
        } catch (JdbcSQLNonTransientConnectionException ignored) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            // No database exists, create new one with default values
            if(isAdmin) {
                Connection conn = DriverManager.getConnection(url, "Admin", "Admin");
                DatabaseConnector.buildDefaultTables(conn);

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Database created with a single Administrator");
                alert.setContentText("Username: Admin\nPassword: Admin");
                alert.showAndWait();

                this._Main.setConnection(conn);
                isConnected = true;

            } else {

                // Notify user of non-existent database
                alert.setHeaderText("No Database");
                alert.setContentText("A database has not been created, contact you administrator to have one created");
                alert.showAndWait();
            }
        }

        return isConnected;
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
            if(userIDField.getText().equals("")) {
                tooltip.setText("Cannot be empty");
                errorBox.setText("The fire ID field must not be blank.");
                hasFailed = true;
            }
        } else if (form.equals(adminLoginForm)) {
            if(adminName.getText().equals("") || adminPassword.getText().equals("")) {
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
        image = new Image("BarApp/logo.jpg");
        logo.setImage(image);

        // ADMIN pathway button
        image = new Image("BarApp/admin.png");
        adminLogin.setGraphic(new ImageView(image));

        // Remove default focus actions on components
        Platform.runLater(this::requestFocus);
    }
}
