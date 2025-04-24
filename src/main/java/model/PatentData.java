package model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

public class PatentData {
    private String inventor;
    private String title;
    private String description;
    private double price;

    public PatentData() {}

    public PatentData(String inventor, String title, String description, double price) {
        this.inventor = inventor;
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public String getInventor() { return inventor; }
    public void setInventor(String inventor) { this.inventor = inventor; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return inventor + "|" + title + "|" + description + "|" + price;
    }
}
