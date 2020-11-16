package BarApp.Admin;

import BarApp.Main;
import BarApp.MainController;
import BarApp.Profile.ProfileController;
import BarApp.Settings.SettingsController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class AdminNavHandler implements EventHandler<ActionEvent> {

    private final Main _Main = Main.getMain();
    private final AdminController navParent;

    public AdminNavHandler(AdminController parent) {
        this.navParent = parent;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        boolean nodeChanged = false;
        Pane node;

        switch (button.getId()) {
            case "profileBtn" -> {
                node = new ProfileController();
                nodeChanged = navParent.setPrimaryNode(node);
            }
            case "settingsBtn" -> {
                node = new SettingsController();
                nodeChanged = navParent.setPrimaryNode(node);
            }
            case "logoutBtn" -> {
                _Main.logout();
            }
        }

        if(nodeChanged) {navParent.openCloseNav(actionEvent);}
    }
}

