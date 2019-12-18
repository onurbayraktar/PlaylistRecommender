package com.example.onurb.playlistrecommender.classes;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Onurb on 15.12.2019.
 */

public interface JsonPlaceHolderApi {
    @GET("data/2.5/weather")
    Call<ApiResponseWeather> getCurrentWeather(@Query("lat") Double latitude,
                                               @Query("lon") Double longitude,
                                               @Query("APPID") String key);

    @GET("/v1/search")
    Call<ApiResponseSpotify> getRecommendedPlaylists(@Query("q") String q,
                                               @Query("type") String type,
                                               @Header("Authorization") String token);

    @GET("v2/tags")
    Call<ApiResponseImagga> getTagsOfPhoto(@Query("image_url") String image_url);

    @GET("v2/tags")
    Call<ApiResponseImagga> getTagsOfPhotoWithId(@Query("image_upload_id") String image_url);


    @FormUrlEncoded
    @POST("v2/uploads")
    Call<ApiResponseImagga> getUploadIdOfPhoto(@Field("image_base64") String encoded_image);
}
