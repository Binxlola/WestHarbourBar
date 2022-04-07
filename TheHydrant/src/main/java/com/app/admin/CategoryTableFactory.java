package com.app.admin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.Duration;
import com.app.entities.ProductCategory;

public class CategoryTableFactory implements Callback<TableColumn<ProductCategory, Object>, TableCell<ProductCategory, Object>> {

    private final EventHandler<ActionEvent> handler;
    private final String btnID, tooltipText;
    private final ColumnType type;

    public CategoryTableFactory(EventHandler<ActionEvent> handler, String btnID, String tooltipText, ColumnType type) {
        this.handler = handler;
        this.btnID = btnID;
        this.tooltipText = tooltipText;
        this.type = type;
    }

    @Override
    public TableCell<ProductCategory, Object> call(TableColumn<ProductCategory, Object> column) {
        if (this.type == ColumnType.BUTTON) {
            return createTableCellBtn(handler, "images/delete.png", btnID, tooltipText);
        }
        return null;
    }

    /**
     * Given and event handler and image file name will create a button to be used on a table
     *
     * @param btnHandler The event handler that is to be set on the button
     * @param iconName   The image name of the icon to be set on the button
     * @param btnID      The ID to be set on the created button
     * @return The table cell button
     */
    private TableCell<ProductCategory, Object> createTableCellBtn(EventHandler<ActionEvent> btnHandler, String iconName, String btnID, String tooltipText) {
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

    public enum ColumnType {
        BUTTON,
        BALANCE
    }
}
