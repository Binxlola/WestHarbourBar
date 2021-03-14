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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.util.Duration;
import main.java.com.app.App;
import main.java.com.app.entities.Member;
import main.java.com.app.entities.Product;
import main.java.com.app.entities.ProductCategory;
import main.java.com.app.entities.Purchase;
import main.java.com.app.util.HibernateUtil;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class StoreController extends BorderPane implements Initializable {

    @FXML private GridPane storeContainer;
    @FXML private StackPane storeStack;
    @FXML private ComboBox<ProductCategory> categoryFilter;
    @FXML private ScrollPane storeScroll;
    @FXML private Button logoutBtn, storeBtn, historyBtn;
    @FXML private Label userId, userBalance, userName;
    @FXML private TableView<Purchase> transactions;
    private final App _Main = App.getInstance();
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

        for (Product product : productList) {
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

    private void writeLog(Member user, Purchase purchase) {
        File logFile = new File("C:/logs/" + user.getLastName() + ".txt");
        StringBuilder text = new StringBuilder();

        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter(logFile); BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String currentLine = reader.readLine();
            while (currentLine != null) {
                text.append(currentLine);
                currentLine = reader.readLine();
            }

            text.append(String.format("\n%s %s $%s", purchase.getItemName(), purchase.getDateOf(), purchase.getItemCost()));
            writer.write(text.toString());
        } catch (IOException e) {
            e.printStackTrace();
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
        Member user = _Main.getUser();
        Purchase purchase;

        float userBalance = user.getBalance();
        float productCost = product.getCost();

        user.setBalance(userBalance - productCost);
        product.setQuantity(product.getQuantity() - 1);

        purchase = new Purchase(user, new Date(), product);
        user.addTransaction(purchase);

        // Save all the altered entities
        HibernateUtil.saveOrRemove(true, purchase, user);

//        writeLog(user, purchase);
        update();

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setContentText("Purchased");
        alert.initOwner(_Main.getCurrentScene().getWindow());
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
        updateTransactionHistory();
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

    private void updateTransactionHistory() {
        transactions.setItems(member.getTransactions());
        transactions.refresh();
    }

    /**
     * Adds any extra requirements any button may need after it's initial creation
     */
    private void setupButtons() {
        logoutBtn.setGraphic(new ImageView("logout.png"));
        logoutBtn.setTooltip(new Tooltip("Logout"));
        logoutBtn.getTooltip().setShowDelay(Duration.millis(700));
        logoutBtn.setOnAction((ActionEvent e) -> _Main.logout());

        storeBtn.setGraphic(new ImageView("card.png"));
        storeBtn.setTooltip(new Tooltip("Store"));
        storeBtn.setOnAction(actionEvent -> storeScroll.toFront());
        storeBtn.getTooltip().setShowDelay(Duration.millis(700));

        historyBtn.setGraphic(new ImageView("history.png"));
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
        setupUserDetails();
        updateTransactionHistory();

        storeScroll.toFront();
    }
}
