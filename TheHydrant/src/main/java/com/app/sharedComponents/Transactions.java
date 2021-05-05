package main.java.com.app.sharedComponents;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import main.java.com.app.entities.BalanceModify;
import main.java.com.app.entities.Member;
import main.java.com.app.entities.Transaction;

import java.io.IOException;

public class Transactions extends TableView<Transaction> {
    private Member user;

    public Transactions() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Transactions.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Transactions(Member user) {
        this();
        this.user = user;
        build();
    }

    public void setUser(Member user) {this.user = user;}

    public void build() {
        extendTable();
        update();
    }

    public void update() {
        setItems(user.getTransactions());
        refresh();
    }

    private void extendTable() {
        TableColumn<Transaction, String> quantity = new TableColumn<>("Value");
        quantity.setCellFactory(balanceFactoryTransactions());
        getColumns().add(quantity);
    }

    private Callback<TableColumn<Transaction, String>, TableCell<Transaction, String>> balanceFactoryTransactions() {
        return param -> (TableCell<Transaction, String>) createColouredCell();
    }

    /**
     * Changes the colour of cell text based on a negative or positive value (currently used for Members and Products)
     *
     * @return Coloured table cell
     */
    private TableCell<?, String> createColouredCell() {
        return new TableCell<>() {

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!isEmpty()) {
                    Object object = getTableView().getItems().get(getIndex());

                    float value = ((Transaction) object).getTransactionValue();
                    if(object instanceof BalanceModify) {
                        if (value != 0) {
                            this.setTextFill(value > 0 ? Color.GREEN : Color.RED);
                        }
                    }

                    this.setText(String.valueOf(value));

                }
            }
        };
    }

//    @FXML
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        buildTable();
//    }
}
