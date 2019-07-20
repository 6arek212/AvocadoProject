package com.example.testavocado.methods.add_newuser;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.testavocado.R;
import com.example.testavocado.objects.result;
import com.example.testavocado.objects.user;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class add_new_user_method {

    public static final String TAG = "add_new_user_method";
    private Context mycontext;

    //----------------------------------------->

    public add_new_user_method(Context mycontext) {
        this.mycontext = mycontext;
    }

    public add_new_user_method() {
    }

    //---------------------------------------------------------------------------------->

    public static String getTAG() {
        return TAG;
    }

    public Context getMycontext() {
        return mycontext;
    }

    public void setMycontext(Context mycontext) {
        this.mycontext = mycontext;
    }

    //-add new user method----------------------------------------------------------------->
    //function add new user
    static public void add_new_user(user newuser, final Context mycontext1, final on_add_new_user listner) {
        Log.d(TAG, "add_new_user: ");
        AsyncHttpClient myclient = new AsyncHttpClient();
        RequestParams myparam = new RequestParams();
        // fill the iformtion of the new user
        myparam.put("firstname", newuser.getUser_firstname());
        myparam.put("lastname", newuser.getUser_lastname());
        myparam.put("emil", newuser.getUser_emil());
        myparam.put("password", newuser.getUser_password());
        myparam.put("birthday", newuser.getUser_birthday());
        myparam.put("gender", newuser.getUser_gender());

        myclient.get(mycontext1.getString(R.string.httpclient_url) + mycontext1.getString(R.string.add_new_user), myparam, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d(TAG, "onSuccess: sucess");
                result myresult = new result();
                try {
                    myresult.setState(response.getString(mycontext1.getString(R.string.State)));
                    myresult.setException(response.getString(mycontext1.getString(R.string.Exception)));
                    myresult.setJson_data(response.getString(mycontext1.getString(R.string.Json_data)));
                    switch (myresult.getState()) {
                        case "1":
                            Toast.makeText(mycontext1, mycontext1.getString(R.string.succuess_registred)+"", Toast.LENGTH_SHORT).show();
                            listner.onsuccess_adding_new_user();
                            break;
                        case "0":
                            listner.onserverException(myresult.getException());
                            break;

                        case "-1":
                            Log.d(TAG, "onSuccess: " + myresult.getException());
                            listner.onserverException(myresult.getException());
                            break;
                    }
                } catch (Exception ex) {
                    Toast.makeText(mycontext1, "error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                    listner.onserverException(ex.getMessage().toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.d(TAG, "onFailure: Fail"+throwable.getMessage().toString());
                listner.onFailure(throwable.getMessage().toString());
            }
        });
    }


}
