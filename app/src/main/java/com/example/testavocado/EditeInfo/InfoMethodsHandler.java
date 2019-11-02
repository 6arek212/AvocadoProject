package com.example.testavocado.EditeInfo;

import android.util.Log;
import android.widget.Toast;

import com.example.testavocado.Login.LoginMethods;
import com.example.testavocado.Models.Status;
import com.example.testavocado.R;
import com.example.testavocado.Utils.NetworkClient;
import com.example.testavocado.Utils.ServerConnection;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class InfoMethodsHandler {
    private static final String TAG = "InfoMethodsHandler";


    public interface OnUpdateListener {
        void onSuccessListener();
        void onServerException(String ex);
        void onFailureListener(String ex);
    }


    public interface update1Method {
        @GET("api/LoginRegisterInfo/updateProfilePhotoGenderBirthDate")
        Call<Status> updateLastNameBirthDateGender(@Query("User_id") int id,@Query("image_path") String image_path,
              @Query("User_gender") int gender,@Query("User_birth_date") String birth_date, @Query("user_country") String user_country);




        @GET("api/LoginRegisterInfo/updateCityCountry")
        Call<Status> updateCityCountry(@Query("User_id") int id, @Query("User_country") String country,
                                       @Query("User_city") String city);



        @GET("api/LoginRegisterInfo/updateProfilePhoto")
        Call<Status> ProfileImage(@Query("User_id") int id, @Query("image_path") String imagePath);
    }








    public static void updateProfilePhotoGenderBirthDate(int userId, String image_path, int genderNum, String birthDate,String user_country,final OnUpdateListener listener) {
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        update1Method update=retrofit.create(update1Method.class);

        final Call<Status> ca= update.updateLastNameBirthDateGender(userId,image_path,genderNum,birthDate,user_country);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status=response.body();

                if (response.isSuccessful()) {
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







    /**
     * Updating the user location setting
     *
     * @param user_id
     * @param country
     * @param city
     */


    public static void updatingUserLocationSetting(int user_id, String country, String city, final OnUpdateListener listener) {
        Log.d(TAG, "updatingUserLocationSetting: attempting to update location settings");

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        update1Method update=retrofit.create(update1Method.class);

        final Call<Status> ca= update.updateCityCountry(user_id,country,city);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status=response.body();

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
            }
            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailureListener(t.getMessage());
            }
        });
    }







    public static void updateProfileImage(int user_id, String imagePath, final OnUpdateListener listener) {
        Log.d(TAG, "updatingUserLocationSetting: attempting to update location settings");

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        update1Method update=retrofit.create(update1Method.class);

        final Call<Status> ca= update.ProfileImage(user_id,imagePath);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status=response.body();

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
            }
            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailureListener(t.getMessage());
            }
        });
    }
}
