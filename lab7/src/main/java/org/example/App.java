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
    private static SessionFactory sessionFactory;

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

    private static void printAllCars() throws Exception{
        List<Car> cars=getAllCars();
        for(Car car:cars){
            System.out.print("Id: ");
            System.out.print(car.getId());
            System.out.print(", License plate: ");
            System.out.print(car.getLicensePlate());
            System.out.print(", Price: ");
            System.out.print(car.getPrice());
            System.out.print(", Year: ");
            System.out.print(car.getYear());
            System.out.print('\n');
        }
    }

    public static void main( String[] args )
    {
        try {
            System.out.println("we are here");
            sessionFactory = getSessionFactory();
            System.out.println("we have session factory");
            session = sessionFactory.openSession();
            System.out.println("we have session");
            session.beginTransaction();
            System.out.println("begun transaction");

            generateCars();

            printAllCars();

            session.getTransaction().commit();//save everything
        }
        catch(Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("Error occured, changes have been rooled back");
            e.printStackTrace();
        }finally {
            session.close();
        }
    }
}
