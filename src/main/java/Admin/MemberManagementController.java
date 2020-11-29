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

    public void openMemberEdit(ActionEvent e) {
        System.out.println(e);
        Button source = (Button) e.getSource();
        source.getUserData();
    }

    public void handleMemberDel(ActionEvent e) {
        Member member = (Member) ((Button) e.getSource()).getUserData();
        HibernateUtil.saveOrRemove(member, false);
        this.update();
        System.out.println("removing member");
    }

    private Callback<TableColumn<Member, Void>, TableCell<Member, Void>> buttonFactory(EventHandler<ActionEvent> btnHandler, String name) {
        return new Callback<>() {
            @Override
            public TableCell<Member, Void> call(final TableColumn<Member, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button(name);

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            btn.setOnAction(btnHandler);
                            btn.setUserData(getTableView().getItems().get(getIndex()));
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
    }

    private void addTableButtons() {
        // Create 2 unnamed columns for the table
        TableColumn<Member, Void> editBtn = new TableColumn<>("");
        TableColumn<Member, Void> removeBtn = new TableColumn<>("");

        // Crete the cell factor for each column of buttons
        editBtn.setCellFactory(buttonFactory(this::openMemberEdit, "edit"));
        removeBtn.setCellFactory(buttonFactory(this::handleMemberDel, "remove"));

        // Add the new button columns to the table
        membersTable.getColumns().add(editBtn);
        membersTable.getColumns().add(removeBtn);
    }

    public void update() {
        membersTable.setItems(HibernateUtil.getMembers());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        update();
        addTableButtons();


        // Add action handling for the add member button
        memberAdd.setOnAction((ActionEvent e) -> {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(new MemberAddController(stage, this)));
            stage.show();
        });
    }
}
