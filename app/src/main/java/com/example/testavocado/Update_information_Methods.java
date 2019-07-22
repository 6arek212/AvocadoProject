package com.example.testavocado;

import android.util.Log;

import com.example.testavocado.Models.Status;
import com.example.testavocado.Profile.Bio_Methods;
import com.example.testavocado.Utils.NetworkClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Update_information_Methods {

    public static final String TAG = "Update_information_Meth";

    public interface on_first_last_name_updated {
        void onSuccessListener(int  result);
        void onServerException(String ex);
        void onFailureListener(String ex);
    }

    public interface update_first_lastname {
        @GET("api/Update/update_first_lastname") // rout path method in c#
        Call<Status> update_first_lastname(@Query("userid") int userid, @Query("firstname") String firstname,@Query("lastname") String lastname);
    }

    public static void Update_first_last_name(int userid, String firstname,String lastname, final Update_information_Methods.on_first_last_name_updated listener)
    {
        Retrofit retrofit= NetworkClient.getRetrofitClient();
        update_first_lastname bi=retrofit.create(update_first_lastname.class);

        final Call<Status> sa=bi.update_first_lastname(userid,firstname,lastname);

        sa.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status=response.body();

                if(status.getState()==1){
                    Log.d(TAG, "onResponse: "+status);
                    listener.onSuccessListener(status.getState());
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
