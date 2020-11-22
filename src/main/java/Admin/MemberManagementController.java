package main.java.Admin;

import javafx.event.EventHandler;
import javafx.util.Callback;
import main.java.HibernateUtil;
import main.java.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.Member;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MemberManagementController extends AnchorPane implements Initializable {

    @FXML private TableView<Member> membersTable;
    @FXML Button memberAdd;
    private final Main _Main = Main.getMain();

    public MemberManagementController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MemberManagement.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleMembers(ActionEvent actionEvent) {

        Object source = actionEvent.getSource();
        if(source.equals(memberAdd)) {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(new MemberAddController(stage, this)));
            stage.show();
        }
    }

    public void openMemberEdit(ActionEvent e) {
        System.out.println(e);
    }

    private Callback<TableColumn<Member, Void>, TableCell<Member, Void>> buttonFactory(EventHandler<ActionEvent> btnHandler, String name, TableView<Member> tableView) {
        return new Callback<>() {
            @Override
            public TableCell<Member, Void> call(final TableColumn<Member, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button(name);

                    {
                        btn.setOnAction(btnHandler);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
    }

    private void addTableButtons() {
        TableColumn<Member, Void> editBtn = new TableColumn<>("");
        TableColumn<Member, Void> removeBtn = new TableColumn<>("");

        editBtn.setCellFactory(buttonFactory(this::openMemberEdit, "edit", this.membersTable));
        removeBtn.setCellFactory(buttonFactory(this::openMemberEdit, "remove", this.membersTable));

        membersTable.getColumns().add(editBtn);
        membersTable.getColumns().add(removeBtn);
    }

    public void update() {
        membersTable.setItems(HibernateUtil.getMembers());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        membersTable.setItems(HibernateUtil.getMembers());
        addTableButtons();

        memberAdd.setOnAction(this::handleMembers);
    }
}
