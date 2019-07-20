package com.example.testavocado.methods;

import android.content.Context;
import android.util.Log;

import com.example.testavocado.R;
import com.example.testavocado.fill_json_objects;
import com.example.testavocado.methods.help_methods.Help_methods;
import com.example.testavocado.objects.result;
import com.example.testavocado.objects.user;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class login_method {

    public static final String TAG = "login_method";
    private Context mycontext;

    public login_method(Context mycontext) {
        this.mycontext = mycontext;
    }


    static public void userlogin(final Context mycontext1, final String emil, final String password, final on_logging_listner listner) {
        AsyncHttpClient myclient = new AsyncHttpClient();
        RequestParams myparam = new RequestParams();
        myparam.add("emil", emil);
        myparam.add("password", password);


        myclient.get(mycontext1.getString(R.string.httpclient_url) + mycontext1.getString(R.string.loginuser), myparam,
                new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.d(TAG, "onSuccess: ");
                        result myresult1 = new result();
                        try {
                            myresult1.setState(response.getString(mycontext1.getString(R.string.State)));
                            myresult1.setException(response.getString(mycontext1.getString(R.string.Exception)));
                            myresult1.setJson_data(response.getString(mycontext1.getString(R.string.Json_data)));

                            if (myresult1.getState().equals("1")) {
                                Log.d(TAG, "onSuccess: ");
                                //get array json
                                JSONArray jsonArray = new JSONArray(myresult1.getJson_data());
                                JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(0);
                                int user_id = jsonObject.getInt("User_id");
                                Log.d(TAG, "onSuccess: userid= " + user_id);
                                //save user_id sahredprefernces
                                Help_methods.save_user_id(user_id, mycontext1);
                                //save emil and password sharedprefernces
                                Help_methods.save_emil_password(emil, password, mycontext1);
                                Log.d(TAG, "onSuccess: emil=" + emil + " password=" + password);
                                //fill myuser object
                                user myuser = fill_json_objects.fill_json_objects_user(jsonObject, mycontext1);
                                //pass myuser object to interface
                                listner.onsuccess_getting_loginuser(myuser);
                                Log.d(TAG, "onSuccess: myuserobject=" + myuser);


                            } else if (myresult1.getState().equals("0")) {
                                Log.d(TAG, "onSuccess: exception="+myresult1.getException());
                                listner.onserverException(myresult1.getException());
                            }
                        } catch (JSONException ex) {
                            Log.d(TAG, "onSuccess: exception="+ex.getMessage());
                            listner.onserverException(myresult1.getException());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d(TAG, "onFailure: exception="+throwable.getMessage());
                        listner.onFailure(throwable.getMessage().toString());

                    }
                });


    }
}
