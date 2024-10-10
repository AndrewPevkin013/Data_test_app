package com.example.datatestapp.DB;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Interfase {
    @FormUrlEncoded
    @POST("adduser.php") //php file
    Call<Response>adduser(@Field("name") String name, @Field("latitude") String latitude,
                          @Field("longitude") String longitude, @Field("altitude") String altitude,
                          @Field("date_time") String dateTime);


}
