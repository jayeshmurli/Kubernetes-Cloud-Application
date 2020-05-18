package com.neu.backend.dao;

import com.neu.backend.model.Recipe;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class RecipeDao {

    @Autowired
    MeterRegistry meterRegistry;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Recipe saveRecipe(Recipe Recipe) {
        Timer timer = meterRegistry.timer("database.recipe.save");
        long startTime = System.nanoTime();
        this.entityManager.persist(Recipe);
        timer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        timer.close();
        return Recipe;
    }

    @Transactional
    public Recipe getRecipeLatest(){
        Timer timer = meterRegistry.timer("database.recipe.get.latest");
        long startTime = System.nanoTime();
        Query query = this.entityManager.createQuery("FROM Recipe ORDER BY created_ts DESC");
        List<Recipe> RecipeList = query.getResultList();
        timer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        timer.close();
        return RecipeList.get(0);
    }

    @Transactional
    public List<Recipe> getRecipes(){
        Timer timer = meterRegistry.timer("database.recipe.get.all");
        long startTime = System.nanoTime();
        Query query = this.entityManager.createQuery("FROM Recipe");
        List<Recipe> RecipeList = query.getResultList();
        timer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        timer.close();
        return RecipeList;
    }

    @Transactional
    public Recipe getRecipeById(String id) {
        Timer timer = meterRegistry.timer("database.recipe.get.id");
        long startTime = System.nanoTime();
        Recipe Recipe;
        Recipe = entityManager.find(Recipe.class, id);
        timer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        timer.close();
        return Recipe;
    }

    @Transactional
    public Recipe updateRecipe(Recipe Recipe, String id) {
        Timer timer = meterRegistry.timer("database.recipe.update");
        long startTime = System.nanoTime();
        Recipe RecipeToBeUpdated = this.entityManager.find(Recipe.class, id);
        RecipeToBeUpdated.setTitle(Recipe.getTitle());
        //RecipeToBeUpdated.setAuthor_id(Recipe.getAuthor_id());
        RecipeToBeUpdated.setServings(Recipe.getServings());
        RecipeToBeUpdated.setCuisine(Recipe.getCuisine());
        RecipeToBeUpdated.setIngredients(Recipe.getIngredients());
        RecipeToBeUpdated.setPrep_time_in_min(Recipe.getPrep_time_in_min());
        RecipeToBeUpdated.setCook_time_in_min(Recipe.getCook_time_in_min());
        RecipeToBeUpdated.setTotal_time_in_min(Recipe.getPrep_time_in_min()+Recipe.getCook_time_in_min());
        RecipeToBeUpdated.setSteps(Recipe.getSteps());
        RecipeToBeUpdated.setNutrition_information(RecipeToBeUpdated.getNutrition_information());
        RecipeToBeUpdated.setUpdated_ts(LocalDateTime.now());
        flushAndClear();
        timer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        timer.close();
        return RecipeToBeUpdated;
    }

    @Transactional
    public void deleteById(Recipe Recipe) {
        Timer timer = meterRegistry.timer("database.recipe.delete");
        long startTime = System.nanoTime();
        entityManager.remove(Recipe);
        timer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        timer.close();
        flushAndClear();
    }

    void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

}


