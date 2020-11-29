package main.java.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import main.java.Brand;
import main.java.HibernateUtil;
import main.java.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class ProductManagementController extends AnchorPane implements Initializable {

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableView<Brand> brandTable;
    @FXML
    private Button productAdd, brandAdd;

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
        update();

        brandAdd.setOnAction((ActionEvent e) -> {
            Dialog<String> dialog = new TextInputDialog();
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setHeaderText(null);
            dialog.setGraphic(null);
            dialog.setContentText("Brand name:");

            Optional<String> result = dialog.showAndWait();
            AtomicReference<String> brandName = new AtomicReference<>("");

            result.ifPresent(brandName::set);

            HibernateUtil.saveOrRemove(new Brand(brandName.get()), true);
            update();
        });

    }

    private void update() {
        brandTable.setItems(HibernateUtil.getBrands());
    }
}
