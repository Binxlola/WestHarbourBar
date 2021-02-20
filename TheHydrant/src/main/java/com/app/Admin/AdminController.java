package main.java.com.app.Admin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import main.java.com.app.*;
import main.java.com.app.entities.Member;
import main.java.com.app.entities.Product;
import main.java.com.app.entities.ProductCategory;
import main.java.com.app.util.HibernateUtil;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Admin.fxml"));
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

    public void openMemberEdit(ActionEvent e) {
        Member member = (Member) ((Button) e.getSource()).getUserData();

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(new MemberController(stage, this, member)));
        stage.show();
    }

    public void handleMemberDel(ActionEvent e) {
        Member member = (Member) ((Button) e.getSource()).getUserData();
        HibernateUtil.saveOrRemove(member, false);
        this.update();
        System.out.println("removing member");
    }

    private Callback<TableColumn<Member, Void>, TableCell<Member, Void>> buttonFactory(EventHandler<ActionEvent> btnHandler, ImageView icon) {
        return new Callback<>() {
            @Override
            public TableCell<Member, Void> call(final TableColumn<Member, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button();

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            btn.setGraphic(icon);
                            btn.setOnAction(btnHandler);
                            btn.setUserData(getTableView().getItems().get(getIndex()));
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
    }

    private void addTableButtons() {
        // Create 2 unnamed columns for the table
        TableColumn<Member, Void> editBtn = new TableColumn<>("");
        TableColumn<Member, Void> removeBtn = new TableColumn<>("");

        // Crete the cell factor for each column of buttons
        editBtn.setCellFactory(buttonFactory(this::openMemberEdit, new ImageView("edit.png")));
        removeBtn.setCellFactory(buttonFactory(this::handleMemberDel, new ImageView("delete.png")));

        // Add the new button columns to the table
        membersTable.getColumns().add(editBtn);
        membersTable.getColumns().add(removeBtn);
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
        productAdd.setOnAction((ActionEvent e) -> {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(new ProductAddController(stage, this)));
            stage.show();
        });

        categoryAdd.setOnAction((ActionEvent e) -> {
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

        // Add action handling for the add member button
        memberAdd.setOnAction((ActionEvent e) -> {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(new MemberController(stage, this)));
            stage.show();
        });
    }

    public void update() {
        membersTable.setItems(HibernateUtil.getMembers());
        productTable.setItems(HibernateUtil.getProducts());
        categoryTable.setItems(HibernateUtil.getProductCategories());
    }
}