package com.neu.backend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neu.backend.config.ElasticsearchConfig;
import com.neu.backend.model.Recipe;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ElascticsearchService {

    @Autowired
    ElasticsearchConfig elasticsearchConfig;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MeterRegistry meterRegistry;

    public ElascticsearchService(){}

    public IndexResponse addRecipe(Recipe recipe) throws Exception{
        Timer timer = meterRegistry.timer("elasticsearch.recipe.save");
        long startTime = System.nanoTime();
        byte[] RecipeMapper = objectMapper.writeValueAsBytes(recipe);

        IndexRequest indexRequest = new IndexRequest("recipe", "_doc", recipe.getId())
                .source(RecipeMapper, XContentType.JSON);
        timer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        timer.close();

        return elasticsearchConfig.client().index(indexRequest, RequestOptions.DEFAULT);
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

    public List<Recipe> getRecipes() throws Exception{
        Timer timer = meterRegistry.timer("elasticsearch.recipe.get.all");
        long startTime = System.nanoTime();
        SearchRequest searchRequest = new SearchRequest("recipe");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = elasticsearchConfig.client().search(searchRequest, RequestOptions.DEFAULT);
        timer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        timer.close();
        return getSearchResult(searchResponse);
    }

    public Recipe getRecipeById(String id) throws Exception{
        Timer timer = meterRegistry.timer("elasticsearch.recipe.get.id");
        long startTime = System.nanoTime();
        GetRequest getRequest = new GetRequest("recipe", "_doc", id);

        GetResponse getResponse = elasticsearchConfig.client().get(getRequest, RequestOptions.DEFAULT);
        Map<String, Object> resultMap = getResponse.getSource();
        timer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        timer.close();
        return objectMapper.convertValue(resultMap, Recipe.class);
    }

    public UpdateResponse updateRecipe(Recipe updatedRecipe) throws Exception{
        Timer timer = meterRegistry.timer("elasticsearch.recipe.update");
        long startTime = System.nanoTime();
        UpdateRequest updateRequest = new UpdateRequest(
                "recipe",
                "_doc",
                updatedRecipe.getId());

        byte[] RecipeMapper = objectMapper.writeValueAsBytes(updatedRecipe);

        updateRequest.doc(RecipeMapper, XContentType.JSON);
        timer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        timer.close();
        return elasticsearchConfig.client().update(updateRequest, RequestOptions.DEFAULT);
    }

    public DeleteResponse deleteRecipeById(String id) throws  Exception{
        Timer timer = meterRegistry.timer("elasticsearch.recipe.delete");
        long startTime = System.nanoTime();
        DeleteRequest deleteRequest = new DeleteRequest("recipe", "_doc", id);
        timer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        timer.close();
        return elasticsearchConfig.client().delete(deleteRequest, RequestOptions.DEFAULT);
    }


}
