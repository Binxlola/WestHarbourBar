package BarApp;

import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends AnchorPane implements Initializable {

    private String ID;
    @FXML private Button menu;
    @FXML private GridPane navList;
    private Node primaryNode;

    // Transition Variables
    TranslateTransition openNav;
    TranslateTransition closeNav;

    public MainController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setID(String ID) {this.ID = ID;}

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.menu.setGraphic(new ImageView("BarApp/menu.png"));
        this.navList.toFront(); // Bring forward so it does not push other content
        this.menu.toFront(); // Bring button forward so it is always clickable
        this.prepareSlideMenuAnimation();
        this.initNavHandlers();

        // Add a listener for window resize, will resize the nav menu
        ChangeListener<Number> shopListener = (observable, oldValue, newValue) -> this.calcNavDimensions(this.getWidth(), this.getHeight());

        this.widthProperty().addListener(shopListener);

    }

    private void calcNavDimensions(Number rootWidth, Number rootHeight) {
        navList.setPrefWidth(rootWidth.doubleValue());
        navList.setTranslateX((-1 * rootWidth.doubleValue()));
    }

    private void initNavHandlers() {
        NavHandler handler = new NavHandler(this);
        for(Node node: navList.getChildren()) {
            if(node instanceof Button) {
               ((Button) node).setOnAction(handler);
            }
        }
    }

    public boolean setPrimaryNode(Node newNode) {
        try {
            ObservableList<Node> children = this.getChildren();
            children.remove(this.primaryNode);
            this.primaryNode = newNode;
            children.add(newNode);
            newNode.toBack();
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
