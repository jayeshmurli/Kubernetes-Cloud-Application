package com.neu.backend.services;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.persistence.NoResultException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neu.backend.config.ElasticsearchConfig;
import com.neu.backend.dao.RecipeDao;
import com.neu.backend.model.NutritionInformation;
import com.neu.backend.model.Recipe;
import com.neu.backend.response.ApiResponse;
import io.micrometer.core.instrument.MeterRegistry;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class RecipeService {
    @Autowired
    RecipeDao Recipedao;

    @Autowired
    ElasticsearchConfig elasticsearchConfig;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    ElascticsearchService elascticsearchService;

    @Autowired
    MeterRegistry meterRegistry;

    public RecipeService() {

    }

    public ResponseEntity<Object> addRecipe(Recipe Recipe) throws Exception {
        Recipe.setTotal_time_in_min(Recipe.getCook_time_in_min()+Recipe.getPrep_time_in_min());
        this.Recipedao.saveRecipe(Recipe);

        System.out.println("Receipe is : "+Recipe.getId());

        IndexResponse indexResponse = elascticsearchService.addRecipe(Recipe);

        if(indexResponse.getId().equals("") || indexResponse.getId() == null){
            ApiResponse resp = new ApiResponse(HttpStatus.NOT_FOUND, "Error Creating Elastic Index",
                    "Error Creating Elastic Index");
            return  new ResponseEntity<Object>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Object>(Recipe, HttpStatus.CREATED);

    }

    public ResponseEntity<Object> getRecipesLatest() {

        Recipe recipe;
        try {
             recipe = this.Recipedao.getRecipeLatest();
            if (recipe==null) {
                ApiResponse resp = new ApiResponse(HttpStatus.NOT_FOUND, "The requested resource could not be found",
                        "Resource not available");
                return new ResponseEntity<Object>(resp, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<Object>(recipe, HttpStatus.OK);
            }
        } catch(Exception e){
            ApiResponse resp = new ApiResponse(HttpStatus.NOT_FOUND, "The requested resource could not be found",
                    e.getMessage());
            return new ResponseEntity<Object>(resp, HttpStatus.NOT_FOUND);
        }
    }

    private List<Recipe> getSearchResult(SearchResponse response) {

        SearchHit[] searchHit = response.getHits().getHits();

        List<Recipe> recipes = new ArrayList<>();

        if (searchHit.length > 0) {

            Arrays.stream(searchHit)
                    .forEach(hit -> recipes
                            .add(objectMapper
                                    .convertValue(hit.getSourceAsMap(),
                                            Recipe.class))
                    );
        }

        return recipes;
    }

    public ResponseEntity<Object> getRecipes() throws Exception{

//        SearchRequest searchRequest = new SearchRequest("recipe");
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
//        searchRequest.source(searchSourceBuilder);
//
//        SearchResponse searchResponse = elasticsearchConfig.client().search(searchRequest, RequestOptions.DEFAULT);

        List<Recipe> recipe =  elascticsearchService.getRecipes();

        if (recipe==null) {
            ApiResponse resp = new ApiResponse(HttpStatus.NOT_FOUND, "The requested resource could not be found",
                    "Resource not available");
            return new ResponseEntity<Object>(resp, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<Object>(recipe, HttpStatus.OK);
        }
    }





    public ResponseEntity<Object> getRecipeById(String id) throws Exception{
        Recipe Recipe;

        try {
            //Recipe=Recipedao.getRecipeById(id);
//            GetRequest getRequest = new GetRequest("recipe", "_doc", id);
//
//            GetResponse getResponse = elasticsearchConfig.client().get(getRequest, RequestOptions.DEFAULT);
//            Map<String, Object> resultMap = getResponse.getSource();
//
//            Recipe = objectMapper
//                    .convertValue(resultMap, Recipe.class);
            Recipe = elascticsearchService.getRecipeById(id);
        }
        catch(NoResultException e){
            ApiResponse resp = new ApiResponse(HttpStatus.NOT_FOUND, "The requested resource could not be found", "Resource not available");
            return new ResponseEntity<Object>(resp, HttpStatus.NOT_FOUND);
        }

        if(Recipe != null) {

            return new ResponseEntity<Object>(Recipe, HttpStatus.OK);
        }

        else {
           ApiResponse resp = new ApiResponse(HttpStatus.NOT_FOUND, "The requested resource could not be found", "Resource not available");
            return new ResponseEntity<Object>(resp, HttpStatus.NOT_FOUND);

        }
    }

    public ResponseEntity<Object> updateRecipe(Recipe Recipe, String id) throws Exception{
        Recipe RecipeFromDb;
        ApiResponse apiResponse;
        RecipeFromDb = Recipedao.getRecipeById(id);


        if(RecipeFromDb == null) {
            apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "The requested resource could not be found", "Resource not available");
            return new ResponseEntity<Object>(apiResponse, HttpStatus.BAD_REQUEST);
        }else {
            Recipe updatedRecipe = this.Recipedao.updateRecipe(Recipe, id);
            //apiResponse = new ApiResponse(HttpStatus.NO_CONTENT, "The requested resource has been updated", "Resource updated successfully");

//            UpdateRequest updateRequest = new UpdateRequest(
//                    "recipe",
//                    "_doc",
//                    updatedRecipe.getId());
//
//            byte[] RecipeMapper = objectMapper.writeValueAsBytes(updatedRecipe);
//
//            updateRequest.doc(RecipeMapper, XContentType.JSON);
//
//            UpdateResponse updateResponse =
//                    elasticsearchConfig.client().update(updateRequest, RequestOptions.DEFAULT);
            UpdateResponse updateResponse = elascticsearchService.updateRecipe(updatedRecipe);

            if(updateResponse.getId().equals("") || updateResponse.getId() == null){
                ApiResponse resp = new ApiResponse(HttpStatus.NOT_FOUND, "Error Updating Elastic Index",
                        "Error Updating Elastic Index");
                return  new ResponseEntity<Object>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<Object>(updatedRecipe, HttpStatus.NO_CONTENT);
        }
    }



    public ResponseEntity<Object> deleteRecipeById(String id) throws  Exception{

        try {

            Recipe Recipe= Recipedao.getRecipeById(id);
            if(Recipe != null)
            {
                Recipedao.deleteById(Recipe);
//                DeleteRequest deleteRequest = new DeleteRequest("recipe", "_doc", id);
//                DeleteResponse response =
//                        elasticsearchConfig.client().delete(deleteRequest, RequestOptions.DEFAULT);
                DeleteResponse response = elascticsearchService.deleteRecipeById(id);

            }else {
                ApiResponse resp = new ApiResponse(HttpStatus.NOT_FOUND, "The requested resource could not be found", "Resource not available");
                return new ResponseEntity<Object>(resp, HttpStatus.NOT_FOUND);
            }
        }

        catch(NoResultException e){
            ApiResponse resp = new ApiResponse(HttpStatus.NOT_FOUND, "The requested resource could not be found", "Resource not available");
            return new ResponseEntity<Object>(resp, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Object>(null ,HttpStatus.NO_CONTENT);
    }

}