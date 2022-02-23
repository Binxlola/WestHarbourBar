module Updater {

    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive java.naming;
    requires java.desktop;
    requires javafx.swing;
    requires transitive java.sql;
    requires java.xml;
    requires github.api;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires org.apache.commons.lang3;
    requires org.apache.commons.io;

    exports main.java.com.updater;
}