package com.example.prematurebabymonitoringapp.network;

//https://stackoverflow.com/questions/41509195/how-to-send-a-http-delete-with-a-body-in-retrofit

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HTTP;

public interface DeleteDataService {
    @HTTP(method = "DELETE", path = "patients", hasBody = true)
    Call<JsonObject> deletePatient(@Body int hospID);
}