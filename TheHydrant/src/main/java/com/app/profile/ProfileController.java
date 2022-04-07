package com.app.profile;

import com.app.App;
import com.app.entities.Member;
import com.app.entities.Transaction;
import com.app.util.CommonUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController extends AnchorPane implements Initializable {

    private final Member member = App.getInstance().getUser();
    @FXML
    private Label memberID, memberBalance;
    @FXML
    private Button topUp;
    @FXML
    private TableView<Transaction> transactions;

    public ProfileController() {
        CommonUtil.buildView(this, "fxml/profile.xml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        memberID.setText("ID: " + member.getId());
        memberBalance.setText("Balance: " + member.getBalance());
        topUp.setText("Top Up");

        transactions.setItems(member.getTransactions());
    }
}
