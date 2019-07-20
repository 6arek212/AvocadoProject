package com.example.testavocado.Utils;

import android.content.Context;

import com.example.testavocado.Models.Post;
import com.example.testavocado.Models.Status;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public class NetworkClient {
    private static final String BASE_URL = "http://ahmadgabarin-001-site1.ftempurl.com/";

   // private static final String BASE_URL = "http://tarik775-001-site1.itempurl.com/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitClient() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }



    public interface UploadAPIs {
        @Multipart
        @POST("api/UploadPhoto/PostUserImage")
        Call<ResponseBody> uploadImage(@Part MultipartBody.Part file, @Part("name") RequestBody requestBody, @Query("id") int id);
    }











}