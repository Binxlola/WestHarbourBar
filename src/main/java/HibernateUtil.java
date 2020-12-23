package main.java;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import javax.imageio.ImageIO;
import javax.persistence.Entity;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static javafx.collections.FXCollections.observableList;

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

    public static void update(Object o) {
        Transaction transaction = null;

        if(o.getClass().getAnnotation(Entity.class) != null) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                // start a transaction
                transaction = session.beginTransaction();
                session.update(o);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
    }

    public static void updateEntities(Object... entities) {
        for(Object entity : entities) {
            update(entity);
        }
    }

    /**
     * Uses a hibernate session to retrieve all members and creates an observableList of the result set.
     * @return Member ObservableList
     */
    public static ObservableList<Member> getMembers() {
        ObservableList<Member> results = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            results = observableList(session.createQuery("Select a from Member a", Member.class).getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    public static Member getMember(long id) {
        Member result = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query query = session.createQuery("Select a from Member a where a.Id=:id");
            query.setParameter("id", id);
            result = (Member) query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Uses a hibernate session to retrieve all products and creates an observableList of the result set.
     * @return Product ObservableList
     */
    public static ObservableList<Product> getProducts() {
        ObservableList<Product> results = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            results = observableList(session.createQuery("Select a from Product a", Product.class).getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    public static ObservableList<ProductCategory> getProductCategories() {
        ObservableList<ProductCategory> results = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            results = observableList(session.createQuery("select a from ProductCategory a", ProductCategory.class).getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;

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

    public static ImageView buildImage(byte[] imageData) {
        final ImageView image = new ImageView();

        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            image.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
        } catch(IOException e) {
            e.printStackTrace();
        }

        return image;

    }

    public static ImageView buildImageToSize(byte[] imageData, double width, double height) {
        final ImageView image = buildImage(imageData);
        image.setFitWidth(width);
        image.setFitHeight(height);

        return image;

    }
}
