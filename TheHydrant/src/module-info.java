module TheHydrant {

    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive java.naming;
    requires org.hibernate.orm.core;
    requires org.hibernate.commons.annotations;
    requires java.desktop;
    requires java.persistence;
    requires javafx.swing;
    requires transitive java.sql;
    requires net.bytebuddy;
    requires java.xml.bind;
    requires java.xml;
    requires com.sun.xml.bind;
    requires com.fasterxml.classmate;

    opens main.java.com.app to javafx.fxml;
    opens main.java.com.app.Admin to javafx.fxml;
    opens main.java.com.app.Login to javafx.fxml;
    opens main.java.com.app.Settings to javafx.fxml;
    opens main.java.com.app.Store to javafx.fxml;
    opens main.java.com.app.entities to org.hibernate.orm.core, javafx.base;
    opens main.java.com.app.util to org.hibernate.orm.core;

    exports main.java.com.app;
}