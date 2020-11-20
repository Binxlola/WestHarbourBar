package BarApp;

import BarApp.Login.LoginController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.HibernateException;
import org.hibernate.Metamodel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

import javax.persistence.metamodel.EntityType;
import java.io.File;
import java.sql.Connection;
import java.util.EnumSet;

public class Main extends Application {

    private static final SessionFactory ourSessionFactory;
    public static final String SCRIPT_FILE = "exportScript.sql";
    private Stage mainStage;
    public static Main _Main;
    private Scene login;
    private Connection conn;

    @Override
    public void start(Stage stage) throws Exception {
        // Set "GLOBAL" variables
        _Main = this;
        this.mainStage = stage;
        this.login = new Scene(new LoginController());

        mainStage.setTitle("The Hydrant");
        mainStage.setScene(login);
        mainStage.show();
    }

    public void setScene(Scene newScene) {
        mainStage.setScene(newScene);
        mainStage.show();
    }

    public void setConnection(Connection connection) {
        this.conn = connection;
    }

    public Connection getConnection() {
        return this.conn;
    }

    public static Main getMain() {
        return _Main;
    }

    public void logout() {
        mainStage.setScene(login);
        mainStage.show();
    }

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            ourSessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public static void createDataBase(SchemaExport export, Metadata metadata) {
        // TargetType.DATABASE - Execute on Databse
        // TargetType.SCRIPT - Write Script file.
        // TargetType.STDOUT - Write log to Console.

        EnumSet<TargetType> targetTypes = EnumSet.of(TargetType.DATABASE, TargetType.SCRIPT, TargetType.STDOUT);

        SchemaExport.Action action = SchemaExport.Action.CREATE;
        //
        export.execute(targetTypes, action, metadata);

        System.out.println("Export OK");

    }

    private static SchemaExport getSchemaExport() {

        SchemaExport export = new SchemaExport();
        // Script file.
        File outputFile = new File(SCRIPT_FILE);
        String outputFilePath = outputFile.getAbsolutePath();

        System.out.println("Export file: " + outputFilePath);

        export.setDelimiter(";");
        export.setOutputFile(outputFilePath);

        // No Stop if Error
        export.setHaltOnError(false);
        //
        return export;
    }

    public static void main(String[] args) throws Exception {
        final Session session = getSession();
        final String configFileName = "hibernate.cfg.xml";
        try {
            // Create the ServiceRegistry
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure(configFileName).build();

            // Create a metadata sources using the specified service registry.
            Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();

            SchemaExport export = getSchemaExport();

            System.out.println("Create Database...");
            // Create tables
            createDataBase(export, metadata);

            System.out.println("querying all the managed entities...");
            final Metamodel metamodel = session.getSessionFactory().getMetamodel();
            for (EntityType<?> entityType : metamodel.getEntities()) {
                final String entityName = entityType.getName();
                final Query query = session.createQuery("from " + entityName);
                System.out.println("executing: " + query.getQueryString());
                for (Object o : query.list()) {
                    System.out.println("  " + o);
                }
            }
        } finally {
            session.close();
        }

        DatabaseConnector.buildDefaultTables(null);
        launch(args);
    }
}
