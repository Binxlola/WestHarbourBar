package com.app.sharedComponents.store;

import com.app.entities.Product;
import com.app.util.CommonUtil;
import com.app.util.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductCard extends BorderPane implements Initializable {

    @FXML
    Label nameLabel, costLabel;
    @FXML
    Button buyBtn;
    @FXML
    ImageView itemImg;

    private final Product item;
    private final EventHandler<ActionEvent> handler;

    public ProductCard(Product item, EventHandler<ActionEvent> handler) {
        this.item = item;
        this.handler = handler;

        CommonUtil.buildView(this, "fxml/productCard.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO add this to css sheet
        setStyle("-fx-border-color: black");
        ImageView image = HibernateUtil.buildImage(item.getImage());

        nameLabel.setText(item.getName());
        costLabel.setText("$" + item.getCost());
        buyBtn.setUserData(item);
        buyBtn.setOnAction(this.handler);
        itemImg.setImage(image.getImage());
    }
}
