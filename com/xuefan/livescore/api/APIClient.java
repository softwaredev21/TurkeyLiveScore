package com.xuefan.livescore.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class APIClient {
    public static Retrofit retrofit;
    private static APIRequest apiRequests;
    public static final String BASE_URL = "https://api-football-v1.p.rapidapi.com/v2/"; //https://soccer.sportmonks.com/

    // Singleton Instance of APIRequests
    public static APIRequest getInstance() {
        if (apiRequests == null) {
            ApiKeyInterceptor interceptor = new ApiKeyInterceptor();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiRequests = retrofit.create(APIRequest.class);

            return apiRequests;
        }
        else {
            return apiRequests;
        }
    }
}
