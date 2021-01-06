package com.example.prematurebabymonitoringapp.network;

import com.example.prematurebabymonitoringapp.Patient;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PostDataService {
    @POST("patients")
    Call<Patient> sendPatient(@Body Patient newPat);
}

