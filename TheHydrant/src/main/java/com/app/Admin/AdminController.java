package main.java.com.app.Admin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import main.java.com.app.*;
import main.java.com.app.entities.Member;
import main.java.com.app.entities.Product;
import main.java.com.app.entities.ProductCategory;
import main.java.com.app.util.CommonUtil;
import main.java.com.app.util.HibernateUtil;

import javax.persistence.Entity;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController extends BorderPane implements Initializable {

    @FXML private StackPane adminStack;
    @FXML private GridPane productManagement;
    @FXML private AnchorPane productManagementContainer;
    @FXML private TableView<Member> membersTable;
    @FXML private TableView<Product> productTable;
    @FXML private TableView<ProductCategory> categoryTable;
    @FXML private Button logoutBtn, membersBtn, productsBtn,  productAdd, categoryAdd, memberAdd;

    private final App _Main = App.getInstance();


    public AdminController() {
        CommonUtil.buildView(this, "Admin.fxml");
    }

    private Callback<TableColumn<Product, Void>, TableCell<Product, Void>> imageFactory() {
        return new Callback<>() {
            @Override
            public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                return new TableCell<>() {

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Product product = getTableView().getItems().get(getIndex());
                            setGraphic(HibernateUtil.buildImageToSize(product.getImage(), 50, 50));
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

    /**
     * Will open the dialog used for any object that has edit functionality
     * @param e The action event used to get data about the object
     */
    public void openObjectEditAdd(ActionEvent e) {
        Button btn = (Button) e.getSource();
        String[] actionId = btn.getId().split(":");
        Stage stage = new Stage();

        Pane pane = switch (actionId[0]) {
            case "Member" ->
                    actionId[1].equals("Edit") ? new MemberController(stage, this, (Member) btn.getUserData()) : new MemberController(stage, this);
            case "Product" ->
                    actionId[1].equals("Edit") ? new ProductController(stage, this, (Product) btn.getUserData()) : new ProductController(stage, this, true);
            case "Category" ->
                    new ProductController(stage, this, false); // Category cant be edited
            default -> null;
        };

        stage.initOwner(_Main.getCurrentScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        assert pane != null;
        stage.setScene(new Scene(pane));
        stage.show();
    }

    public void handleObjectDel(ActionEvent e) {
        Object o = ((Button) e.getSource()).getUserData();
        HibernateUtil.saveOrRemove(o, false);
        this.update();
    }

    // === BUTTON FACTORY METHODS ===
    private Callback<TableColumn<Member, Void>, TableCell<Member, Void>> buttonFactoryMembers(EventHandler<ActionEvent> btnHandler, String iconName) {
        return param -> (TableCell<Member, Void>) createTableCellBtn(btnHandler, iconName);
    }

    private Callback<TableColumn<Product, Void>, TableCell<Product, Void>> buttonFactoryProducts(EventHandler<ActionEvent> btnHandler, String iconName) {
        return param -> (TableCell<Product, Void>) createTableCellBtn(btnHandler, iconName);
    }

    private Callback<TableColumn<ProductCategory, Void>, TableCell<ProductCategory, Void>> buttonFactoryCategories(EventHandler<ActionEvent> btnHandler, String iconName) {
        return param -> (TableCell<ProductCategory, Void>) createTableCellBtn(btnHandler, iconName);
    }


    /**
     * Given and event handler and image file name will create a button to be used on a table
     * @param btnHandler The event handler that is to be set on the button
     * @param iconName The image name of the icon to be set on the button
     * @return The table cell button
     */
    private TableCell<?, Void> createTableCellBtn(EventHandler<ActionEvent> btnHandler, String iconName) {
        return new TableCell<>() {
            private final Button btn = new Button();

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    btn.setGraphic(new ImageView(iconName));
                    btn.setOnAction(btnHandler);
                    btn.setUserData(getTableView().getItems().get(getIndex()));
                    setGraphic(btn);
                }
            }
        };
    }

    /**
     * Adds any action buttons required for a table
     */
    private void addTableButtons() {
        TableColumn<Member, Void> editBtnMember = new TableColumn<>("");
        TableColumn<Member, Void> removeBtnMember = new TableColumn<>("");
        TableColumn<Product, Void> editBtnProduct = new TableColumn<>("");
        TableColumn<Product, Void> removeBtnProduct = new TableColumn<>("");
        TableColumn<ProductCategory, Void> removeBtnCategory = new TableColumn<>("");

        // Crete the cell factory for each column of buttons and add to table
        editBtnMember.setId("Member:Edit");
        removeBtnMember.setId("Member");
        editBtnMember.setCellFactory(buttonFactoryMembers(this::openObjectEditAdd, "edit.png"));
        removeBtnMember.setCellFactory(buttonFactoryMembers(this::handleObjectDel, "delete.png"));
        membersTable.getColumns().add(editBtnMember);
        membersTable.getColumns().add(removeBtnMember);

        editBtnProduct.setId("Product:Edit");
        removeBtnProduct.setId("Product");
        editBtnProduct.setCellFactory(buttonFactoryProducts(this::openObjectEditAdd, "edit.png"));
        removeBtnProduct.setCellFactory(buttonFactoryProducts(this::handleObjectDel, "delete.png"));
        productTable.getColumns().add(editBtnProduct);
        productTable.getColumns().add(removeBtnProduct);

        removeBtnCategory.setId("Category");
        removeBtnCategory.setCellFactory(buttonFactoryCategories(this::handleObjectDel, "delete.png"));
        categoryTable.getColumns().add(removeBtnCategory);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        update();
        addProductImages();
        addTableButtons();

        logoutBtn.setGraphic(new ImageView("logout.png"));
        logoutBtn.setTooltip(new Tooltip("Logout"));
        logoutBtn.getTooltip().setShowDelay(Duration.millis(700));
        logoutBtn.setOnAction((ActionEvent e) -> _Main.logout());

        membersBtn.setGraphic(new ImageView("members.png"));
        membersBtn.setTooltip(new Tooltip("Manage Members"));
        membersBtn.getTooltip().setShowDelay(Duration.millis(700));
        membersBtn.setOnAction(ActionEvent -> membersTable.toFront());

        productsBtn.setGraphic(new ImageView("products.png"));
        productsBtn.setTooltip(new Tooltip("Manage Products"));
        productsBtn.getTooltip().setShowDelay(Duration.millis(700));
        productsBtn.setOnAction(ActionEvent -> productManagementContainer.toFront());

        // Add action handling for the add member button
        productAdd.setId("Product:Add");
        productAdd.setOnAction(this::openObjectEditAdd);

        categoryAdd.setId("Category:Add");
        categoryAdd.setOnAction(this::openObjectEditAdd);

        // Add action handling for the add member button
        memberAdd.setId("Member:Add");
        memberAdd.setOnAction(this::openObjectEditAdd);
    }

    public void update() {
        membersTable.setItems(HibernateUtil.getMembers());
        productTable.setItems(HibernateUtil.getProducts());
        categoryTable.setItems(HibernateUtil.getProductCategories());
    }
}
