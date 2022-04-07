package com.app.admin;

import com.app.entities.Member;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.Duration;

public class MemberTableFactory implements Callback<TableColumn<Member, Object>, TableCell<Member, Object>> {

    private final EventHandler<ActionEvent> handler;
    private final String iconName, btnID, tooltipText;
    private final ColumnType type;

    public MemberTableFactory(ColumnType type) {
        this.handler = null;
        this.iconName = null;
        this.btnID = null;
        this.tooltipText = null;
        this.type = type;
    }

    public MemberTableFactory(EventHandler<ActionEvent> handler, String iconName, String btnID, String tooltipText, ColumnType type) {
        this.handler = handler;
        this.iconName = iconName;
        this.btnID = btnID;
        this.tooltipText = tooltipText;
        this.type = type;
    }

    @Override
    public TableCell<Member, Object> call(TableColumn<Member, Object> column) {
        switch (this.type) {
            case BUTTON -> {
                return createTableCellBtn(handler, iconName, btnID, tooltipText);
            }
            case CHECK_BOX -> {
                return createTableCheckBox();
            }
            case BALANCE -> {
                return createColouredCell();
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * Given and event handler and image file name will create a button to be used on a table
     *
     * @param btnHandler The event handler that is to be set on the button
     * @param iconName   The image name of the icon to be set on the button
     * @param btnID      The ID to be set on the created button
     * @return The table cell button
     */
    private TableCell<Member, Object> createTableCellBtn(EventHandler<ActionEvent> btnHandler, String iconName, String btnID, String tooltipText) {
        return new TableCell<>() {
            private final Button btn = new Button();

            @Override
            public void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    btn.setId(btnID);
                    btn.setGraphic(new ImageView(iconName));
                    btn.setOnAction(btnHandler);
                    btn.setUserData(getTableView().getItems().get(getIndex()));
                    btn.setTooltip(new Tooltip(tooltipText));
                    btn.getTooltip().setShowDelay(Duration.millis(700));
                    setGraphic(btn);
                }
            }
        };
    }

    /**
     * Changes the colour of cell text based on a negative or positive value (currently used for Members and Products)
     *
     * @return Coloured table cell
     */
    private TableCell<Member, Object> createColouredCell() {
        return new TableCell<>() {
            @Override
            public void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (!isEmpty()) {
                    Member object = getTableView().getItems().get(getIndex());
                    float value = object.getBalance();

                    this.setText(String.valueOf(value));
                    if (value != 0) {
                        this.setStyle(value > 0 ? "-fx-text-fill: green" : "-fx-text-fill: red");
                    } else {
                        this.setStyle("");
                    }
                }
            }
        };
    }

    /**
     * Creates a table cell that contains a check box
     *
     * @return The table cell containing the disabled checkbox
     */
    private TableCell<Member, Object> createTableCheckBox() {
        return new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            @Override
            public void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Member member = getTableView().getItems().get(getIndex());
                    checkBox.setSelected(member.isAdmin());
                    checkBox.setDisable(true);
                    setGraphic(checkBox);
                }
            }
        };
    }

    public enum ColumnType {
        BUTTON,
        CHECK_BOX,
        BALANCE
    }
}
