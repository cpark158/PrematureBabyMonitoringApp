package com.example.prematurebabymonitoringapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/* Reference 1 - taken from https://medium.com/@prakash_pun/retrofit-a-simple-android-tutorial-48437e4e5a23 */

public class ClientInstance {
    private static Retrofit retrofit;
//    private static final String BASE_URL = "https://bhmdb2020.herokuapp.com/";
    private static final String BASE_URL = "http://localhost:8080/bhmDB2020/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
/* end of reference 1 */