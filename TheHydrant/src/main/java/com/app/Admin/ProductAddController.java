package main.java.com.app.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.com.app.HibernateUtil;
import main.java.com.app.Product;
import main.java.com.app.ProductCategory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductAddController extends AnchorPane implements Initializable {

    private final Stage parentStage;
    private final AdminController parentController;
    private byte[] selectedImageFile = null;
    @FXML private TextField name, quantity, cost;
    @FXML private ComboBox<ProductCategory> category;
    @FXML private Button apply, cancel, imageSelect;
    @FXML private Label imageSelected;

    public ProductAddController(Stage parentStage, AdminController parentController) {
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
            Product newProduct = new Product();
            newProduct.setName(name.getText());
            newProduct.setCost(Float.parseFloat(cost.getText()));
            newProduct.setQuantity(Integer.parseInt(quantity.getText()));
            newProduct.setCategory(category.getValue());
            newProduct.setImage(selectedImageFile);

            HibernateUtil.saveOrRemove(newProduct, true);

            parentController.update();
            parentStage.close();
        } else if(source.equals(imageSelect)) {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);

            try {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showOpenDialog(stage);
//                stage.showAndWait();

                BufferedImage bImage = ImageIO.read(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(bImage, "jpg", bos );
                selectedImageFile = bos.toByteArray();

                imageSelected.setText(file.getName());

            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        category.setItems(HibernateUtil.getProductCategories());

        apply.setOnAction(this::handler);
        cancel.setOnAction(this::handler);
        imageSelect.setOnAction(this::handler);
    }
}
