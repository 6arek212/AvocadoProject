package com.example.testavocado.Login;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.testavocado.Models.Post;
import com.example.testavocado.Models.Status;
import com.example.testavocado.R;
import com.example.testavocado.Utils.NetworkClient;
import com.example.testavocado.Utils.ServerConnection;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RegisterMethods {
    private static final String TAG = "RegisterMethods";


    public interface Notification {
        @GET("api/Notification/SendNotification")
        Call<Status> sendNotification(@Query("token") String token,@Query("title") String title, @Query("body") String body);


        @GET("api/Notification/updateToken")
        Call<Status> updateToken(@Query("token") String token,@Query("user_id") int user_id);
    }



    public interface OnNotificationListener {
        void onSuccessListener();
        void onServerException(String ex);
        void onFailureListener(String ex);
    }


    public static void updateToken(String token,int user_id,final OnNotificationListener listener) {

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        Notification a=retrofit.create(Notification.class);

        final Call<Status> ca= a.updateToken(token,user_id);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status=response.body();

                if (response.isSuccessful()){
                    if(status.getState()==1){
                        Log.d(TAG, "onResponse: "+status);
                        listener.onSuccessListener();
                    }
                    else if(status.getState()==0)
                    {
                        listener.onServerException(status.getException());
                    }else
                    {
                        listener.onFailureListener(status.getException());
                    }
                }else{
                    listener.onFailureListener(response.message());
                }
            }
            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailureListener(t.getMessage());
            }
        });
    }





    public static void onSendingNotification(String token, final String title, String body,final OnNotificationListener listener) {

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        Notification a=retrofit.create(Notification.class);

        final Call<Status> ca= a.sendNotification(token,title,body);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status=response.body();

                if (response.isSuccessful()){
                    if(status.getState()==1){
                        Log.d(TAG, "onResponse: "+status);
                        listener.onSuccessListener();
                    }
                    else if(status.getState()==0)
                    {
                        listener.onServerException(status.getException());
                    }else
                    {
                        listener.onFailureListener(status.getException());
                    }
                }else{
                    listener.onFailureListener(response.message());
                }
            }
            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailureListener(t.getMessage());
            }
        });
    }






    public interface onLoginRegister {
        void onSuccessListener(int user_id);
        void onServerException(String ex);
        void onFailureListener(String ex);
    }



    public interface registerNewUser {
        @GET("api/LoginRegisterInfo/registerNewUser")
        Call<Status> registerNewUser(@Query("User_first_name") String first_name,@Query("user_last_name") String last_name, @Query("User_Email") String email, @Query("User_password") String password,@Query("register_datetime") String register_datetime);
    }


    /**
     * Adding new user to the data base
     * <p>
     * <p>
     * server function :
     * checking if the email is not been used and creating a new user with initial values
     * <p>
     * <p>
     * <p>
     * the server returns :
     * <p>
     * 0- if the task is not done - and return custom exception
     * 1- if the task is done - and  custom exception
     * -1- if the task failed and return System Exception for Debugging
     *

     */

    public static void onRegisteringNewUser(String name,String last_name, final String email, String password,String register_datetime, final Context context, final onLoginRegister listener) {

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        registerNewUser newUser=retrofit.create(registerNewUser.class);

        final Call<Status> ca= newUser.registerNewUser(name,last_name,email,password,register_datetime);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status=response.body();

                if(status.getState()==1){
                        Log.d(TAG, "onResponse: "+status);

                    try {
                        JSONObject json=new JSONObject(status.getJson_data());
                        int User_id=json.getInt("User_id");
                        listener.onSuccessListener(User_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                else if(status.getState()==0)
                {
                    listener.onServerException(status.getException());
                }else
                {
                    listener.onFailureListener(status.getException());
                }
            }
            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailureListener(t.getMessage());
            }
        });
    }







}


