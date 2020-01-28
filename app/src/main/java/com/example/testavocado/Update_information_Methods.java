package com.example.testavocado;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.testavocado.Models.Status;
import com.example.testavocado.Profile.Bio_Methods;
import com.example.testavocado.Utils.NetworkClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class Update_information_Methods {

    public static final String TAG = "Update_information_Meth";

    public interface on_first_last_name_updated {
        void onSuccessListener(int result);

        void onServerException(String ex);

        void onFailureListener(String ex);
    }

    //update firstname
    public interface update_first_lastname {
        @GET("api/Update/update_first_lastname")
            // rout path method in c#
        Call<Status> update_first_lastname(@Query("userid") int userid, @Query("firstname") String firstname, @Query("lastname") String lastname);
    }

    public static void Update_first_last_name(final Context mcontext, int userid, String firstname, String lastname, final Update_information_Methods.on_first_last_name_updated listener) {
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        update_first_lastname bi = retrofit.create(update_first_lastname.class);

        final Call<Status> sa = bi.update_first_lastname(userid, firstname, lastname);

        sa.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                if (response.isSuccessful())
                {
                    if (status.getState() == 1)
                    {
                        Log.d(TAG, "onResponse: " + status);
                        listener.onSuccessListener(status.getState());
                    }
                    else if (status.getState() == 0)
                    {
                        listener.onServerException(status.getException());
                    }
                    else
                        {
                        listener.onFailureListener(status.getException());
                    }
                }
                else
                    Toast.makeText(mcontext, mcontext.getString(R.string.no_intrent_connection)+"", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailureListener(t.getMessage());
            }
        });
    }


    //update email address
    public interface update_emailaddress {
        @GET("api/Update/update_emailaddress")
            // rout path method in c#
        Call<Status> update_emailaddress(@Query("userid") int userid, @Query("emailaddress") String emailaddress,@Query("password") String password);
    }


    public static void Update_emailaddress(final Context mcontext, int userid, String emailaddress,String password,final Update_information_Methods.on_first_last_name_updated listener) {
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        update_emailaddress bi = retrofit.create(update_emailaddress.class);

        final Call<Status> sa = bi.update_emailaddress(userid, emailaddress,password);

        sa.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                if (response.isSuccessful())
                {
                    if (status.getState() == 1)
                    {
                        Log.d(TAG, "onResponse: " + status);
                        listener.onSuccessListener(status.getState());
                    }
                    else if (status.getState() == 0)
                    {
                        listener.onServerException(status.getException());
                    }
                    else
                    {
                        listener.onFailureListener(status.getException());
                    }
                }
                else
                    listener.onFailureListener(response.message());
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailureListener(t.getMessage());
            }
        });
    }


    //update phone number
    public interface update_phonenumber {
        @GET("api/Update/update_phonenumber")
            // rout path method in c#
        Call<Status> update_phonenumber(@Query("userid") int userid, @Query("phonenumber") String phonenumber);
    }


    public static void Update_phonenumber(final Context mcontext, int userid, String phonenumber,final Update_information_Methods.on_first_last_name_updated listener) {
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        update_phonenumber bi = retrofit.create(update_phonenumber.class);

        final Call<Status> sa = bi.update_phonenumber(userid, phonenumber);

        sa.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                if (response.isSuccessful())
                {
                    if (status.getState() == 1)
                    {
                        Log.d(TAG, "onResponse: " + status);
                        listener.onSuccessListener(status.getState());
                    }
                    else if (status.getState() == 0)
                    {
                        listener.onServerException(status.getException());
                    }
                    else
                    {
                        listener.onFailureListener(status.getException());
                    }
                }
                else
                    Toast.makeText(mcontext, mcontext.getString(R.string.no_intrent_connection)+"", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailureListener(t.getMessage());
            }
        });
    }


    //update password
    public interface update_password {
        @POST("api/Update/update_password")
            // rout path method in c#
        Call<Status> update_password(@Query("userid") int userid, @Query("current_password") String current_password, @Query("new_password") String new_password);
    }


    public static void Update_password(final Context mcontext, int userid, String current_password,String new_password,final Update_information_Methods.on_first_last_name_updated listener) {
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        update_password bi = retrofit.create(update_password.class);

        final Call<Status> sa = bi.update_password(userid,current_password,new_password);

        sa.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                if (response.isSuccessful())
                {
                    if (status.getState() == 1)
                    {
                        Log.d(TAG, "onResponse: " + status);
                        listener.onSuccessListener(status.getState());
                    }
                    else if (status.getState() == 0)
                    {
                        listener.onServerException(status.getException());
                    }
                    else
                    {
                        listener.onFailureListener(status.getException());
                    }
                }
                else
                    listener.onFailureListener(response.message());
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailureListener(t.getMessage());
            }
        });
    }

}
