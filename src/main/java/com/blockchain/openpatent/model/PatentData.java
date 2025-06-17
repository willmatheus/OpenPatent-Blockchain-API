package com.blockchain.openpatent.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
public class PatentData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserData userData;

    private String inventor;
    private String title;
    private String description;
    private double price;
    @CreationTimestamp
    private LocalDate registrationDate;

    public PatentData() {}

    // Getters e setters

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getInventor() { return inventor; }
    public void setInventor(String inventor) { this.inventor = inventor; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public UserData getUserData() { return userData; }
    public void setUserData(UserData userData) { this.userData = userData; }

    @Override
    public String toString() {
        return inventor + "|" + title + "|" + description + "|" + price;
    }
}
