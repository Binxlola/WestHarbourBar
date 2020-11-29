package main.java.Admin;

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
import javafx.util.Duration;
import main.java.NavHandler;
import main.java.RootController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController extends AnchorPane implements Initializable, RootController {

    @FXML
    private Button menu;
    @FXML private GridPane navList;
    private Node primaryNode;

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

                // Remove old screen
                ObservableList<Node> children = this.getChildren();
                children.remove(this.getPrimaryNode());

                // Add new screen
                setPrimaryNode(newNode);
                children.add(newNode);
                newNode.toBack();

                // Fit new screen to parent
                setPrimaryNodeSize(this.getWidth(), this.getHeight());
            } else {
                this.primaryNode = new MemberManagementController();
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
        this.menu.setGraphic(new ImageView("resources/menu.png"));
        this.navList.toFront(); // Bring forward so it does not push other content
        this.menu.toFront(); // Bring button forward so it is always clickable
        this.prepareMenuAnimation();
        this.initNavHandlers();
        this.changeScreen(null);

        // Create and set a listener for parent resizing
        ChangeListener<Number> resizeListener = (observable, oldValue, newValue) -> this.configSizing();
        this.widthProperty().addListener(resizeListener);
        this.heightProperty().addListener(resizeListener);

        configSizing();

    }

    private void configSizing() {
        double parentWidth = this.getWidth();
        double parentHeight = this.getHeight();

        calcNavDimensions(parentWidth, parentHeight);
        setPrimaryNodeSize(parentWidth, parentHeight);
    }

    private void setPrimaryNodeSize(double width, double height) {
        ((Pane)primaryNode).setPrefWidth(width);
        ((Pane)primaryNode).setPrefHeight(height);
    }
}
