package main.java.com.app.store;

import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.util.Duration;
import main.java.com.app.App;
import main.java.com.app.entities.Member;
import main.java.com.app.entities.Product;
import main.java.com.app.entities.ProductCategory;
import main.java.com.app.entities.Purchase;
import main.java.com.app.sharedComponents.Transactions;
import main.java.com.app.util.HibernateUtil;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class StoreController extends AnchorPane implements Initializable {

    @FXML private GridPane productsContainer;
    @FXML private TabPane storeTabs;
    @FXML private ComboBox<ProductCategory> categoryFilter;
    @FXML private ScrollPane productsScroll;
    @FXML private Button logoutBtn;
    @FXML private Label userId, userBalance, userName;
    @FXML private Transactions transactions;
    private final App APP = App.getInstance();
    private final Member member = APP.getUser();

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
        ObservableList<? extends Product> productList = HibernateUtil.getProducts(Product.ProductVisibility.PUBLIC);
        int row = 0;
        int col = 0;

        for (Product product : productList) {
            BorderPane productCard = new BorderPane();

            //TODO add this to css sheet
            productCard.setStyle("-fx-border-color: black");

            Label name = new Label(product.getName());
            name.setPrefWidth(180.0);

            // Set up products info and action bar
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

            // Set image for product
            ImageView image = HibernateUtil.buildImage(product.getImage());
            image.setFitWidth(180.0);
            image.setFitHeight(180.0);

            productCard.setTop(name);
            productCard.setCenter(image);
            productCard.setBottom(productPrimaryInfo);

            productsContainer.add(productCard, col, row);

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
     *
     * @param e The ActionEvent used to get the product data
     */
    private void handleBuy(ActionEvent e) {
        Button source = (Button) e.getSource();
        Product product = (Product) source.getUserData();
        Member user = APP.getUser();
        Purchase purchase;

        float userBalance = user.getBalance();
        float productCost = product.getCost();

        user.setBalance(userBalance - productCost);
        product.setQuantity(product.getQuantity() - 1);

        purchase = new Purchase(user, new Date(), product);
        user.addTransaction(purchase);

        // Save all the altered entities
        HibernateUtil.saveOrRemove(true, purchase, user, product);

        update();

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setContentText("Purchased");
        alert.initOwner(APP.getCurrentScene().getWindow());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event -> {
            alert.setResult(ButtonType.CANCEL);
            alert.close();
        });
        delay.play();
    }

    private void update() {
        buildItems();
        setUserBalance();
        transactions.update();
    }

    private void setupUserDetails() {
        userId.setText("ID: " + member.getId());
        userName.setText("Name: " + member.getFirstName() + " " + member.getLastName());
        setUserBalance();
    }

    private void setUserBalance() {
        float balance = member.getBalance();
        userBalance.setText("Balance: " + balance);
        if (balance != 0) {
            userBalance.setTextFill(balance > 0 ? Color.GREEN : Color.RED);
        }
    }

    /**
     * Adds any extra requirements any button may need after it's initial creation
     */
    private void setupButtons() {
        logoutBtn.setGraphic(new ImageView("logout.png"));
        logoutBtn.setTooltip(new Tooltip("Logout"));
        logoutBtn.getTooltip().setShowDelay(Duration.millis(700));
        logoutBtn.setOnAction((ActionEvent e) -> APP.logout());
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buildItems();
        transactions.setUser(member);
        transactions.build();
        categoryFilter.setItems(HibernateUtil.getProductCategories());
        productsScroll.setStyle("-fx-background-color:transparent;");

        setupButtons();
        setupUserDetails();

        productsScroll.toFront();
    }
}
