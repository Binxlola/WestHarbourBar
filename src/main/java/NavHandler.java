package main.java;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import main.java.Admin.MemberManagementController;
import main.java.Admin.ProductManagementController;
import main.java.Profile.ProfileController;
import main.java.Settings.SettingsController;
import main.java.Store.StoreController;

public class NavHandler implements EventHandler<ActionEvent> {

    private final Main _Main = Main.getInstance();
    private final RootController navParent;

    public NavHandler(RootController parent) {
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
                nodeChanged = navParent.changeScreen(node);
            }
            case "settingsBtn" -> {
                node = new SettingsController();
                nodeChanged = navParent.changeScreen(node);
            }
            case "membersBtn" -> {
                node = new MemberManagementController();
                nodeChanged = navParent.changeScreen(node);
            }
            case "productsBtn" -> {
                node = new ProductManagementController();
                nodeChanged = navParent.changeScreen(node);
            }
            case "storeBtn" -> {
                node = new StoreController();
                nodeChanged = navParent.changeScreen(node);
            }
            case "logoutBtn" -> {
                _Main.logout();
            }
        }

        if(nodeChanged) {
            navParent.openCloseNav(actionEvent);}
    }
}
