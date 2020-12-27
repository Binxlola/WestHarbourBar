package main.java;

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
import javafx.util.Duration;
import main.java.Settings.SettingsController;
import main.java.Store.StoreController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends AnchorPane implements Initializable, RootController {

    private String ID;
    @FXML private Button menu;
    @FXML private GridPane navList;
    private Node primaryNode;

    // Transition Variables
    private TranslateTransition openNav;
    private TranslateTransition closeNav;

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

    @Override
    public void calcNavDimensions(Number rootWidth, Number rootHeight) {
        navList.setPrefWidth(rootWidth.doubleValue());
        navList.setTranslateX((-1 * rootWidth.doubleValue()));
    }

    @Override
    public void prepareMenuAnimation() {
        openNav = new TranslateTransition(new Duration(350), navList);
        closeNav = new TranslateTransition(new Duration(350), navList);
        openNav.setToX(0);
        menu.setOnAction(this::openCloseNav);
    }

    @Override
    public void openCloseNav(ActionEvent e) {
        if(navList.getTranslateX()!=0){
            openNav.play();
        }else{
            closeNav.setToX(-(navList.getWidth()));
            closeNav.play();
        }
    }

    @Override
    public void initNavHandlers() {
        NavHandler handler = new NavHandler(this);
        for(Node node: navList.getChildren()) {
            if(node instanceof Button) {
                ((Button) node).setOnAction(handler);
            }
        }
    }

    @Override
    public boolean changeScreen(Node newNode) {
        try {
            if(newNode != null) {
                ObservableList<Node> children = this.getChildren();
                children.remove(this.getPrimaryNode());
                this.setPrimaryNode(newNode);
                children.add(newNode);
                newNode.toBack();
            } else {
                this.primaryNode = new StoreController();
                getChildren().add(primaryNode);
                primaryNode.toBack();
            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public Node getPrimaryNode() {return this.primaryNode;}
    @Override
    public void setPrimaryNode(Node primaryNode) {this.primaryNode = primaryNode;}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menu.setGraphic(new ImageView("resources/menu.png"));
        navList.toFront(); // Bring forward so it does not push other content
        menu.toFront(); // Bring button forward so it is always clickable
        prepareMenuAnimation();
        initNavHandlers();
        changeScreen(null);

        // Add a listener for window resize, will resize the nav menu
        ChangeListener<Number> shopListener = (observable, oldValue, newValue) -> this.calcNavDimensions(this.getWidth(), this.getHeight());

        this.widthProperty().addListener(shopListener);

    }
}
