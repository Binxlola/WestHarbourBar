package main.java;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;

import javafx.event.ActionEvent;

public interface RootController {

    Node getPrimaryNode();
    void setPrimaryNode(Node primaryNode);
    void openCloseNav(ActionEvent e);
    void prepareMenuAnimation();
    void initNavHandlers();
    void calcNavDimensions(Number rootWidth, Number rootHeight);
    boolean changeScreen(Node newScreen);
}
