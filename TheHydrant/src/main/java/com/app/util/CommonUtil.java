package main.java.com.app.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class CommonUtil {

    /**
     * Will build the view from an xml file and set it to the corresponding controller
     *
     * @param controller The controller for the FXML view
     * @param FXMLName   The name of the FXML file containing the view information
     */
    public static void buildView(Pane controller, String FXMLName) {
        FXMLLoader fxmlLoader = new FXMLLoader(controller.getClass().getResource(FXMLName));
        fxmlLoader.setRoot(controller);
        fxmlLoader.setController(controller);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
