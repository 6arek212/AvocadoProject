package com.example.testavocado.methods.users_method;

import android.content.Context;
import android.util.Log;

import com.example.testavocado.R;
import com.example.testavocado.fill_json_objects;
import com.example.testavocado.objects.result;
import com.example.testavocado.objects.user;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class users_method {
    public static final String TAG = "users_method";
    private Context context;

    //Ctor--------------------------------------------------------------------------->
    public users_method(Context context) {
        this.context = context;
    }

    public users_method() {
    }
    //set get--------------------------------------------------------------------------->


    public static String getTAG() {
        return TAG;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    //methods---------------------------------------------------------------------------->
    static private AsyncHttpClient mycleint = new AsyncHttpClient();
    static private RequestParams myparam = new RequestParams();


    // method get user json object by id
    static public void getuser_by_userid(int userid, final Context context1, final on_getting_users_listner listner) {
        myparam.put("userid", userid);
        Log.d(TAG, "getuser_by_userid: userid=" + userid);
        mycleint.get(context1.getString(R.string.httpclient_url) + context1.getString(R.string.get_user_by_userid), myparam, new JsonHttpResponseHandler() {

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
                        Log.d(TAG, "onSuccess: ");
                        //get array json
                        JSONArray jsonArray = new JSONArray(myresult1.getJson_data());
                        JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(0);
                        user myuser = fill_json_objects.fill_json_objects_user(jsonObject, context1);
                        // after fill the user insert into interface mthod
                        listner.onSuccess_getting_user_byid(myuser);

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

                Log.d(TAG, "onFailure: " +throwable.getMessage().toString());
                listner.onFailure(throwable.getMessage().toString());
            }
        });
    }


    static public void get_allusers_search(String searchtext, final Context mycontext1, final on_getting_users_startwith_search_listner listner)
    {
        myparam=new RequestParams();
        myparam.put("text",searchtext);

        mycleint.get(mycontext1.getString(R.string.httpclient_url)+mycontext1.getString(R.string.get_allusers_startwith),myparam,new JsonHttpResponseHandler()
        {
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
                        JSONArray jsonArray=new JSONArray(myresult1.getJson_data());
                        JSONObject jsonObject=new JSONObject();
                        ArrayList<user> user_list=new ArrayList<user>();
                        for (int i=0;i<jsonArray.length();i++)
                        {
                          jsonObject=jsonArray.getJSONObject(i);
                          user_list.add(fill_json_objects.fill_json_objects_user(jsonObject,mycontext1));
                        }
                        listner.onSuccess_getting_user_byid(user_list);

                    } else if (myresult1.getState().equals("0")) {
                        Log.d(TAG, "onSuccess:exception=" + myresult1.getException());
                        listner.onserverException(myresult1.getState());


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
