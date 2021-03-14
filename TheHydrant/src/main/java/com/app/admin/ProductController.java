package main.java.com.app.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.com.app.entities.Product;
import main.java.com.app.entities.ProductCategory;
import main.java.com.app.util.CommonUtil;
import main.java.com.app.util.HibernateUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductController extends AnchorPane implements Initializable {

    private Stage parentStage;
    private AdminController parentController;
    private byte[] selectedImageFile = null;
    private String imageFileName;
    private boolean isProduct;
    private boolean isEdit;
    private Product product = null;
    private ProductCategory category;
    @FXML private GridPane productGrid, categoryGrid;
    @FXML private TextField productName, categoryName, quantity, cost;
    @FXML private ComboBox<ProductCategory> categoryComboBox;
    @FXML private Button apply, cancel, imageSelect;
    @FXML private Label imageSelected;

    public ProductController(Stage parentStage, AdminController parentController, boolean isProduct) {
        setCommonVariables(parentStage, parentController, isProduct, false);
        CommonUtil.buildView(this, "Product.fxml");
        setManaged();
    }

    public ProductController(Stage parentStage, AdminController parentController, Product product, boolean isProduct) {
        setCommonVariables(parentStage, parentController, isProduct, true);
        this.product = product;
        CommonUtil.buildView(this, "Product.fxml");
        setManaged();
    }

    /**
     * A helper method to set the common variable used across class constructors
     */
    private void setCommonVariables(Stage parentStage, AdminController parentController, boolean isProduct, boolean isEdit) {
        this.parentStage = parentStage;
        this.parentController = parentController;
        this.isProduct = isProduct;
        this.isEdit = isEdit;
    }

    /**
     * Will determine which grid to manage and make visible
     */
    private void setManaged() {
        productGrid.setVisible(isProduct);
        productGrid.setManaged(isProduct);

        categoryGrid.setVisible(!isProduct);
        categoryGrid.setManaged(!isProduct);
    }

    /**
     * Creates a new product object and saves it to the database
     */
    private void addProduct() {
        //#TODO error handling

        // New product is being created
        if(!isEdit) {product = new Product();}

        product.setName(productName.getText());
        product.setCategory(categoryComboBox.getValue());
        product.setCost(Float.parseFloat(cost.getText()));
        product.setQuantity(Integer.parseInt(quantity.getText()));
        product.setImage(selectedImageFile);
        product.setImageFileName(imageFileName);

        HibernateUtil.saveOrRemove(product, true);

        parentController.update();
        parentStage.close();
    }

    /**
     * Handles the selection of an image for a new product being created
     */
    private void selectProductImage() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        try {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(stage);

            BufferedImage bImage = ImageIO.read(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "jpg", bos);
            selectedImageFile = bos.toByteArray();

            imageSelected.setText(file.getName());
            imageFileName = file.getName();

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Creates a new category object and saves it to the database
     */
    private void addCategory() {
        ProductCategory category = new ProductCategory();
        String name = categoryName.getText();
        //#TODO error handling for this
        if (!name.equals("")) {
            category.setName(name);
            HibernateUtil.saveOrRemove(category, true);
            parentController.update();
            parentStage.close();
        }
    }

    /**
     * The main button handler for the controller
     *
     * @param e The action event taking place
     */
    private void handler(ActionEvent e) {
        Object source = e.getSource();

        if (source.equals(cancel)) {
            parentStage.close();
        } else if (source.equals(apply)) {
            // Are we adding product or category?
            if (isProduct) {
                addProduct();
            } else {
                addCategory();
            }
        } else if (source.equals(imageSelect)) {
            selectProductImage();
        }
    }

    /**
     * Called on controller initialization, and will fill in any form values that should have a set starting value.
     * Will also disable certain fields that can not be changed once set.
     */
    private void setupFormFields() {
        if (isEdit) {
            productName.setText(product.getName());
            categoryComboBox.setValue(product.getCategory());
            quantity.setText(String.valueOf(product.getQuantity()));
            cost.setText(String.valueOf(product.getCost()));
            imageSelected.setText(product.getImageFileName());
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categoryComboBox.setItems(HibernateUtil.getProductCategories());
        setupFormFields();

        apply.setOnAction(this::handler);
        cancel.setOnAction(this::handler);
        imageSelect.setOnAction(this::handler);
    }
}
