package main.java.com.app.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import main.java.com.app.App;
import main.java.com.app.entities.Member;
import main.java.com.app.sharedComponents.store.StoreController;
import main.java.com.app.sharedComponents.transactions.TransactionsController;
import main.java.com.app.util.CommonUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class UserController extends AnchorPane implements Initializable {

    @FXML private StackPane userStack;
    @FXML private StoreController store;
    @FXML private TransactionsController transactions;
    @FXML private Button logoutBtn;

    private final App APP = App.getInstance();
    private final Member member = APP.getUser();


    public UserController() {
        CommonUtil.buildView(this, "user.fxml");
    }

    private void update() {
        transactions.update();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        transactions.setUser(member);
        transactions.build();

        logoutBtn.setGraphic(new ImageView("logout.png"));
        logoutBtn.setTooltip(new Tooltip("Logout"));
        logoutBtn.getTooltip().setShowDelay(Duration.millis(700));
        logoutBtn.setOnAction((ActionEvent e) -> APP.logout());
    }
}
