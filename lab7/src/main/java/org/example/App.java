package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

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
        Scanner s=new Scanner(System.in);
        // Add ALL of your entities here. You can also try adding a whole package.
        configuration.addAnnotatedClass(Car.class);
        configuration.addAnnotatedClass(Person.class);
        configuration.addAnnotatedClass(Garage.class);
        configuration.setProperty("hibernate.connection.password","159753a");
        ServiceRegistry serviceRegistry=new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

    public static void generateCars() throws Exception {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            Car car = new Car("MOO-" + random.nextInt(), 90000+random.nextInt(100000), 2000 + random.nextInt(19));
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

    public static void generatePeople() throws Exception{
        Random rnd=new Random();
        List<Car> cars=getAllCars();

        for(int i=0;i<100 ;i++){
            Person p=new Person("Chadwick "+rnd.nextInt(100),
                    "Boseman "+rnd.nextInt(100),
                    "p"+rnd.nextInt(1000)+""+rnd.nextInt(1000),
                    "chadwick"+rnd.nextInt(1000)+"@gmail.com");
            List<Car> myCar=new ArrayList<>();
            myCar.add(cars.get(i));
            p.setCars(myCar);
            cars.get(i).setOwner(p);
            session.save(p);
        }
        session.flush();
    }

    public static void generateGarages() throws Exception{
        Random rnd=new Random();
        List<Car> cars=getAllCars();
        List<Person> people=getAllPeople();
        for(int i=0;i<4;i++){
            String num="";
            //make phone number
            for(int j=0;j<7;j++){
                num=num+rnd.nextInt(10);
            }
           Garage g=new Garage("Haifa University, branch no: "+(i+1), "05"+rnd.nextInt(10)+"-"+num);
           List<Car> myCar=new ArrayList<>();
           List<Person> myp=new ArrayList<>();
           //splitting cars into garages , arbitrary
           for(int j = 25*i ; j < (25*(i+1)) ; j++){
               myCar.add(cars.get(j));
           }
           for(int j=0;j<20;j++){
               myCar.add(cars.get((j+rnd.nextInt(50000))%cars.size()));
           }
           for(int j=0;j<5;j++){
               myp.add(people.get(rnd.nextInt(people.size())));
           }
           g.setCars(myCar);
           g.setOwners(myp);
           for(Car c:myCar){
               List<Garage> myg=c.getGarages();
               myg.add(g);
               c.setGarages(myg);
           }
           session.save(g);
        }
        session.flush();
    }

    public static List<Garage> getAllGarages() throws Exception{
        CriteriaBuilder builder=session.getCriteriaBuilder();
        CriteriaQuery<Garage> query=builder.createQuery(Garage.class);
        query.from(Garage.class);
        List<Garage> data=session.createQuery(query).getResultList();
        return data;
    }

    public static List<Person> getAllPeople() throws Exception{
        CriteriaBuilder builder=session.getCriteriaBuilder();
        CriteriaQuery<Person> query=builder.createQuery(Person.class);
        query.from(Person.class);
        List<Person> data=session.createQuery(query).getResultList();
        return data;
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
    private static void printGarageDetails() throws Exception{
        List<Garage> glist=getAllGarages();
        for(Garage g:glist){
            System.out.println("Garage Address: " + g.getAddress());
            System.out.println("Garage Phone Number: " + g.getPhoneNumber());
            System.out.println("Cars in this Garage:");
            for (Car c : g.getCars()) {
                System.out.println("  License Plate: " + c.getLicensePlate());
            }
            System.out.println();
        }
    }

    private static void printCarDetails() throws Exception{
        List<Car> clist=getAllCars();
        for(Car c : clist){
            System.out.println("Car License Plate: " + c.getLicensePlate());
            System.out.println("Car Price: " + c.getPrice());
            System.out.println("Car Year: " + c.getYear());
            System.out.println("Owner Details:");
            if(c.getOwner()!=null){
                Person owner=c.getOwner();
                System.out.println("  Owner First Name: " + owner.getFirstName());
                System.out.println("  Owner Last Name: " + owner.getLastName());
                System.out.println("  Owner Email: " + owner.getEmailAddress());
            }
            System.out.println("Garages who handle this car: ");
            for(Garage g:c.getGarages()){
                System.out.println("Garage address: "+g.getAddress());
            }
            System.out.println();
        }
    }

    public static void doSomething() {
        Scanner s = new Scanner(System.in);
        String firstName = "";
        while (!firstName.equals("james")) {
            try {
                session.beginTransaction();
                Person p = session.get(Person.class, 1);
                System.out.println("type james if you want to stop lol:   ");
                String f = s.nextLine();
                firstName = f;
                p.setFirstName(f);
                session.update(p);
                session.flush();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session != null) {
                    session.getTransaction().rollback();
                }
                e.printStackTrace();
            }
        }
    }


    public static void main( String[] args )
    {
        try {
            sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            generateCars();
            generatePeople();
            generateGarages();

            System.out.println("Garage details & licence plates associated with each garage: ");
            printGarageDetails();
            System.out.println("Car details, owner details and addresses of garages that handle car: ");
            printCarDetails();

            session.flush();

            session.getTransaction().commit();
            doSomething();
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
