package com.example.testavocado.methods.post_comments_methods;

import android.content.Context;
import android.util.Log;

import com.example.testavocado.R;
import com.example.testavocado.objects.post_comment;
import com.example.testavocado.objects.result;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class post_comments_methods {
    public static final String TAG = "post_comments_methods";
    private Context mycontext;

    public post_comments_methods(Context mycontext) {
        this.mycontext = mycontext;
    }

    public post_comments_methods() {
    }
//---------------------------------------------------------------------------------------------------------------------->
    //method get all comments of the post selected by id
   static public void get_post_comments(final Context context1, int post_id, int startfrom, int rowsnum, final on_getting_post_comments_listner listner) {
        AsyncHttpClient myclient = new AsyncHttpClient();
        RequestParams myparam = new RequestParams();
        myparam.put("post_id", post_id);
        myparam.put("startfrom", startfrom);
        myparam.put("row_num", rowsnum);
        final result myresult1 = new result();

        myclient.get(context1.getString(R.string.httpclient_url) + context1.getString(R.string.get_post_comments),
                myparam, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.d(TAG, "onSuccess: ");
                        try {
                            Log.d(TAG, "onSuccess: ");
                            myresult1.setState(response.getString(context1.getString(R.string.State)));
                            myresult1.setException(response.getString(context1.getString(R.string.Exception)));
                            myresult1.setJson_data(response.getString(context1.getString(R.string.Json_data)));
                            Log.d(TAG, "onSuccess: " + myresult1);

                            switch (myresult1.getState()) {
                                case "1":
                                    Log.d(TAG, "onSuccess: ");
                                    ArrayList<post_comment> post_comments1 = new ArrayList<post_comment>();
                                    //get array json
                                    JSONArray jsonArray = new JSONArray(myresult1.getJson_data());
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(i);

                                        Gson gson = new Gson();
                                        post_comment comment = gson.fromJson(String.valueOf(jsonObject), post_comment.class);
                                        post_comments1.add(comment);
                                        Log.d(TAG, "onSuccess: " + comment);
                                    }
                                    listner.onsuccess_getting_post_comments(post_comments1);
                                    break;

                                case "0":
                                    Log.d(TAG, "onSuccess: " + myresult1.getException());
                                    listner.onserverException(myresult1.getException());
                                    break;

                                case "-1":
                                    listner.onserverException(myresult1.getException());
                                    break;
                            }


                        } catch (Exception ex) {
                            listner.onserverException(ex.getMessage());
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

    //method add new comment to post
   static public void add_comment(final Context context1, int post_id, int user_id, String comment_text, String comment_date,
                            final on_adding_post_comment listner) {
        AsyncHttpClient myclient = new AsyncHttpClient();
        RequestParams myparam = new RequestParams();
        myparam.put("post_id", post_id);
        myparam.put("userid", user_id);
        myparam.put("comment_text", comment_text);
        myparam.put("comment_date",comment_date);
        final result myresult1 = new result();

        myclient.get(context1.getString(R.string.httpclient_url) + context1.getString(R.string.add_post_comment),
                myparam, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.d(TAG, "onSuccess: ");
                        try {
                            myresult1.setState(response.getString(context1.getString(R.string.State)));
                            myresult1.setException(response.getString(context1.getString(R.string.Exception)));
                            myresult1.setJson_data(response.getString(context1.getString(R.string.Json_data)));
                            Log.d(TAG, "onSuccess: " + myresult1);

                            switch (myresult1.getState()) {
                                case "1":
                                    listner.onsuccess_adding_post_comment(myresult1.getState());
                                    break;

                                case "0":
                                    Log.d(TAG, "onSuccess: " + myresult1.getException());
                                    listner.onserverException(myresult1.getException());
                                    break;

                                case "-1":
                                    listner.onserverException(myresult1.getException());
                                    break;
                            }


                        } catch (Exception ex) {
                            listner.onserverException(ex.getMessage());
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
