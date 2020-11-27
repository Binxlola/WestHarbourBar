package main.java.Admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import main.java.Brand;
import main.java.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductManagementController extends AnchorPane implements Initializable {

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableView<Brand> brandTable;
    @FXML
    private Button productAdd;

    public ProductManagementController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProductManagement.fxml"));
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

    }
}
