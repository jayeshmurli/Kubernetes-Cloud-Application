package com.neu.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.*;

@Entity
public class NutritionInformation {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "varchar(100)")
    @JsonIgnore
    private String id;

    private int calories;

    private double cholesterol_in_mg;

    private int sodium_in_mg;

    private double carbohydrates_in_grams;

    private double protein_in_grams;


    public NutritionInformation(){

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public double getCholesterol_in_mg() {
        return cholesterol_in_mg;
    }

    public void setCholesterol_in_mg(double cholesterol_in_mg) {
        this.cholesterol_in_mg = cholesterol_in_mg;
    }

    public int getSodium_in_mg() {
        return sodium_in_mg;
    }

    public void setSodium_in_mg(int sodium_in_mg) {
        this.sodium_in_mg = sodium_in_mg;
    }

    public double getCarbohydrates_in_grams() {
        return carbohydrates_in_grams;
    }

    public void setCarbohydrates_in_grams(double carbohydrates_in_grams) {
        this.carbohydrates_in_grams = carbohydrates_in_grams;
    }

    public double getProtein_in_grams() {
        return protein_in_grams;
    }

    public void setProtein_in_grams(double protein_in_grams) {
        this.protein_in_grams = protein_in_grams;
    }
}
