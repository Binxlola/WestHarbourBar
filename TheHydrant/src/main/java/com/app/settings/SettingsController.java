package com.app.settings;

import com.app.util.CommonUtil;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController extends GridPane implements Initializable {

    public SettingsController() {
        CommonUtil.buildView(this, "fxml/Settings.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
