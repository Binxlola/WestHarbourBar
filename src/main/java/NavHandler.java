package main.java;

import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import main.java.Profile.ProfileController;
import main.java.Settings.SettingsController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class NavHandler implements EventHandler<ActionEvent> {

    private final Main _Main = Main.getMain();
    private final Pane navParent;

    public NavHandler(Pane parent) {
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
                nodeChanged = ((RootController)navParent).changeScreen(node);
            }
            case "settingsBtn" -> {
                node = new SettingsController();
                nodeChanged = ((RootController)navParent).changeScreen(node);
            }
            case "logoutBtn" -> {
                _Main.logout();
            }
        }

        if(nodeChanged) {((RootController)navParent).openCloseNav(actionEvent);}
    }
}
