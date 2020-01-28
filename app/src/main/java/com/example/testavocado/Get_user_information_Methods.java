package com.example.testavocado;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.testavocado.Models.Status;
import com.example.testavocado.Models.User;
import com.example.testavocado.Profile.ProfileHandler;
import com.example.testavocado.Utils.NetworkClient;
import com.example.testavocado.objects.user_information;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Get_user_information_Methods {
    public static final String TAG = "Get_user_information_Me";

    public interface OnGettinguser_information {
        void successfullyGettingInfo(user_information user);

        void serverException(String exception);

        void OnFailure(String exception);
    }


//get user information
    public interface get_user_information {
        @GET("api/Update/getUserInformation")
            // rout path method in c#
        Call<Status> get_user_information(@Query("user_id") int userid);
    }


    public static void getuser_info(int profile_user_id, final Get_user_information_Methods.OnGettinguser_information listener) {
        Log.d(TAG, "getProfileInfo: profile_user_id " + profile_user_id );

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        get_user_information get_user_information = retrofit.create(get_user_information.class);

        final Call<Status> ca = get_user_information.get_user_information(profile_user_id);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();

                if (response.isSuccessful() && status!=null){
                    if (status.getState() == 1) {
                        try {
                            Log.d(TAG, "onResponse: " + status);
                            JSONArray json = new JSONArray(status.getJson_data());

                            user_information user_information1 = new Gson().fromJson(json.get(0).toString().toString(), user_information.class);
                            listener.successfullyGettingInfo(user_information1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.OnFailure(e.getMessage());
                        }
                    } else if (status.getState() == 0) {
                        listener.serverException(0 + "  " + status.getException());

                    } else {
                        listener.serverException(-1 + "  " + status.getException());
                    }
                }else{
                    listener.OnFailure(response.message());
                }

            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.OnFailure(t.getMessage());
            }
        });

    }

}
