package com.neu.backend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neu.backend.config.ElasticsearchConfig;
import com.neu.backend.dao.ImageDao;
import com.neu.backend.dao.RecipeDao;
import com.neu.backend.model.Image;
import com.neu.backend.model.Recipe;
import com.neu.backend.response.ApiResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NoResultException;

@Service
public class ImageService {

    @Autowired
    ImageDao imageDao;

    @Autowired
    AmazonS3FileUploadService amazonS3FileUploadService;

    @Autowired
    ElasticsearchConfig elasticsearchConfig;

    @Autowired
    ElascticsearchService elascticsearchService;

    @Autowired
    private ObjectMapper objectMapper;

    public ImageService(){

    }

    public ResponseEntity<Object> addImage(Recipe recipe, Image image, MultipartFile file) throws  Exception{
        try {
            String url = this.amazonS3FileUploadService.uploadFile(file);
            image.setRecipe(recipe);
            image.setUrl(url);
        } catch (Exception e){
            ApiResponse resp = new ApiResponse(HttpStatus.BAD_REQUEST, "Bad Request", "Bad Request");
            return new ResponseEntity<Object>(resp, HttpStatus.BAD_REQUEST);
        }
        this.imageDao.save(image);

        recipe.setImage(image);
//        UpdateRequest updateRequest = new UpdateRequest(
//                "recipe",
//                "_doc",
//                recipe.getId());
//
//        byte[] RecipeMapper = objectMapper.writeValueAsBytes(recipe);
//
//        updateRequest.doc(RecipeMapper, XContentType.JSON);
//
//        UpdateResponse updateResponse =
//                elasticsearchConfig.client().update(updateRequest, RequestOptions.DEFAULT);
        UpdateResponse updateResponse = elascticsearchService.updateRecipe(recipe);

        if(updateResponse.getId().equals("") || updateResponse.getId() == null){
            ApiResponse resp = new ApiResponse(HttpStatus.NOT_FOUND, "Error Updating Elastic Index",
                    "Error Updating Elastic Index");
            return  new ResponseEntity<Object>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Object>(image, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> getImageById(String id){
        Image image;

        try{
            image = this.imageDao.findById(id);
        } catch (NoResultException e){
            ApiResponse resp = new ApiResponse(HttpStatus.NOT_FOUND, "The requested resource could not be found", "Resource not available");
            return new ResponseEntity<Object>(resp, HttpStatus.NOT_FOUND);
        }

        if(image != null){
            return new ResponseEntity<Object>(image, HttpStatus.OK);
        } else {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.NOT_FOUND, "The requested resource could not be found", "Resource not available");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> updateImage(String id, Recipe recipe, Image image, MultipartFile file) throws Exception{
        ApiResponse apiResponse;
        Image imageFromDB = this.imageDao.findById(id);

        if(imageFromDB == null){
            apiResponse = new ApiResponse(HttpStatus.NOT_FOUND, "The requested resource could not be found", "Resource not available");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.NOT_FOUND);
        } else {
            try {
                String oldFileName = imageFromDB.getUrl().substring(imageFromDB.getUrl().lastIndexOf("/")+1);
                this.amazonS3FileUploadService.deleteFile(oldFileName);
                String url = this.amazonS3FileUploadService.uploadFile(file);
                image.setRecipe(recipe);
                image.setUrl(url);
            } catch (Exception e){
                ApiResponse resp = new ApiResponse(HttpStatus.NOT_FOUND, "Bad Request", "Bad Request");
                return new ResponseEntity<Object>(resp, HttpStatus.BAD_REQUEST);
            }
            this.imageDao.update(id, image);

            recipe.setImage(image);
//            UpdateRequest updateRequest = new UpdateRequest(
//                    "recipe",
//                    "_doc",
//                    recipe.getId());
//
//            byte[] RecipeMapper = objectMapper.writeValueAsBytes(recipe);
//
//            updateRequest.doc(RecipeMapper, XContentType.JSON);
//
//            UpdateResponse updateResponse =
//                    elasticsearchConfig.client().update(updateRequest, RequestOptions.DEFAULT);
            UpdateResponse updateResponse = elascticsearchService.updateRecipe(recipe);

            if(updateResponse.getId().equals("") || updateResponse.getId() == null){
                ApiResponse resp = new ApiResponse(HttpStatus.NOT_FOUND, "Error Updating Elastic Index",
                        "Error Updating Elastic Index");
                return  new ResponseEntity<Object>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<Object>(image, HttpStatus.NO_CONTENT);
        }

    }

    public ResponseEntity<Object> deleteImage(String id) throws  Exception{
        ApiResponse apiResponse;
        Image imageFromDB = this.imageDao.findById(id);
        Recipe recipe = imageFromDB.getRecipe();

        if(imageFromDB == null){
            apiResponse = new ApiResponse(HttpStatus.NOT_FOUND, "The requested resource could not be found", "Resource not available");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.NOT_FOUND);
        } else {
            String fileName = imageFromDB.getUrl().substring(imageFromDB.getUrl().lastIndexOf("/")+1);
            try{
                this.amazonS3FileUploadService.deleteFile(fileName);
            } catch (Exception e){
                apiResponse = new ApiResponse(HttpStatus.NOT_FOUND, "Bad Request", "Bad Request");
                return new ResponseEntity<Object>(apiResponse, HttpStatus.BAD_REQUEST);
            }
            this.imageDao.delete(id);

            recipe.setImage(null);
//            UpdateRequest updateRequest = new UpdateRequest(
//                    "recipe",
//                    "_doc",
//                    recipe.getId());
//
//            byte[] RecipeMapper = objectMapper.writeValueAsBytes(recipe);
//
//            updateRequest.doc(RecipeMapper, XContentType.JSON);
//
//            UpdateResponse updateResponse =
//                    elasticsearchConfig.client().update(updateRequest, RequestOptions.DEFAULT);
            UpdateResponse updateResponse = elascticsearchService.updateRecipe(recipe);

            if(updateResponse.getId().equals("") || updateResponse.getId() == null){
                ApiResponse resp = new ApiResponse(HttpStatus.NOT_FOUND, "Error Updating Elastic Index",
                        "Error Updating Elastic Index");
                return  new ResponseEntity<Object>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<Object>(imageFromDB, HttpStatus.NO_CONTENT);
        }
    }
}
