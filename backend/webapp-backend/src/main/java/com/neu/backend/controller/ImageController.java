package com.neu.backend.controller;

import com.neu.backend.model.Image;
import com.neu.backend.model.Recipe;
import com.neu.backend.model.User;
import com.neu.backend.response.ApiResponse;
import com.neu.backend.services.ImageService;
import com.neu.backend.services.RecipeService;
import com.neu.backend.services.UserService;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/recipe")
public class ImageController {

    @Autowired
    RecipeService Recipeservice;

    @Autowired
    UserService userService;

    @Autowired
    ImageService imageService;

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Timed(value = "api.v1.recipe.id.image.post")
    @PostMapping("/{id}/image")
    public ResponseEntity<Object> addImage(@PathVariable String id, @RequestPart(value="file")MultipartFile file) throws Exception{

        String message = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_email = SecurityContextHolder.getContext().getAuthentication().getName();
        ApiResponse apiResponse;

        if (message.equals("Username does not exist") || message.equals("Invalid Credentials")|| message.equals("Username not entered") || message.equals("Password not entered") || message.equals("anonymousUser")) {
            logger.error("Username and invalid credentials");
            apiResponse = new ApiResponse(HttpStatus.UNAUTHORIZED, message, message);
            return new ResponseEntity<Object>(apiResponse, HttpStatus.UNAUTHORIZED);
        }

        User user = (User)this.userService.getUser(user_email).getBody();

        Recipe recipe = (Recipe)this.Recipeservice.getRecipeById(id).getBody();

        if(this.Recipeservice.getRecipeById(id).getBody() == null){
            logger.error("Recipe not Found");
            apiResponse = new ApiResponse(HttpStatus.NOT_FOUND, "Recipe not Found ", "Recipe not Found");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.NOT_FOUND);
        }

        if(!((Recipe)this.Recipeservice.getRecipeById(id).getBody()).getAuthor_id().equalsIgnoreCase(user.getId())){
            logger.error("Recipe does not belong to user");
            apiResponse = new ApiResponse(HttpStatus.UNAUTHORIZED , "Unauthorized to update the recipe", "Unauthorized to update the recipe");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.UNAUTHORIZED);
        }

        Image image = new Image();

        return this.imageService.addImage(recipe, image, file);

    }

    @Timed(value = "api.v1.recipe.id.image.id.get")
    @GetMapping("/{id}/image/{imageId}")
    public ResponseEntity<Object> getImage(@PathVariable String id, @PathVariable String imageId) throws Exception{
        ApiResponse apiResponse;
        Recipe recipe = (Recipe)this.Recipeservice.getRecipeById(id).getBody();

        if(this.Recipeservice.getRecipeById(id).getBody() == null){
            logger.error("Recipe not Found");
            apiResponse = new ApiResponse(HttpStatus.NOT_FOUND, "Recipe not Found ", "Recipe not Found");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.NOT_FOUND);
        }

        return this.imageService.getImageById(imageId);

    }

    @Timed(value = "api.v1.recipe.id.image.id.delete")
    @DeleteMapping("/{id}/image/{imageId}")
    public ResponseEntity<Object> deleteImage(@PathVariable String id, @PathVariable String imageId) throws Exception{
        String message = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_email = (String) SecurityContextHolder.getContext().getAuthentication().getName();
        ApiResponse apiResponse;

        if (message.equals("Username does not exist") || message.equals("Invalid Credentials")|| message.equals("Username not entered") || message.equals("Password not entered") || message.equals("anonymousUser")) {
            logger.error("Username and invalid credentials");
            apiResponse = new ApiResponse(HttpStatus.UNAUTHORIZED, message, message);
            return new ResponseEntity<Object>(apiResponse, HttpStatus.UNAUTHORIZED);
        }

        User user = (User)this.userService.getUser(user_email).getBody();

        Recipe recipe = (Recipe)this.Recipeservice.getRecipeById(id).getBody();

        if(this.Recipeservice.getRecipeById(id).getBody() == null) {
            logger.error("Recipe not Found");
            apiResponse = new ApiResponse(HttpStatus.NOT_FOUND, "Recipe not Found ", "Recipe not Found");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.NOT_FOUND);
        }



        if(!((Recipe)this.Recipeservice.getRecipeById(id).getBody()).getAuthor_id().equalsIgnoreCase(user.getId())){
            logger.error("Recipe does not belong to user");
            apiResponse = new ApiResponse(HttpStatus.UNAUTHORIZED , "Unauthorized to update the recipe", "Unauthorized to update the recipe");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.UNAUTHORIZED);
        }

        Image image = (Image)imageService.getImageById(imageId).getBody();

        if(this.imageService.getImageById(imageId).getBody() == null) {
            logger.error("Recipe not Found");
            apiResponse = new ApiResponse(HttpStatus.NOT_FOUND, "Image not Found ", "Image not Found");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.NOT_FOUND);
        }

        if(!image.getRecipe().getId().equalsIgnoreCase(recipe.getId())){
            logger.error("Wrong Image");
            apiResponse = new ApiResponse(HttpStatus.NOT_FOUND, "Image does not belong to recipe", "Image does not belong to recipe");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.NOT_FOUND);
        }

        return this.imageService.deleteImage(imageId);
    }

    @Timed(value = "api.v1.recipe.id.image.id.put")
    @PutMapping("/{id}/image/{imageId}")
    public ResponseEntity<Object> addImage(@PathVariable String id,@PathVariable String imageId, @RequestPart(value="file")MultipartFile file) throws Exception{
        String message = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_email = (String) SecurityContextHolder.getContext().getAuthentication().getName();
        ApiResponse apiResponse;

        if (message.equals("Username does not exist") || message.equals("Invalid Credentials")|| message.equals("Username not entered") || message.equals("Password not entered") || message.equals("anonymousUser")) {
            logger.error("Username and invalid credentials");
            apiResponse = new ApiResponse(HttpStatus.UNAUTHORIZED, message, message);
            return new ResponseEntity<Object>(apiResponse, HttpStatus.UNAUTHORIZED);
        }

        User user = (User)this.userService.getUser(user_email).getBody();

        Recipe recipe = (Recipe)this.Recipeservice.getRecipeById(id).getBody();

        if(this.Recipeservice.getRecipeById(id).getBody() == null) {
            logger.error("Recipe not Found");
            apiResponse = new ApiResponse(HttpStatus.NOT_FOUND, "Recipe not Found ", "Recipe not Found");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.NOT_FOUND);
        }



        if(!((Recipe)this.Recipeservice.getRecipeById(id).getBody()).getAuthor_id().equalsIgnoreCase(user.getId())){
            logger.error("Recipe does not belong to user");
            apiResponse = new ApiResponse(HttpStatus.UNAUTHORIZED , "Unauthorized to update the recipe", "Unauthorized to update the recipe");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.UNAUTHORIZED);
        }

        Image image = (Image)imageService.getImageById(imageId).getBody();

        if(this.imageService.getImageById(imageId).getBody() == null) {
            logger.error("Recipe not Found");
            apiResponse = new ApiResponse(HttpStatus.NOT_FOUND, "Image not Found ", "Image not Found");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.NOT_FOUND);
        }

        if(!image.getRecipe().equals(recipe)){
            logger.error("Wrong Image");
            apiResponse = new ApiResponse(HttpStatus.NOT_FOUND, "Image does not belong to recipe", "Image does not belong to recipe");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.NOT_FOUND);
        }

        return this.imageService.updateImage(imageId, recipe, image, file);
    }
}
