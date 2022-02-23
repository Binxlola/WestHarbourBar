package main.java.com.app.sharedComponents.store;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import main.java.com.app.entities.Member;

public class MemberCellFactory implements Callback<ListView<Member>, ListCell<Member>> {

    @Override
    public ListCell<Member> call(ListView<Member> cell) {
        return new MemberCell();
    }
}
