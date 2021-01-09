package main.java.Store;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import main.java.*;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class StoreController extends AnchorPane implements Initializable {

    private Parent store, history;

    @FXML private GridPane itemsContainer;
    @FXML private ComboBox<ProductCategory> categoryFilter;
    @FXML private ScrollPane itemsScroll;
    @FXML private Button logout, historyBtn;
    @FXML private Label userId, userBalance;
    @FXML private TableView<Purchase> transactions;
    private final Main _Main = Main.getInstance();
    private final Member member = _Main.getUser();

    public StoreController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Store.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void buildItems() {
        ObservableList<Product> productList = HibernateUtil.getProducts();
        int row = 0;
        int col = 0;

        for(Product product : productList) {
            BorderPane productCard = new BorderPane();

            //TODO add this to css sheet
            productCard.setStyle("-fx-border-color: black");

            Label name = new Label(product.getName());
            name.setPrefWidth(180.0);

            HBox productPrimaryInfo = new HBox(8);
            productPrimaryInfo.setAlignment(Pos.CENTER);

            Label cost = new Label("$" + String.valueOf(product.getCost()));
            cost.setPrefWidth(180.0);
            cost.setPrefHeight(25.0);
            Button buy = new Button("Buy");
            buy.setPrefWidth(180.0);
            buy.setPrefHeight(25.0);
            buy.setUserData(product);
            buy.setOnAction(this::handleBuy);
            productPrimaryInfo.getChildren().addAll(cost, buy);

            ImageView image = HibernateUtil.buildImage(product.getImage());
            image.setFitWidth(180.0);
            image.setFitHeight(180.0);

            productCard.setTop(name);
            productCard.setCenter(image);
            productCard.setBottom(productPrimaryInfo);
            
            itemsContainer.add(productCard, col, row);

            // Increment or reset
            if (col >= 2) {
                col = 0;
                row++;
            } else {
                col++;
            }
        }
    }

    private void handleBuy(ActionEvent e) {
        Button source = (Button) e.getSource();
        Product product = (Product) source.getUserData();
        Member user = _Main.getUser();
        Purchase purchase;

        float userBalance = user.getBalance();
        float productCost = product.getCost();

        if (userBalance >= productCost) {
            user.setBalance(userBalance - productCost);
            product.setQuantity(product.getQuantity() - 1);

            purchase = new Purchase(user, new Date(), product);
            user.addTransaction(purchase);

            // Save all the altered entities
            HibernateUtil.updateEntities(user, product);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buildItems();
        categoryFilter.setItems(HibernateUtil.getProductCategories());
        itemsScroll.setStyle("-fx-background-color:transparent;");

        // Setup Buttons
        logout.setGraphic(new ImageView("resources/logout.png"));
        logout.setTooltip(new Tooltip("Logout"));
        logout.getTooltip().setShowDelay(Duration.millis(700));
        logout.setOnAction((ActionEvent e) -> {_Main.logout();});

        historyBtn.setGraphic(new ImageView("resources/history.png"));
        historyBtn.setTooltip(new Tooltip("Purchase History"));
        historyBtn.getTooltip().setShowDelay(Duration.millis(700));

        userId.setText(userId.getText() + _Main.getUser().getId());
        userBalance.setText(userBalance.getText() + member.getBalance());

        transactions.setItems(member.getTransactions());
    }
}
