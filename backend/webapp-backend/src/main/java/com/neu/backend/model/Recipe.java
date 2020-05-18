package com.neu.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name="recipe")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "$resolved", "$promise", "deleted" })
public class Recipe implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "varchar(100)")
    private String id;

    private LocalDateTime created_ts;

    private LocalDateTime updated_ts;

    @NotNull(message = "Author Id cannot be empty.")
    private String author_id;
    @NotNull(message = "Cook time cannot be empty.")
    private int cook_time_in_min;
    @NotNull(message = "prep time cannot be empty.")
    private int prep_time_in_min;
    private int total_time_in_min;

    @NotNull(message = "title time cannot be empty.")
    private String title;


    @NotNull(message = "cuisine time cannot be empty.")
    private String cuisine;
    @NotNull(message = "servings time cannot be empty.")
    private int servings;
    @NotNull(message = "ingredients  time cannot be empty.")
    @ElementCollection
    private List <String>ingredients;

    @OneToOne(cascade = {CascadeType.ALL})
    private NutritionInformation nutrition_information;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<OrderedList> steps;

    @OneToOne(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Image image;

    public Recipe() {
        LocalDateTime curr_date = LocalDateTime.now();
        this.created_ts = curr_date;
        this.updated_ts = curr_date;
        ingredients = new ArrayList<>();
        steps = new ArrayList<>();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreated_ts() {
        return created_ts;
    }

    public void setCreated_ts(LocalDateTime created_ts) {
        this.created_ts = created_ts;
    }

    public LocalDateTime getUpdated_ts() {
        return updated_ts;
    }

    public void setUpdated_ts(LocalDateTime updated_ts) {
        this.updated_ts = updated_ts;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public int getCook_time_in_min() {
        return cook_time_in_min;
    }

    public void setCook_time_in_min(int cook_time_in_min) {
        this.cook_time_in_min = cook_time_in_min;
    }

    public int getPrep_time_in_min() {
        return prep_time_in_min;
    }

    public void setPrep_time_in_min(int prep_time_in_min) {
        this.prep_time_in_min = prep_time_in_min;
    }

    public int getTotal_time_in_min() {
        return total_time_in_min;
    }

    public void setTotal_time_in_min(int total_time_in_min) {
        this.total_time_in_min = total_time_in_min;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public List getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public NutritionInformation getNutrition_information() {
        return nutrition_information;
    }

    public void setNutrition_information(NutritionInformation nutrition_information) {
        this.nutrition_information = nutrition_information;
    }

    public List<OrderedList> getSteps() {
        return steps;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setSteps(List<OrderedList> steps) {
        Collections.sort(steps, new Comparator<OrderedList>() {
            @Override
            public int compare(OrderedList u1, OrderedList u2) {
                return Integer.valueOf(u1.getPosition()).compareTo(Integer.valueOf(u2.getPosition()));
            }
        });

        this.steps = steps;
    }
}
