package com.app.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import com.app.App;
import com.app.entities.BalanceModify;
import com.app.entities.Member;
import com.app.entities.Product;
import com.app.entities.ProductCategory;
import com.app.sharedComponents.store.StoreController;
import com.app.sharedComponents.transactions.TransactionsController;
import com.app.util.CommonUtil;
import com.app.util.HibernateUtil;

import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminController extends AnchorPane implements Initializable {

    @FXML private StackPane adminStack;
    @FXML private GridPane productManagement;
    @FXML private AnchorPane productManagementContainer;
    @FXML private TableView<Member> membersTable;
    @FXML private TableView<Product> productTable;
    @FXML private TableView<ProductCategory> categoryTable;
    @FXML private StoreController store;
    @FXML private Button logoutBtn, productAdd, categoryAdd, memberAdd;

    private final App APP = App.getInstance();


    public AdminController() {
        CommonUtil.buildView(this, "fxml/Admin.fxml");
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
        TableColumn<Product, Void> image = new TableColumn<>("Image");

        // Crete the cell factor for each column of buttons
        image.setCellFactory(imageFactory());

        // Add the new button columns to the table
        productTable.getColumns().add(image);
    }

    /**
     * Will open the dialog used for any object that has edit functionality. The controller to be opened will be based on the
     * ID set to the button clicked following the pattern 'Member:Action'
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
            case "Category" -> new ProductController(stage, this, false); // Category can't be edited
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
                Member member = (Member) entity;
                float value = Float.parseFloat(input.get());

                member.updateBalance(Float.parseFloat(input.get()));
                BalanceModify transaction = new BalanceModify((Member) entity, new Date(), value);
                member.addTransaction(transaction);
                HibernateUtil.saveOrRemove(true, transaction, member);
            } else if (entity instanceof Product) {
                ((Product) entity).updateQuantity(Integer.parseInt(input.get()));
                HibernateUtil.saveOrRemove(entity, true);
            }

            try {
                update();
            } catch (IllegalAccessException exception) {
                exception.printStackTrace();
            }
        }

    }

    /**
     * Will open the dialog used for any object that has edit functionality. The controller to be opened will be based on the
     * ID set to the button clicked following the pattern 'Entity:Action'
     *
     * @param e The action event used to get data about the entity
     */
    private void openTransactions(ActionEvent e) {
        Button btn = (Button) e.getSource();
        Stage stage = new Stage();
        Pane pane = new Pane();
        pane.getChildren().add(new TransactionsController((Member) btn.getUserData()));

        stage.initOwner(APP.getCurrentScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setScene(new Scene(pane));
        stage.setResizable(false);
        stage.show();
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


            try {
                this.update();
            } catch (IllegalAccessException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Generates the more complex cells for the members table. These cells require a unique cell factory
     */
    private void extendMemberTable() {
        TableColumn<Member, Object> balance = new TableColumn<>("Balance");
        balance.setCellFactory(new MemberTableFactory(MemberTableFactory.ColumnType.BALANCE));
        membersTable.getColumns().add(balance);

        TableColumn<Member, Object> isAdmin = new TableColumn<>("Admin");
        isAdmin.setCellFactory(new MemberTableFactory(MemberTableFactory.ColumnType.CHECK_BOX));
        membersTable.getColumns().add(isAdmin);

    }

    /**
     * Generates the more complex cells for the products table. These cells require a unique cell factory
     */
    private void extendProductsTable() {
        TableColumn<Product, Object> quantity = new TableColumn<>("Quantity");
        quantity.setCellFactory(new ProductTableFactory(ProductTableFactory.ColumnType.BALANCE));
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
        TableColumn<Member, Object> editBtnMember = new TableColumn<>("");
        TableColumn<Member, Object> removeBtnMember = new TableColumn<>("");
        TableColumn<Member, Object> viewTransactionsBtn = new TableColumn<>("");
        TableColumn<Member, Object> updateBalanceBtn = new TableColumn<>("");

        // PRODUCT TABLE BUTTONS
        TableColumn<Product, Object> editBtnProduct = new TableColumn<>("");
        TableColumn<Product, Object> removeBtnProduct = new TableColumn<>("");
        TableColumn<Product, Object> updateStockBtn = new TableColumn<>("");

        // CATEGORY TABLE BUTTONS
        TableColumn<ProductCategory, Object> removeBtnCategory = new TableColumn<>("");

        // Crete the cell factory for each column of buttons and add to table
        editBtnMember.setCellFactory(new MemberTableFactory(this::openObjectEditAdd, "images/edit.png", "Member:Edit", "Edit Member", MemberTableFactory.ColumnType.BUTTON));
        updateBalanceBtn.setCellFactory(new MemberTableFactory(this::openValueUpdate, "images/update_balance.png", "Member", "Update Balance", MemberTableFactory.ColumnType.BUTTON));
        viewTransactionsBtn.setCellFactory(new MemberTableFactory(this::openTransactions, "images/bill.png", "Member", "Update Balance", MemberTableFactory.ColumnType.BUTTON));
        removeBtnMember.setCellFactory(new MemberTableFactory(this::handleObjectDel, "images/delete.png", "Member", "Delete Member", MemberTableFactory.ColumnType.BUTTON));
        membersTable.getColumns().add(editBtnMember);
        membersTable.getColumns().add(updateBalanceBtn);
        membersTable.getColumns().add(viewTransactionsBtn);
        membersTable.getColumns().add(removeBtnMember);

        editBtnProduct.setCellFactory(new ProductTableFactory(this::openObjectEditAdd, "images/edit.png", "Product:Edit", "Edit Product", ProductTableFactory.ColumnType.BUTTON));
        updateStockBtn.setCellFactory(new ProductTableFactory(this::openValueUpdate, "images/add_stock.png", "Product", "Update Stock", ProductTableFactory.ColumnType.BUTTON));
        removeBtnProduct.setCellFactory(new ProductTableFactory(this::handleObjectDel, "images/delete.png", "Product", "Delete Product", ProductTableFactory.ColumnType.BUTTON));
        productTable.getColumns().add(editBtnProduct);
        productTable.getColumns().add(updateStockBtn);
        productTable.getColumns().add(removeBtnProduct);

        removeBtnCategory.setCellFactory(new CategoryTableFactory(this::handleObjectDel, "Category", "Delete Category", CategoryTableFactory.ColumnType.BUTTON));
        categoryTable.getColumns().add(removeBtnCategory);
    }

    public void update() throws IllegalAccessException {
        membersTable.setItems(HibernateUtil.getMembers());
        productTable.setItems(HibernateUtil.getProducts());
        categoryTable.setItems(HibernateUtil.getProductCategories());
        store.update();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            update();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        addProductImages();
        buildTables();

        logoutBtn.setGraphic(new ImageView("images/logout.png"));
        logoutBtn.setTooltip(new Tooltip("Logout"));
        logoutBtn.getTooltip().setShowDelay(Duration.millis(700));
        logoutBtn.setOnAction((ActionEvent e) -> APP.logout());

        // Add action handling for the add member button
        productAdd.setId("Product:Add");
        productAdd.setOnAction(this::openObjectEditAdd);

        categoryAdd.setId("Category:Add");
        categoryAdd.setOnAction(this::openObjectEditAdd);

        // Add action handling for the add member button
        memberAdd.setId("Member:Add");
        memberAdd.setOnAction(this::openObjectEditAdd);
    }
}
