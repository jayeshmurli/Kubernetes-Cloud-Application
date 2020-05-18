package com.neu.backend.controller;

import java.util.logging.FileHandler;


import com.neu.backend.dao.RecipeDao;
import com.neu.backend.dao.UserDao;
import com.neu.backend.model.Recipe;
import com.neu.backend.model.User;
import com.neu.backend.response.ApiResponse;
import com.neu.backend.services.RecipeService;
import com.neu.backend.services.UserService;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


@RestController
public class RecipeController {
    @Autowired
    RecipeService Recipeservice;

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    RecipeDao recipeDao;


    private static final Logger logger = LoggerFactory.getLogger(RecipeController.class);
    FileHandler fh;

    @Timed(value = "api.v1.recipe.post")
    @RequestMapping(value = "/v1/recipe", method = RequestMethod.POST)
    public ResponseEntity<Object> addRecipe(@RequestBody Recipe Recipe) throws  Exception {
        logger.info("Adding Recipe with ID :"+Recipe.getId());
        String message = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_email = (String) SecurityContextHolder.getContext().getAuthentication().getName();
        ApiResponse apiResponse;

        if (message.equals("Username does not exist") || message.equals("Invalid Credentials")|| message.equals("Username not entered") || message.equals("Password not entered") || message.equals("anonymousUser")) {
            logger.error("Username and invalid credentials");
            apiResponse = new ApiResponse(HttpStatus.UNAUTHORIZED, message, message);
            return new ResponseEntity<Object>(apiResponse, HttpStatus.UNAUTHORIZED);
        }

        if(Recipe.getCook_time_in_min()%5!=0 || Recipe.getPrep_time_in_min()%5!=0){
            logger.error("Time not in multiple of 5");
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Time not in multiple of 5", "Time not in multiple of 5");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isEmpty(Recipe.getTitle())) {
            logger.error("Title is needed");
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Please Enter Title", "Please Enter Title");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isEmpty(Recipe.getCook_time_in_min())) {
            logger.error("Cook Time is needed");
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Please Enter Cook Time", "Please Enter Cook Time");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(Recipe.getPrep_time_in_min())) {
            logger.error("Prep Time is needed");
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Please Prep Time", "Please Prep Time");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(Recipe.getServings())) {
            logger.error("Quantity is needed");
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Please Enter Servings ", "Please Enter Servings ");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(Recipe.getCuisine())) {
            logger.error("Quantity is needed");
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Please Enter Cusine ", "Please Enter Cusine");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(Recipe.getIngredients())) {
            logger.error("Quantity is needed");
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Please Enter Ingredients ", "Please Enter Ingredients");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(Recipe.getNutrition_information())){
            logger.error("Quantity is needed");
        apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Please Enter NutritionInformation ", "Please Enter NutritionInformation");
        return new ResponseEntity<Object>(apiResponse, HttpStatus.BAD_REQUEST);
         }
        if (StringUtils.isEmpty(Recipe.getSteps())){
            logger.error("Quantity is needed");
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Please Enter Steps ", "Please Enter Steps");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.BAD_REQUEST);
        }
            logger.info("Recipe added with ID :"+Recipe.getId());
        User user = (User)this.userService.getUser(user_email).getBody();
        Recipe.setAuthor_id(user.getId());
        logger.info("Receipe info : " + Recipe.getNutrition_information());
        return this.Recipeservice.addRecipe(Recipe);
    }

    @Timed(value = "api.v1.recipes.get")
    @CrossOrigin
    @RequestMapping(value="/v1/recipes", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllRecipes(){


        ApiResponse apiResponse;


        logger.info("Get all the Recipes");
        return this.Recipeservice.getRecipesLatest();

    }

    @Timed(value = "api.v1.recipe.get")
    @CrossOrigin
    @RequestMapping(value="/v1/recipe", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllRecipe() throws  Exception{

        logger.info("Get all the Recipes");
        return this.Recipeservice.getRecipes();

    }

    @Timed(value = "api.v1.recipe.id.put")
    @RequestMapping(value="/v1/recipe/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateRecipe(@PathVariable @NonNull String id, @RequestBody Recipe Recipe) throws Exception{

        String message = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_email = (String) SecurityContextHolder.getContext().getAuthentication().getName();
        ApiResponse apiResponse;

        if (message.equals("Username does not exist") || message.equals("Invalid Credentials")|| message.equals("Username not entered") || message.equals("Password not entered") || message.equals("anonymousUser")) {
            logger.error("Username and invalid credentials");
            apiResponse = new ApiResponse(HttpStatus.UNAUTHORIZED, message, message);
            return new ResponseEntity<Object>(apiResponse, HttpStatus.UNAUTHORIZED);
        }


        if (StringUtils.isEmpty(Recipe.getTitle())) {
            logger.error("Title is needed");
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Please Enter Title", "Please Enter Title");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isEmpty(Recipe.getCook_time_in_min())) {
            logger.error("Cook Time is needed");
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Please Enter Cook Time", "Please Enter Cook Time");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(Recipe.getPrep_time_in_min())) {
            logger.error("Prep Time is needed");
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Please Prep Time", "Please Prep Time");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(Recipe.getServings())) {
            logger.error("Quantity is needed");
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Please Enter Servings ", "Please Enter Servings ");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(Recipe.getCuisine())) {
            logger.error("Quantity is needed");
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Please Enter Cusine ", "Please Enter Cusine");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(Recipe.getIngredients())) {
            logger.error("Quantity is needed");
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Please Enter Ingredients ", "Please Enter Ingredients");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(Recipe.getNutrition_information())){
            logger.error("Quantity is needed");
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Please Enter NutritionInformation ", "Please Enter NutritionInformation");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(Recipe.getSteps())){
            logger.error("Quantity is needed");
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Please Enter Steps ", "Please Enter Steps");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.BAD_REQUEST);
        }
        User user = (User)this.userService.getUser(user_email).getBody();

        if(this.Recipeservice.getRecipeById(id).getBody() == null){
            logger.error("Quantity is needed");
            apiResponse = new ApiResponse(HttpStatus.NOT_FOUND, "Recipe not Found ", "Recipe not Found");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.NOT_FOUND);
        }

        if(!((Recipe)this.Recipeservice.getRecipeById(id).getBody()).getAuthor_id().equalsIgnoreCase(user.getId())){
            logger.error("Recipe does not belong to user");
            apiResponse = new ApiResponse(HttpStatus.UNAUTHORIZED , "Unauthorized to update the recipe", "Unauthorized to update the recipe");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.BAD_REQUEST);
        }
        Recipe.setAuthor_id(user.getId());
        logger.info("Update Recipe with ID"+Recipe.getId());

        return this.Recipeservice.updateRecipe(Recipe,id);
    }

    @Timed(value = "api.v1.recipe.id.get")
    @RequestMapping(value= "/v1/recipe/{id}", method=RequestMethod.GET)
    public ResponseEntity<Object> getRecipe(@PathVariable @NonNull String id) throws  Exception{

        logger.info("Inside Controller");
        ApiResponse errorResponse;
//        String message = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (message.equals("Username does not exist") || message.equals("Invalid Credentials") || message.equals("Username not entered") || message.equals("Password not entered")) {
//            logger.error("Username and invalid credentials");
//            errorResponse = new ApiResponse(HttpStatus.UNAUTHORIZED, message, message);
//            return new ResponseEntity<Object>(errorResponse, HttpStatus.UNAUTHORIZED);
//        }
        logger.info("GET Recipe with ID"+id);
        return Recipeservice.getRecipeById(id);


    }

    @Timed(value = "api.v1.recipe.id.delete")
    @RequestMapping(value="/v1/recipe/{id}" ,method=RequestMethod.DELETE)
    public ResponseEntity<Object> deleteRecipe(@PathVariable @NonNull String id) throws  Exception  {
        ApiResponse errorResponse;
        String message = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_email = (String) SecurityContextHolder.getContext().getAuthentication().getName();
        if (message.equals("Username does not exist") || message.equals("Invalid Credentials")|| message.equals("Username not entered") || message.equals("Password not entered") || message.equals("anonymousUser")) {
            logger.error("Username and invalid credentials");
            errorResponse = new ApiResponse(HttpStatus.UNAUTHORIZED, message, message);
            return new ResponseEntity<Object>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
        User user = (User)this.userService.getUser(user_email).getBody();

        if(this.Recipeservice.getRecipeById(id).getBody() == null){
            logger.error("Quantity is needed");
            errorResponse = new ApiResponse(HttpStatus.NOT_FOUND, "Recipe not Found ", "Recipe not Found");
            return new ResponseEntity<Object>(errorResponse, HttpStatus.NOT_FOUND);
        }

        if(!((Recipe)this.Recipeservice.getRecipeById(id).getBody()).getAuthor_id().equalsIgnoreCase(user.getId())){
            logger.error("Recipe does not belong to user");
            errorResponse = new ApiResponse(HttpStatus.UNAUTHORIZED , "Unauthorized to update the recipe", "Unauthorized to update the recipe");
            return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        logger.info("DELETE Recipe with ID"+id);
        return Recipeservice.deleteRecipeById(id);

    }

}