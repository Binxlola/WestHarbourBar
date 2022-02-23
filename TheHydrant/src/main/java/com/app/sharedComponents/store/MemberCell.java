package main.java.com.app.sharedComponents.store;

import javafx.scene.control.ListCell;
import main.java.com.app.entities.Member;

public class MemberCell extends ListCell<Member> {

    public MemberCell() {}

    @Override
    protected void updateItem(Member item, boolean empty) {
        super.updateItem(item, empty);

        if(empty || item == null) {
            setText(null);
        }
        else {
            setText(item.getFirstName() + " " + item.getLastName());
            setUserData(item);
        }
    }

}
