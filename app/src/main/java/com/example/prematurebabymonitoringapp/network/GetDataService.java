package com.example.prematurebabymonitoringapp.network;

import com.example.prematurebabymonitoringapp.Patient;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

/* Reference 2 - taken from https://medium.com/@prakash_pun/retrofit-a-simple-android-tutorial-48437e4e5a23 */
public interface GetDataService {
    @GET("startup")
    Call<List<Patient>> getPatientsList();
}
/* end of reference 2 */