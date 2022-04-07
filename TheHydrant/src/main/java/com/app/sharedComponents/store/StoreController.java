package com.app.sharedComponents.store;

import com.app.App;
import com.app.entities.Member;
import com.app.entities.Product;
import com.app.entities.ProductCategory;
import com.app.entities.Purchase;
import com.app.util.CommonUtil;
import com.app.util.HibernateUtil;
import javafx.animation.PauseTransition;
import javafx.beans.NamedArg;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.util.Duration;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class StoreController extends AnchorPane implements Initializable {

    @FXML private HBox toolbar, userDetails;
    @FXML private GridPane productsContainer;
    @FXML private ComboBox<ProductCategory> categoryFilter;
    @FXML private ScrollPane productsScroll;
    @FXML private ListView<Member> memberList;
    @FXML private Button logoutBtn;
    @FXML private Label userId, userBalance, userName;
    private final App APP = App.getInstance();
    private final Member member = APP.getUser();
    private final BooleanProperty isAdminMode = new SimpleBooleanProperty();

    public StoreController(@NamedArg("isAdminMode") boolean isAdminMode) {
        setId("storeController");
        this.isAdminMode.setValue(isAdminMode && member.isAdmin());
        CommonUtil.buildView(this, "fxml/Store.fxml");
    }

    /**
     * Builds the product cards and adds them to the store GridPane
     */
    private void buildItems() {
        ObservableList<? extends Product> productList = null;
        int row = 0;
        int col = 0;

        try {
            productList = isAdminMode.get()? HibernateUtil.getProducts() : HibernateUtil.getProducts(Product.ProductVisibility.PUBLIC);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        for (Product product : productList) {
            ProductCard card = new ProductCard(product, this::handleBuy);
            productsContainer.add(card, col, row);

            // Increment or reset
            if (col >= 2) {
                col = 0;
                row++;
            } else {
                col++;
            }
        }
    }

    /**
     * Handles a user purchasing an item from the store
     *
     * @param e The ActionEvent used to get the product data
     */
    private void handleBuy(ActionEvent e) {
        Button source = (Button) e.getSource();
        Product product = (Product) source.getUserData();
        Member user = APP.getUser();

        // If admin mode, a member needs to be selected and multiple members may be selected
        if (isAdminMode.get()) {
            ObservableList<Member> selectedMembers = memberList.getSelectionModel().getSelectedItems();
            if (!selectedMembers.isEmpty() && product.getQuantity() >= selectedMembers.size()) {
                selectedMembers.forEach(selectedMember -> {
                    this.persistPurchase(selectedMember, product);
                });
            } else {
                this.displayAlert(
                        selectedMembers.isEmpty() ?
                                "Please select at least one member for the purchase." :
                                "There is not enough " + product.getQuantity() + " for number of members selected."
                );
            }
        } else {
            this.persistPurchase(user, product);
        }

        // Purchase complete, repaint the window
        update();
    }

    /**
     * A helper method to display an alert box to the user with a given message.
     * @param message The message to be displayed
     */
    private void displayAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setContentText(message);
        alert.initOwner(APP.getCurrentScene().getWindow());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event -> {
            alert.setResult(ButtonType.CANCEL);
            alert.close();
        });
        delay.play();
    }

    /**
     * When a purchase is either admin or normal mode, this is where the handling of data for persistence takes place.
     * The updated member balance and product quantity are calculated, from there a purchase record is created.
     * Finally, all entities (Product, Member, Purchase) are saved or updated.
     * @param member The member for which the purchase is being made for
     * @param item The product which is being purchased
     */
    private void persistPurchase(Member member, Product item) {
        float userBalance = member.getBalance();
        float productCost = item.getCost();
        Purchase purchase;

        member.setBalance(userBalance - productCost);
        item.setQuantity(item.getQuantity() - 1);

        purchase = new Purchase(member, new Date(), item);
        member.addTransaction(purchase);

        HibernateUtil.saveOrRemove(true, purchase, member, item);
        displayAlert("Purchase Successful");

    }

    private void setupUserDetails() {
        userId.setText("ID: " + member.getId());
        userName.setText("Name: " + member.getFirstName() + " " + member.getLastName());
        setUserBalance();
    }

    private void setUserBalance() {
        float balance = member.getBalance();
        userBalance.setText("Balance: " + balance);
        if (balance != 0) {
            userBalance.setTextFill(balance > 0 ? Color.GREEN : Color.RED);
        }
    }



    public void update() {
        buildItems();
        setUserBalance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categoryFilter.setItems(HibernateUtil.getProductCategories());

        // Member list should only be displayed and functional when used in admin mode
        if (isAdminMode.get()) {
            memberList.prefHeightProperty().bind(this.heightProperty().subtract(toolbar.heightProperty()));
            memberList.maxHeightProperty().bind(this.heightProperty().subtract(toolbar.heightProperty()));
            memberList.setItems(HibernateUtil.getMembers());
            memberList.setManaged(true);

            userDetails.setManaged(false);
            userDetails.setVisible(false);
        } else {
            memberList.setManaged(false);
            userDetails.setManaged(true);
        }

        productsScroll.prefWidthProperty().bind(this.widthProperty().subtract(memberList.widthProperty()));
        productsScroll.maxWidthProperty().bind(this.widthProperty().subtract(memberList.widthProperty()));
        productsScroll.prefHeightProperty().bind(this.heightProperty().subtract(toolbar.heightProperty()));
        productsScroll.maxHeightProperty().bind(this.heightProperty().subtract(toolbar.heightProperty()));

        setupUserDetails();
        update();

        productsScroll.toFront();
    }
}
