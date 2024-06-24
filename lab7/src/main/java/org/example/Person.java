package org.example;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "People")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="First_Name")
    private String firstName;

    @Column(name="Last_Name")
    private String lastName;

    @Column(name = "pwd")
    private String pwd;

    @Column(name="email_address")
    private String emailAddress;

    @OneToMany(mappedBy = "owner",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Car> cars=new ArrayList<>();;

    @ManyToMany(mappedBy = "owners")
    private List<Garage> garages=new ArrayList<>();
    public Person() {
    }

    public Person(String firstName,String lastName,String pwd, String emailAddress) {
        super();
        this.firstName=firstName;
        this.lastName=lastName;
        this.pwd=pwd;
        this.emailAddress=emailAddress;
    }

   public void setFirstName(String firstName){
        this.firstName=firstName;
   }

   public void setLastName(String lastName){
        this.lastName=lastName;
   }

   public void setPwd(String pwd){
        this.pwd=pwd;
   }

   public void setEmailAddress(String eAdd){
        this.emailAddress=eAdd;
   }

   public String getFirstName(){
        return this.firstName;
   }

   public String getLastName(){
        return this.lastName;
   }

   public String getPwd(){
        return this.pwd;
   }

   public String getEmailAddress(){
        return this.emailAddress;
   }

   public List<Car> getCars(){
        return this.cars;
   }

   public void setCars(List<Car> cars){
        this.cars=cars;
   }

   public List<Garage> getGarages(){
        return this.garages;
   }

   public void setGarages(List<Garage> g){
        this.garages=g;
   }

}
