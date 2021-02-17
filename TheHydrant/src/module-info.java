module TheHydrant {

    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive java.naming;
    requires org.hibernate.orm.core;
    requires java.desktop;
    requires java.persistence;
    requires javafx.swing;

    opens main.java.com.app to javafx.fxml;
    opens main.java.com.app.Admin to javafx.fxml;
    opens main.java.com.app.Login to javafx.fxml;
    opens main.java.com.app.Settings to javafx.fxml;

    exports main.java.com.app;
}