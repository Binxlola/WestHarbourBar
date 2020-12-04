package main.java.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.java.HibernateUtil;
import main.java.Member;
import main.java.ProductCategory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductAddController extends AnchorPane implements Initializable {

    private final Stage parentStage;
    private final ProductManagementController parentController;
    @FXML private TextField name, quantity;
    @FXML private ComboBox<ProductCategory> category;
    @FXML private Button apply, cancel;

    public ProductAddController(Stage parentStage, ProductManagementController parentController) {
        this.parentStage = parentStage;
        this.parentController = parentController;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProductAdd.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handler(ActionEvent e) {
        Object source = e.getSource();
        if(source.equals(cancel)) {
            parentStage.close();
        } else if(source.equals(apply)) {

            parentController.update();
            parentStage.close();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        category.setItems(HibernateUtil.getProductCategories());

        apply.setOnAction(this::handler);
        cancel.setOnAction(this::handler);
    }
}
