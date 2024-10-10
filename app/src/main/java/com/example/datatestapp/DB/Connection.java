package com.example.datatestapp.DB;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Connection {

    private static final String URL = "http://192.168.1.103/adduser.php/";
    private static Retrofit retrofit=null;

    public static Retrofit getConnection() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
