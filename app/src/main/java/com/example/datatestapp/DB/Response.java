package com.example.datatestapp.DB;

import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("status")
    private String status;

    @SerializedName("result_code")
    private int result_code;


    public int getResult_code() {
        return result_code;
    }

    public String getStatus() {
        return status;
    }
}
