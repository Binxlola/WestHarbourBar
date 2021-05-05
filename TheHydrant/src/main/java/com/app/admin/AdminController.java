package main.java.com.app.admin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import main.java.com.app.App;
import main.java.com.app.entities.Member;
import main.java.com.app.entities.Product;
import main.java.com.app.entities.ProductCategory;
import main.java.com.app.util.CommonUtil;
import main.java.com.app.util.HibernateUtil;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminController extends BorderPane implements Initializable {

    @FXML private StackPane adminStack;
    @FXML private GridPane productManagement;
    @FXML private AnchorPane productManagementContainer;
    @FXML private TableView<Member> membersTable;
    @FXML private TableView<Product> productTable;
    @FXML private TableView<ProductCategory> categoryTable;
    @FXML private Button logoutBtn, membersBtn, productsBtn,  productAdd, categoryAdd, memberAdd;

    private final App APP = App.getInstance();


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
     * Will open the dialog used for any object that has edit functionality. The controller to be opened will be based on the
     * ID set to the button clicked following the pattern 'Entity:Action'
     *
     * @param e The action event used to get data about the entity
     */
    private void openObjectEditAdd(ActionEvent e) {
        Button btn = (Button) e.getSource();
        String[] actionId = btn.getId().split(":");
        Stage stage = new Stage();

        Pane pane = switch (actionId[0]) {
            case "Member" -> actionId[1].equals("Edit") ? new MemberController(stage, this, (Member) btn.getUserData()) : new MemberController(stage, this);
            case "Product" -> actionId[1].equals("Edit") ? new ProductController(stage, this, (Product) btn.getUserData(), true) : new ProductController(stage, this, true);
            case "Category" -> new ProductController(stage, this, false); // Category cant be edited
            default -> null;
        };

        stage.initOwner(APP.getCurrentScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        assert pane != null;
        stage.setScene(new Scene(pane));
        stage.show();
    }

    /**
     * Is called when a specific value of an entity need to be updated by a given value.
     * The update taking place will be determined by the type of entity that is set as the buttons user data
     *
     * @param e The action event used to get data about the entity
     */
    private void openValueUpdate(ActionEvent e) {
        Button btn = (Button) e.getSource();
        Object entity = btn.getUserData();

        TextInputDialog dialog = new TextInputDialog();
        dialog.setContentText(String.format("Enter the change in %s:", entity instanceof Member ? "User balance" : "Stock level"));
        dialog.initOwner(APP.getCurrentScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);

        Optional<String> input = dialog.showAndWait();

        if (input.isPresent()) {
            if (entity instanceof Member) {
                ((Member) entity).updateBalance(Float.parseFloat(input.get()));
            } else if (entity instanceof Product) {
                ((Product) entity).updateQuantity(Integer.parseInt(input.get()));
            }

            HibernateUtil.saveOrRemove(entity, true);
            update();
        }

    }


    /**
     * Is called when user wishes to delete an entity from the database. Will get the entity as an object and call the
     * Hibernate utility method to remove the entity from the database
     *
     * @param e The click event of the delete button
     */
    private void handleObjectDel(ActionEvent e) {

        // Selection buttons
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.APPLY);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        // Build alert dialog
        Alert confirmDelete = new Alert(Alert.AlertType.WARNING);
        confirmDelete.initStyle(StageStyle.UNDECORATED);
        confirmDelete.initModality(Modality.APPLICATION_MODAL);
        confirmDelete.initOwner(APP.getCurrentScene().getWindow());
        confirmDelete.getButtonTypes().setAll(no, yes);
        confirmDelete.setTitle("Warning!");
        confirmDelete.setHeaderText("Are you sure you want to PERMANENTLY delete the selected item?");
        Optional<ButtonType> result = confirmDelete.showAndWait();

        // User confirms delete, so we delete item
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.APPLY) {
            Object o = ((Button) e.getSource()).getUserData();
            HibernateUtil.saveOrRemove(o, false);
            this.update();
        }
    }

    // === BUTTON FACTORY METHODS ===
    private Callback<TableColumn<ProductCategory, Void>, TableCell<ProductCategory, Void>> buttonFactoryCategories(EventHandler<ActionEvent> btnHandler, String btnID, String tooltipText) {
        return param -> (TableCell<ProductCategory, Void>) createTableCellBtn(btnHandler, "delete.png", btnID, tooltipText);
    }

    // === PRODUCT TABLE CELL FACTORIES
    private Callback<TableColumn<Product, Void>, TableCell<Product, Void>> buttonFactoryProducts(EventHandler<ActionEvent> btnHandler, String iconName, String btnID, String tooltipText) {
        return param -> (TableCell<Product, Void>) createTableCellBtn(btnHandler, iconName, btnID, tooltipText);
    }

    private Callback<TableColumn<Product, String>, TableCell<Product, String>> quantityFactoryProducts() {
        return param -> (TableCell<Product, String>) createColouredCell();
    }

    // === MEMBER TABLE CELL FACTORIES
    private Callback<TableColumn<Member, Void>, TableCell<Member, Void>> buttonFactoryMembers(EventHandler<ActionEvent> btnHandler, String iconName, String btnID, String tooltipText) {
        return param -> (TableCell<Member, Void>) createTableCellBtn(btnHandler, iconName, btnID, tooltipText);
    }

    private Callback<TableColumn<Member, Void>, TableCell<Member, Void>> checkBoxFactoryMembers() {
        return param -> createTableCheckBox();
    }

    private Callback<TableColumn<Member, String>, TableCell<Member, String>> balanceFactoryMembers() {
        return param -> (TableCell<Member, String>) createColouredCell();
    }


    /**
     * Given and event handler and image file name will create a button to be used on a table
     *
     * @param btnHandler The event handler that is to be set on the button
     * @param iconName   The image name of the icon to be set on the button
     * @param btnID      The ID to be set on the created button
     * @return The table cell button
     */
    private TableCell<?, Void> createTableCellBtn(EventHandler<ActionEvent> btnHandler, String iconName, String btnID, String tooltipText) {
        return new TableCell<>() {
            private final Button btn = new Button();

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    btn.setId(btnID);
                    btn.setGraphic(new ImageView(iconName));
                    btn.setOnAction(btnHandler);
                    btn.setUserData(getTableView().getItems().get(getIndex()));
                    btn.setTooltip(new Tooltip(tooltipText));
                    btn.getTooltip().setShowDelay(Duration.millis(700));
                    setGraphic(btn);
                }
            }
        };
    }

    /**
     * Creates a table cell that contains a check box
     *
     * @return The table cell containing the disabled checkbox
     */
    private TableCell<Member, Void> createTableCheckBox() {
        return new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Member member = getTableView().getItems().get(getIndex());
                    checkBox.setSelected(member.isAdmin());
                    checkBox.setDisable(true);
                    setGraphic(checkBox);
                }
            }
        };
    }

    /**
     * Changes the colour of cell text based on a negative or positive value (currently used for Members and Products)
     *
     * @return Coloured table cell
     */
    private TableCell<?, String> createColouredCell() {
        return new TableCell<>() {

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!isEmpty()) {
                    Object object = getTableView().getItems().get(getIndex());
                    float value = object instanceof Member ? ((Member) object).getBalance() : ((Product) object).getQuantity();
                    if (value != 0) {
                        this.setTextFill(value > 0 ? Color.GREEN : Color.RED);
                    }

                    this.setText(String.valueOf(value));

                }
            }
        };
    }

    /**
     * Generates the more complex cells for the members table. These cells require a unique cell factory
     */
    private void extendMemberTable() {
        TableColumn<Member, String> balance = new TableColumn<>("Balance");
        balance.setCellFactory(balanceFactoryMembers());
        membersTable.getColumns().add(balance);

        TableColumn<Member, Void> isAdmin = new TableColumn<>("Admin");
        isAdmin.setCellFactory(checkBoxFactoryMembers());
        membersTable.getColumns().add(isAdmin);

    }

    /**
     * Generates the more complex cells for the products table. These cells require a unique cell factory
     */
    private void extendProductsTable() {
        TableColumn<Product, String> quantity = new TableColumn<>("Quantity");
        quantity.setCellFactory(quantityFactoryProducts());
        productTable.getColumns().add(quantity);
    }

    /**
     * Builds all the tables for the admin section
     */
    private void buildTables() {
        extendMemberTable();
        extendProductsTable();

        addTableButtons();
    }

    /**
     * Adds any action buttons required for a table
     */
    private void addTableButtons() {
        // MEMBER TABLE BUTTONS
        TableColumn<Member, Void> editBtnMember = new TableColumn<>("");
        TableColumn<Member, Void> removeBtnMember = new TableColumn<>("");
        TableColumn<Member, Void> updateBalanceBtn = new TableColumn<>("");

        // PRODUCT TABLE BUTTONS
        TableColumn<Product, Void> editBtnProduct = new TableColumn<>("");
        TableColumn<Product, Void> removeBtnProduct = new TableColumn<>("");
        TableColumn<Product, Void> updateStockBtn = new TableColumn<>("");

        // CATEGORY TABLE BUTTONS
        TableColumn<ProductCategory, Void> removeBtnCategory = new TableColumn<>("");

        // Crete the cell factory for each column of buttons and add to table
        editBtnMember.setCellFactory(buttonFactoryMembers(this::openObjectEditAdd, "edit.png", "Member:Edit", "Edit Member"));
        updateBalanceBtn.setCellFactory(buttonFactoryMembers(this::openValueUpdate, "update_balance.png", "Member", "Update Balance"));
        removeBtnMember.setCellFactory(buttonFactoryMembers(this::handleObjectDel, "delete.png", "Member", "Delete Member"));
        membersTable.getColumns().add(editBtnMember);
        membersTable.getColumns().add(updateBalanceBtn);
        membersTable.getColumns().add(removeBtnMember);

        editBtnProduct.setCellFactory(buttonFactoryProducts(this::openObjectEditAdd, "edit.png", "Product:Edit", "Edit Product"));
        updateStockBtn.setCellFactory(buttonFactoryProducts(this::openValueUpdate, "add_stock.png", "Product", "Update Stock"));
        removeBtnProduct.setCellFactory(buttonFactoryProducts(this::handleObjectDel, "delete.png", "Product", "Delete Product"));
        productTable.getColumns().add(editBtnProduct);
        productTable.getColumns().add(updateStockBtn);
        productTable.getColumns().add(removeBtnProduct);

        removeBtnCategory.setCellFactory(buttonFactoryCategories(this::handleObjectDel, "Category", "Delete Category"));
        categoryTable.getColumns().add(removeBtnCategory);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        update();
        addProductImages();
        buildTables();

        logoutBtn.setGraphic(new ImageView("logout.png"));
        logoutBtn.setTooltip(new Tooltip("Logout"));
        logoutBtn.getTooltip().setShowDelay(Duration.millis(700));
        logoutBtn.setOnAction((ActionEvent e) -> APP.logout());

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
        productAdd.setGraphic(new ImageView("add_row.png"));
        productAdd.setOnAction(this::openObjectEditAdd);

        categoryAdd.setId("Category:Add");
        categoryAdd.setGraphic(new ImageView("add_row.png"));
        categoryAdd.setOnAction(this::openObjectEditAdd);

        // Add action handling for the add member button
        memberAdd.setId("Member:Add");
        memberAdd.setGraphic(new ImageView("add_row.png"));
        memberAdd.setOnAction(this::openObjectEditAdd);
    }

    public void update() {
        membersTable.setItems(HibernateUtil.getMembers());
        productTable.setItems(HibernateUtil.getProducts());
        categoryTable.setItems(HibernateUtil.getProductCategories());
    }
}
