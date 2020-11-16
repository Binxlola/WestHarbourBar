package BarApp;

import BarApp.Profile.ProfileController;
import BarApp.Settings.SettingsController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class NavHandler implements EventHandler<ActionEvent> {

    private final Main _Main = Main.getMain();
    private final MainController navParent;

    public NavHandler(MainController parent) {
        this.navParent = parent;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        boolean nodeChanged = false;
        Node node;

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
