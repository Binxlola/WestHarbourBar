package main.java;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.persistence.Entity;

public class HibernateUtil {

    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;
    private static final String configFileName = "resources/hibernate.cfg.xml";
    public static final String SCRIPT_FILE = "exportScript.sql";

    public static SessionFactory getSessionFactory() {

        if (sessionFactory == null) {
            try {
                // Create registry
                registry = new StandardServiceRegistryBuilder().configure(configFileName).build();

                // Create MetadataSources
                MetadataSources sources = new MetadataSources(registry);

                // Create Metadata
                Metadata metadata = sources.getMetadataBuilder().build();

                // Create SessionFactory
                sessionFactory = metadata.getSessionFactoryBuilder().build();

            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    /**
     * Basic utility method to allow an easy save or deletion of data through hibernate by getting the current session,
     * and committing a change through a transaction.
     * Will only attempt the commit if the object passed in is classed as an Entity
     * @param o The Object of data to be saved
     * @param isSave A flag representing if change is a save or a deletion
     */
    public static void saveOrRemove(Object o, boolean isSave) {
        Transaction transaction = null;

        if(o.getClass().getAnnotation(Entity.class) != null) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                // start a transaction
                transaction = session.beginTransaction();
                // save the student objects
                if (isSave) {
                    session.save(o);
                } else {
                    session.remove(o);
                }
                // commit transaction
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
    }

    /**
     * Uses a hibernate session to retrieve all rows from a table and creates an observableList of the result set.
     * Will only run query if entityClass is in fact an entity
     * @return Members ObservableList
     */
    public static <T> ObservableList<Member> getAllRows(String tableName, Class<T> entityClass) {
        ObservableList<T> results = null;

        if(entityClass.getAnnotation(Entity.class) != null) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                results = FXCollections.observableList(session.createQuery("Select a from " + tableName + " a", entityClass).getResultList());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return (ObservableList<Member>) results;
    }

    /**
     * Closes the open hibernate registry if there is one open.
     */
    public static void shutdown() {
        if (registry != null) {
            System.out.println("Shutting Down Hibernate");
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
