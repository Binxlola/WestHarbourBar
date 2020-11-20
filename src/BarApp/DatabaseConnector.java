package BarApp;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnector {

    final static String CREATE = "create table";
    final static String LINE_SEPARATOR = System.getProperty("line.separator");
    final static String SELECT_ALL = "select * from";

    public static void buildDefaultTables(Connection conn) throws SQLException, IOException {
        String url = "jdbc:h2:/" + System.getProperty("user.dir") + "/TheHydrant";
        Connection con = DriverManager.getConnection(url + ";IFEXISTS=TRUE", "Admin", "Admin");
        String scriptFilePath = "D:\\Dev\\WestHarbourBar\\exportScript.sql";
        BufferedReader reader = new BufferedReader(new FileReader(scriptFilePath));

        String query = reader.readLine();
        while (query != null) {

            con.createStatement().execute(query);
            query = reader.readLine();
        }

    }

    /**
     * Builds the sql query for creating the default users table and then runs the query to create the table in the
     * database.
     * @param conn The connection to the database
     * @throws SQLException An exception thrown if an error occurs turing the execution of sql statement
     */
    private static void createUsersTable(Connection conn) throws SQLException {
        String query = CREATE + " users" + "(" + LINE_SEPARATOR +
                " " + getSizableField("id", "int", 6, true) + "," +
                LINE_SEPARATOR +
                " " + getSizableField("firstName", "varchar", 20, false) + "," +
                LINE_SEPARATOR +
                getSizableField("lastName", "varchar", 20, false) + "," +
                LINE_SEPARATOR +
                getSizableField("email", "varchar", 320, false) + "," +
                LINE_SEPARATOR +
                getSizableField("mobile", "int", 10, false) +
                LINE_SEPARATOR + " );";
        conn.createStatement().execute(query);
    }


    public static ResultSet getAllMembers(Connection conn) {
        // TODO add error logging
        try {
         return conn.createStatement().executeQuery(SELECT_ALL + " users");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Creates a query string for creation of a sizable type field.
     * Example: name varchar(20) primary key
     * @param fieldName The name to be set as field name
     * @param fieldType The type to be set as field type
     * @param size Size to be set as size of sizable field
     * @param isPrimary Boolean specifying if field must be set as primary key
     * @return The SQL query string
     */
    private static String getSizableField(String fieldName, String fieldType, int size, boolean isPrimary) {
        return String.format("%s %s(%s)%s", fieldName, fieldType, size, isPrimary ? " primary key" : "");
    }

    public static void storeData(Object data) {
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();

        SessionFactory factory = meta.getSessionFactoryBuilder().build();
        Session session = factory.openSession();
        Transaction t = session.beginTransaction();

        session.save(data);
        t.commit();
        factory.close();
        session.close();
    }
}
