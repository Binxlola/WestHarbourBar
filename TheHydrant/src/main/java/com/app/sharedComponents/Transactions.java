package main.java.com.app.sharedComponents;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import main.java.com.app.entities.BalanceModify;
import main.java.com.app.entities.Member;
import main.java.com.app.entities.Purchase;
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
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        extendTable();
        update();
    }

    public void update() {
        setItems(user.getTransactions());
        refresh();
    }

    /**
     * Extends the table with columns that require further logic from creating a column that just reads a get method.
     * An example would be a cell with coloured text
     */
    private void extendTable() {
        // Add the value column
        TableColumn<Transaction, String> value = new TableColumn<>("Value");
        value.setCellFactory(colourCellCallback(false));
        getColumns().add(value);

        // Add the balance after column
        TableColumn<Transaction, String> balanceAfter = new TableColumn<>("Balance After");
        balanceAfter.setCellFactory(colourCellCallback(true));
        getColumns().add(balanceAfter);
    }

    /**
     * A callback method that will return a cell that has it's text coloured. Will be called after the cell is initially
     * initialized.
     * @param isBalanceAfter A flag to identify is the current cell is a balance after cell
     * @return TableCell<Transaction, String> The cell that will have colored text
     */
    private Callback<TableColumn<Transaction, String>, TableCell<Transaction, String>> colourCellCallback(boolean isBalanceAfter) {
        return param -> (TableCell<Transaction, String>) createColouredCell(isBalanceAfter);
    }

    /**
     * Changes the colour of cell text based on a negative or positive value. If current transaction is an instance of a
     * purchase, only the balance after will be coloured. If the transaction is an instance of a balance modify then both
     * balance after and update amount will be coloured.
     * NOTE: Only values that are not 0 will have their colour changed
     * @param isBalanceAfter A flag to identify is the current cell is a balance after cell
     * @return Coloured table cell
     */
    private TableCell<?, String> createColouredCell(boolean isBalanceAfter) {
        return new TableCell<>() {

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!isEmpty()) {
                    Transaction transaction = (Transaction) getTableView().getItems().get(getIndex());

                    float value = isBalanceAfter ? transaction.getBalanceAfter() : transaction.getTransactionValue();
                    if(value != 0) {
                        if(transaction instanceof Purchase && isBalanceAfter) {
                            setTextFill(value > 0 ? Color.GREEN : Color.RED);
                        } else if (transaction instanceof BalanceModify) {
                            setTextFill(value > 0 ? Color.GREEN : Color.RED);
                        }
                    }

                    this.setText(String.valueOf(value));

                }
            }
        };
    }
}
