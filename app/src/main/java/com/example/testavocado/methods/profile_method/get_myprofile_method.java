package com.example.testavocado.methods.profile_method;

import android.content.Context;
import android.util.Log;

import com.example.testavocado.R;
import com.example.testavocado.fill_json_objects;
import com.example.testavocado.objects.result;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class get_myprofile_method {
    public static final String TAG = "get_myprofile_method";

    private Context context;
//---------------------------------------------------------------------------------->
    public get_myprofile_method(Context context) {
        this.context = context;
    }
    public  get_myprofile_method()
    {}
    //---------------------------------------------------------------------------------->
    public static String getTAG() {
        return TAG;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    //---------------------------------------------------------------------------------->

    static private AsyncHttpClient myclient=new AsyncHttpClient();
    static private RequestParams myparam=new RequestParams();

     static public void get_myprofile_byid(final Context context1, int userid, final on_getting_myprofile_byid listner)
    {
        myparam.put("userid",userid);

        myclient.get(context1.getString(R.string.httpclient_url)+context1.getString(R.string.get_myprofile_byid),myparam,new JsonHttpResponseHandler()
        {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                super.onSuccess(statusCode, headers, response);
                Log.d(TAG, "onSuccess: ");
                result myresult1 = new result();
                try {
                    myresult1.setState(response.getString(context1.getString(R.string.State)));
                    myresult1.setException(response.getString(context1.getString(R.string.Exception)));
                    myresult1.setJson_data(response.getString(context1.getString(R.string.Json_data)));

                    if (myresult1.getState().equals("1"))
                    {
                        JSONArray array=new JSONArray(myresult1.getJson_data());
                        JSONObject myobject=new JSONObject();
                        myobject=array.getJSONObject(0);
                        listner.onsuccess_getting_myprofile_byid(fill_json_objects.fill_object_myprofile(myobject));

                    }
                    else if (myresult1.getState().equals("0"))
                    {
                        Log.d(TAG, "onSuccess: " + myresult1.getException());
                        listner.onserverException(myresult1.getException());
                    }
                } catch (Exception ex) {
                    Log.d(TAG, "onSuccess: exciption" + ex.getMessage().toString());
                    listner.onserverException(myresult1.getException());
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
