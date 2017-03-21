package com.wizeline.omarsanchez.nytimessearch.interfaces;

import com.wizeline.omarsanchez.nytimessearch.models.SearchResults;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.wizeline.omarsanchez.nytimessearch.webService.Config.SEARCH;

/**
 * Created by omarsanchez on 3/16/17.
 */

public interface RetrofitInterface {

    @GET(SEARCH)
    Call<SearchResults> searchDocs(@Query("api-key") String apiKey, @Query("q") String q, @Query("page") String page, @Query("begin_date") String date, @Query("sort") String sort,@Query("fq") String news_desk);

    @GET(SEARCH)
    Call<SearchResults> requestDocs(@Query("api-key") String apiKey, @Query("page") String page, @Query("begin_date") String date, @Query("sort") String sort, @Query("fq") String news_desk);
}
