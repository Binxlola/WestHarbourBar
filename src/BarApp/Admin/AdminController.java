package BarApp.Admin;

import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController extends AnchorPane implements Initializable {

    @FXML
    private Button menu;
    @FXML private GridPane navList;
    @FXML private StackPane paneStack;

    // Transition Variables
    TranslateTransition openNav;
    TranslateTransition closeNav;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.menu.setGraphic(new ImageView("BarApp/menu.png"));
        this.navList.toFront(); // Bring forward so it does not push other content
        this.menu.toFront(); // Bring button forward so it is always clickable
        this.prepareSlideMenuAnimation();
        this.initNavHandlers();
        this.paneStack.getChildren().add(new MemberManagementController());

        // Create and set a listener for parent resizing
        ChangeListener<Number> resizeListener = (observable, oldValue, newValue) -> this.configSizing();
        this.widthProperty().addListener(resizeListener);

        configSizing();

    }

    private void configSizing() {
        double parentWidth = this.getWidth();

        // Make sure the stack pane is always at 100% height and width
        paneStack.prefHeightProperty().bind(this.prefHeightProperty());
        paneStack.prefWidthProperty().bind(this.prefWidthProperty());

        this.calcNavDimensions(parentWidth);
    }

    private void prepareSlideMenuAnimation() {
        openNav = new TranslateTransition(new Duration(350), navList);
        closeNav = new TranslateTransition(new Duration(350), navList);
        openNav.setToX(0);

        menu.setOnAction(this::openCloseNav);
    }

    /**
     * Event handler for the navigation opening and closing logic
     * @param e The event (Not currently required)
     */
    protected void openCloseNav(ActionEvent e) {
        if(navList.getTranslateX()!=0){
            openNav.play();
        }else{
            closeNav.setToX(-(navList.getWidth()));
            closeNav.play();
        }
    }

    private void calcNavDimensions(Number rootWidth) {
        navList.setPrefWidth(rootWidth.doubleValue());
        navList.setTranslateX((-1 * rootWidth.doubleValue()));
    }

    private void initNavHandlers() {
        AdminNavHandler handler = new AdminNavHandler(this);
        for(Node node: navList.getChildren()) {
            if(node instanceof Button) {
                ((Button) node).setOnAction(handler);
            }
        }
    }

    public boolean setPrimaryNode(Pane newNode) {
        try {
            ObservableList<Node> children = this.getChildren();
            children.add(newNode);
            newNode.toBack();
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
