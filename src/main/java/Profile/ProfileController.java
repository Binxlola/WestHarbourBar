package main.java.Profile;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import main.java.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController extends AnchorPane implements Initializable {

    private final Member member = Main.getInstance().getUser();
    @FXML
    private Label memberID, memberBalance;
    @FXML
    private Button topUp;
    @FXML
    private TableView<Purchase> transactions;

    public ProfileController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("profile.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        memberID.setText("ID: " + member.getId());
        memberBalance.setText("Balance: " + member.getBalance());
        topUp.setText("Top Up");

        transactions.setItems(member.getTransactions());
    }
}
