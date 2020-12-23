package main.java.Store;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import main.java.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StoreController extends AnchorPane implements Initializable {

    @FXML
    private GridPane itemsContainer;
    @FXML
    private ComboBox<ProductCategory> categoryFilter;
    private Main _Main = Main.getInstance();

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
            name.setPrefWidth(200.0);

            HBox productPrimaryInfo = new HBox(8);
            productPrimaryInfo.setAlignment(Pos.CENTER);

            Label cost = new Label("$" + String.valueOf(product.getCost()));
            cost.setPrefWidth(200.0);
            cost.setPrefHeight(25.0);
            Button buy = new Button("Buy");
            buy.setPrefWidth(200.0);
            buy.setPrefHeight(25.0);
            buy.setUserData(product);
            buy.setOnAction(this::handleBuy);
            productPrimaryInfo.getChildren().addAll(cost, buy);

            ImageView image = HibernateUtil.buildImage(product.getImage());
            image.setFitWidth(200.0);
            image.setFitHeight(200.0);

            productCard.setTop(name);
            productCard.setCenter(image);
            productCard.setBottom(productPrimaryInfo);
            
            itemsContainer.add(productCard, col, row);

            // Increment or reset
            col = col >= 3 ? 0 : col + 1;



        }
    }

    private void handleBuy(ActionEvent e) {
        Button source = (Button) e.getSource();
        Product product = (Product) source.getUserData();
        Member user = _Main.getUser();

        float userBalance = user.getBalance();
        float productCost = product.getCost();

        if (userBalance >= productCost) {
            user.setBalance(userBalance - productCost);
            product.setQuantity(product.getQuantity() - 1);

            // Save all the altered entities
            HibernateUtil.updateEntities(user, product);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buildItems();
        categoryFilter.setItems(HibernateUtil.getProductCategories());
    }
}
