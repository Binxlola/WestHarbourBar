package com.app.sharedComponents.store;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import com.app.entities.Member;

public final class MemberCellFactory implements Callback<ListView<Member>, ListCell<Member>> {

    @Override
    public ListCell<Member> call(ListView<Member> cell) {
        return new ListCell<>() {
            @Override
            protected void updateItem(Member item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getFirstName() + " " + item.getLastName());
                    setUserData(item);
                }
            }
        };
    }
}
