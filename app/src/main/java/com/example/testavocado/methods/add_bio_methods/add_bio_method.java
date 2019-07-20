package com.example.testavocado.methods.add_bio_methods;

import android.content.Context;
import android.util.Log;

import com.example.testavocado.R;
import com.example.testavocado.objects.result;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class add_bio_method {

    public static final String TAG = "add_bio_method";

    private Context context;
    //--------------------------------------------------------------------------------->
    public add_bio_method(Context context) {
        this.context = context;
    }

    public add_bio_method()
    {

    }
    //--------------------------------------------------------------------------------->

    public static String getTAG() {
        return TAG;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    //--------------------------------------------------------------------------------->
  static   AsyncHttpClient mycleint=new AsyncHttpClient();
  static   RequestParams myparam=new RequestParams();

   static public void add_bio_myprofile(final Context context1, int userid, String bio_text, final on_adding_bio listner)
    {
        //add values to myparam
        myparam.put("userid",userid);
        myparam.put("bio_text",bio_text);
        //1 success profile find and add
        //0 profile not found
        //-1 exception
        mycleint.get(context1.getString(R.string.httpclient_url)+context1.getString(R.string.update_bio_myprofile),myparam,new JsonHttpResponseHandler()
        {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d(TAG, "onSuccess: ");
                result myresult1 = new result();
                try {
                    myresult1.setState(response.getString(context1.getString(R.string.State)));
                    myresult1.setException(response.getString(context1.getString(R.string.Exception)));
                    myresult1.setJson_data(response.getString(context1.getString(R.string.Json_data)));

                    if (myresult1.getState().equals("1")) {

                        listner.onsuccess_adding_bio( myresult1.getState());
                    } else if (myresult1.getState().equals("0")) {
                        Log.d(TAG, "onSuccess: " + myresult1.getException());
                        listner.onserverException(myresult1.getException());
                    }
                } catch (Exception ex) {
                    Log.d(TAG, "onSuccess: exciption" + ex.getMessage().toString());
                    listner.onserverException(ex.getMessage().toString());

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.d(TAG, "onFailure: " + throwable.getMessage().toString());
                listner.onFailure(throwable.getMessage().toString());
            }
        });
    }
}
