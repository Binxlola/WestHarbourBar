package com.app.admin;

import com.app.entities.Product;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.Duration;

public class ProductTableFactory implements Callback<TableColumn<Product, Object>, TableCell<Product, Object>> {

    private final EventHandler<ActionEvent> handler;
    private final String iconName, btnID, tooltipText;
    private final ColumnType type;

    public ProductTableFactory(ColumnType type) {
        this.handler = null;
        this.iconName = null;
        this.btnID = null;
        this.tooltipText = null;
        this.type = type;
    }

    public ProductTableFactory(EventHandler<ActionEvent> handler, String iconName, String btnID, String tooltipText, ColumnType type) {
        this.handler = handler;
        this.iconName = iconName;
        this.btnID = btnID;
        this.tooltipText = tooltipText;
        this.type = type;
    }

    @Override
    public TableCell<Product, Object> call(TableColumn<Product, Object> column) {
        switch (this.type) {
            case BUTTON -> {
                return createTableCellBtn(handler, iconName, btnID, tooltipText);
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
    private TableCell<Product, Object> createTableCellBtn(EventHandler<ActionEvent> btnHandler, String iconName, String btnID, String tooltipText) {
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
    private TableCell<Product, Object> createColouredCell() {
        return new TableCell<>() {
            @Override
            public void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (!isEmpty()) {
                    Product object = getTableView().getItems().get(getIndex());
                    float value = object.getQuantity();

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

    public enum ColumnType {
        BUTTON,
        BALANCE
    }
}
