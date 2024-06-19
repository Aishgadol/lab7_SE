package org.example;

import java.util.List;
import java.util.Random;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class App
{
    private static Session session;

    private static SessionFactory getSessionFactory() throws HibernateException{
        Configuration configuration=new Configuration();
        // Add ALL of your entities here. You can also try adding a whole package.
        configuration.addAnnotatedClass(Car.class);

        ServiceRegistry serviceRegistry=new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

    public static void generateCars() throws Exception {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            Car car = new Car("MOO-" + random.nextInt(), 100000, 2000 + random.nextInt(19));
            session.save(car);
            /*
                 * The call to session.flush() updates the DB immediately
                without ending the transaction.
                 * Recommended to do after an arbitrary unit of work.
                 * MANDATORY to do if you are saving a large amount of data -
                otherwise you may get cache errors.
                 */
            session.flush();
        }
    }

    public static List<Car> getAllCars() throws Exception{
        CriteriaBuilder builder=session.getCriteriaBuilder();
        CriteriaQuery<Car> query=builder.createQuery(Car.class);
        query.from(Car.class);
        List<Car> data=session.createQuery(query).getResultList();
        return data;
    }
    public static void main( String[] args )
    {
        System.out.println( "\n\nHello World!\n\n" );
    }
}
