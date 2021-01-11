package main.java.Store;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;
import main.java.*;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class StoreController extends BorderPane implements Initializable {

    @FXML private GridPane storeContainer;
    @FXML private StackPane storeStack;
    @FXML private ComboBox<ProductCategory> categoryFilter;
    @FXML private ScrollPane storeScroll;
    @FXML private Button logoutBtn, storeBtn, historyBtn;
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

    /**
     * Builds the product cards and adds them to the store GridPane
     */
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

            Label cost = new Label("$" + product.getCost());
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
            
            storeContainer.add(productCard, col, row);

            // Increment or reset
            if (col >= 2) {
                col = 0;
                row++;
            } else {
                col++;
            }
        }
    }

    /**
     * Handles a user purchasing an item from the store
     * @param e The ActionEvent used to get the product data
     */
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

    /**
     * Adds any extra requirements any button may need after it's initial creation
     */
    private void setupButtons() {
        logoutBtn.setGraphic(new ImageView("resources/logout.png"));
        logoutBtn.setTooltip(new Tooltip("Logout"));
        logoutBtn.getTooltip().setShowDelay(Duration.millis(700));
        logoutBtn.setOnAction((ActionEvent e) -> _Main.logout());

        storeBtn.setGraphic(new ImageView("resources/card.png"));
        storeBtn.setTooltip(new Tooltip("Store"));
        storeBtn.setOnAction(actionEvent -> storeScroll.toFront());
        storeBtn.getTooltip().setShowDelay(Duration.millis(700));

        historyBtn.setGraphic(new ImageView("resources/history.png"));
        historyBtn.setTooltip(new Tooltip("Purchase History"));
        historyBtn.setOnAction(actionEvent -> transactions.toFront());
        historyBtn.getTooltip().setShowDelay(Duration.millis(700));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buildItems();
        categoryFilter.setItems(HibernateUtil.getProductCategories());
        storeScroll.setStyle("-fx-background-color:transparent;");

        setupButtons();

        userId.setText(userId.getText() + _Main.getUser().getId());
        userBalance.setText(userBalance.getText() + member.getBalance());

        transactions.setItems(member.getTransactions());

        storeScroll.toFront();
    }
}
