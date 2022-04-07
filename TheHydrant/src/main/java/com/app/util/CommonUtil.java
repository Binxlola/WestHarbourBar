package com.app.util;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Optional;

public class CommonUtil {

    /**
     * Will build the view from a xml file and set it to the corresponding controller
     *
     * @param controller The controller for the FXML view
     * @param FXMLName   The name of the FXML file containing the view information
     */
    public static void buildView(Object controller, String FXMLName) {
        FXMLLoader fxmlLoader = new FXMLLoader(
                main.java.Main.class.getClassLoader().getResource(FXMLName)
        );
        fxmlLoader.setRoot(controller);
        fxmlLoader.setController(controller);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}
