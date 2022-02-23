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
    requires github.api;
    requires com.fasterxml.classmate;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires org.apache.commons.lang3;
    requires org.apache.commons.io;

    // Main parent packages for the module
    opens main.java.com.app to javafx.fxml;
    opens main.java.com.app.admin to javafx.fxml;
    opens main.java.com.app.user to javafx.fxml;

    // Secondary Packages
    opens main.java.com.app.login to javafx.fxml;
    opens main.java.com.app.settings to javafx.fxml;
    opens main.java.com.app.entities to org.hibernate.orm.core, javafx.base;
    opens main.java.com.app.util to org.hibernate.orm.core;

    // Shared packages
    opens main.java.com.app.sharedComponents.store to javafx.fxml;
    opens main.java.com.app.sharedComponents.transactions to javafx.fxml;

    exports main.java.com.app;
}