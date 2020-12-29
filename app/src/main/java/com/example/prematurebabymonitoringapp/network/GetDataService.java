package com.example.prematurebabymonitoringapp.network;

import com.example.prematurebabymonitoringapp.Patient;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface GetDataService {
    @GET("startup")
    Call<List<Patient>> getPatientsList();
}
