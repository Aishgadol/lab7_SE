package org.example;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String licensePlate;

    @Column(name="price_of_car")
    private double price;

    @Column(name = "year_car_was_made")
    private int year;

    @Column(name="link_to_image")
    private String link;

    @ManyToOne
    @JoinColumn(name="owner_id")
    private Person owner;

    @ManyToMany(mappedBy = "cars")
    private List<Garage> garages=new ArrayList<>();

    public Car() {
    }

    public Car(String licensePlate, double price, int year) {
        super();
        this.licensePlate = licensePlate;
        this.price = price;
        this.year = year;
        this.link="www."+licensePlate+".com";
        this.owner=null;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setOwner(Person owner){
        this.owner=owner;
    }

    public Person getOwner(){
        return this.owner;
    }

    public List<Garage> getGarages(){
        return this.garages;
    }

    public void setGarages(List<Garage> g){
        this.garages=g;
    }
}
