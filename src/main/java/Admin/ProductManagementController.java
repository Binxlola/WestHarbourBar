package main.java.Admin;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import main.java.HibernateUtil;
import main.java.Product;
import main.java.ProductCategory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class ProductManagementController extends AnchorPane implements Initializable {

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableView<ProductCategory> categoryTable;
    @FXML
    private Button productAdd, typeAdd;

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


    private Callback<TableColumn<Product, Void>, TableCell<Product, Void>> imageFactory() {
        return new Callback<>() {
            @Override
            public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                return new TableCell<>() {
                    private final ImageView image = new ImageView();

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            try {
                                Product product = getTableView().getItems().get(getIndex());

                                ByteArrayInputStream inputStream = new ByteArrayInputStream(product.getImage());
                                BufferedImage bufferedImage = ImageIO.read(inputStream);
                                image.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
                                setGraphic(image);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
            }
        };
    }

    private void addProductImages() {
        // Create 2 unnamed columns for the table
        TableColumn<Product, Void> image = new TableColumn<>("");

        // Crete the cell factor for each column of buttons
        image.setCellFactory(imageFactory());

        // Add the new button columns to the table
        productTable.getColumns().add(image);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        update();
        addProductImages();

        // Add action handling for the add member button
        productAdd.setOnAction((ActionEvent e) -> {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(new ProductAddController(stage, this)));
            stage.show();
        });

        typeAdd.setOnAction((ActionEvent e) -> {
            Dialog<String> dialog = new TextInputDialog();
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setHeaderText(null);
            dialog.setGraphic(null);
            dialog.setContentText("Category name:");

            Optional<String> result = dialog.showAndWait();
            AtomicReference<String> typeName = new AtomicReference<>("");

            result.ifPresent(typeName::set);

            HibernateUtil.saveOrRemove(new ProductCategory(typeName.get()), true);
            update();
        });

    }

    public void update() {
        productTable.setItems(HibernateUtil.getProducts());
        categoryTable.setItems(HibernateUtil.getProductCategories());
    }
}
