package org.example;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="Garages")
public class Garage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="Address")
    private String address;

    @Column(name="Phone_Number")
    private String num;

    @ManyToMany
    @JoinTable(name="Garage_Owners",
            joinColumns = @JoinColumn(name="garage_id"),
            inverseJoinColumns = @JoinColumn(name="person_id"))
    private List<Person> owners=new ArrayList<>();

    @ManyToMany
    @JoinTable(name="Garage_Owners",
            joinColumns = @JoinColumn(name="garage_id"),
            inverseJoinColumns = @JoinColumn(name="car_id"))
    private List<Car> cars=new ArrayList<>();


    public Garage(){
    }

    public Garage(String address,String num){
        this.address=address;
        this.num=num;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return this.num;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.num = phoneNumber;
    }

    public void setCars(List<Car> cars){
        this.cars=cars;
    }

    public List<Car> getCars(){
        return this.cars;
    }

    public void setOwners(List<Person> owners){
        this.owners=owners;
    }

    public List<Person> getOwners(){
        return this.owners;
    }
}
