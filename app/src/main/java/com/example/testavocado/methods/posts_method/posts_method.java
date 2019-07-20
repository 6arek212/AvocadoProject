package com.example.testavocado.methods.posts_method;

import android.content.Context;
import android.util.Log;

import com.example.testavocado.R;
import com.example.testavocado.fill_json_objects;
import com.example.testavocado.methods.help_methods.Help_methods;
import com.example.testavocado.objects.friends_posts;
import com.example.testavocado.objects.result;
import com.example.testavocado.objects.user_post;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class posts_method {
    public static final String TAG = "posts_method";
    private Context mycontext;

    //Ctor---------------------------------------------------------------------------------->

    public posts_method(Context mycontext) {
        this.mycontext = mycontext;
    }

    public posts_method() {
    }
    //Get Set------------------------------------------------------------------------------->

    public static String getTAG() {
        return TAG;
    }

    public Context getMycontext() {
        return mycontext;
    }

    public void setMycontext(Context mycontext) {
        this.mycontext = mycontext;
    }
    //--------------------------------------------------------------------------------------->

    //method add new post


    static public void add_new_post(final Context context1, user_post post, final on_create_new_post_listner listner) {
        AsyncHttpClient myclient = new AsyncHttpClient();

        RequestParams myparam = new RequestParams();

        myparam.put("userid", post.getUserid());
        myparam.put("post_text", post.getPost_text());
        myparam.put("image_url", post.getPost_imageurl());
        myparam.put("post_date", post.getPost_date());
        myparam.put("post_lastchange_date", post.getPostlastchange_date());
        myparam.put("how_can_see", post.getHow_can_see());

        Log.d(TAG, "add_new_post: date=" + post.getPost_date());
        Log.d(TAG, "add_new_post: date=" + post.getPostlastchange_date());

        myclient.get(context1.getString(R.string.httpclient_url) + context1.getString(R.string.add_newpost), myparam, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d(TAG, "onSuccess: ");
                result myresult1 = new result();
                try {
                    //fill object my result
                    myresult1.setState(response.getString(context1.getString(R.string.State)));
                    myresult1.setException(response.getString(context1.getString(R.string.Exception)));
                    myresult1.setJson_data(response.getString(context1.getString(R.string.Json_data)));

                    //1 if success  0 some wrong -1 error
                    if (myresult1.getState().equals("1")) {
                        listner.onsuccess_getting_myprofile_posts(myresult1.getState());
                    } else if (myresult1.getState().equals("0")) {
                        Log.d(TAG, "onSuccess: result=0 : " + myresult1.getException());
                        listner.onserverException(myresult1.getException());
                    } else if (myresult1.getState().equals("-1")) {
                        Log.d(TAG, "onSuccess: result=-1 : " + myresult1.getException());
                        listner.onserverException(myresult1.getException());
                    }
                } catch (Exception ex) {
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


    //method get myprofile posts
    static public void get_myprofile_posts(final Context context1, final ArrayList<user_post> posts_list, final on_getting_myprofile_posts listner) {
        Log.d(TAG, "get_myprofile_posts: ");
        AsyncHttpClient myclient = new AsyncHttpClient();

        RequestParams myparam = new RequestParams();
        myparam.put("userid", Help_methods.get_user_id_sharedprefernces(context1));
        myparam.put("startfrom", posts_list.size());
        myparam.put("rows_number", 10);

        final result myresult1 = new result();

        myclient.get(context1.getString(R.string.httpclient_url) + context1.getString(R.string.get_myposts), myparam, new JsonHttpResponseHandler() {
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
                            Log.d(TAG, "onSuccess: ");
                            //get array json
                            JSONArray jsonArray = new JSONArray(myresult1.getJson_data());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(i);
                                user_post post = fill_json_objects.fill_objectpost(jsonObject, context1);
                                posts_list.add(post);
                                Log.d(TAG, "onSuccess: " + post);
                            }
                            listner.onsuccess_getting_myprofile_posts(posts_list);
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


    static public void get_personprofile_posts(final Context context1, int userid, int statfrom , int type,
                                           int rows_number,final on_getting_personprofile_posts_listner listner) {
        Log.d(TAG, "get_myprofile_posts: ");
        AsyncHttpClient myclient = new AsyncHttpClient();

        RequestParams myparam = new RequestParams();
        myparam.put("userid",userid);
        myparam.put("startfrom",statfrom);
        myparam.put("rows_number", rows_number);
        myparam.put("type", type);

        final result myresult1 = new result();

        myclient.get(context1.getString(R.string.httpclient_url) + context1.getString(R.string.get_person_post_bytype),
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
                                    Log.d(TAG, "onSuccess: ");
                                    ArrayList<user_post>posts_list=new ArrayList<user_post>();
                                    //get array json
                                    JSONArray jsonArray = new JSONArray(myresult1.getJson_data());
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(i);
                                        user_post post = fill_json_objects.fill_objectpost(jsonObject, context1);
                                        posts_list.add(post);
                                        Log.d(TAG, "onSuccess: " + post);
                                    }
                                    listner.onsuccess_getting_personprofile_posts(posts_list);
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


    //method get person posts public and friends posts only
    static public void get_personprofile_posts_public_friends(final Context context1, int userid, int statfrom ,
                                               int rows_number,final on_getting_personprofile_posts_listner listner) {
        Log.d(TAG, "get_myprofile_posts: ");
        AsyncHttpClient myclient = new AsyncHttpClient();

        RequestParams myparam = new RequestParams();
        myparam.put("userid",userid);
        myparam.put("startfrom",statfrom);
        myparam.put("rows_number", rows_number);

        final result myresult1 = new result();

        myclient.get(context1.getString(R.string.httpclient_url) + context1.getString(R.string.get_person_posts_public_friends),
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
                                    Log.d(TAG, "onSuccess: ");
                                    ArrayList<user_post>posts_list=new ArrayList<user_post>();
                                    //get array json
                                    JSONArray jsonArray = new JSONArray(myresult1.getJson_data());
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(i);
                                        user_post post = fill_json_objects.fill_objectpost(jsonObject, context1);
                                        posts_list.add(post);
                                        Log.d(TAG, "onSuccess: " + post);
                                    }
                                    listner.onsuccess_getting_personprofile_posts(posts_list);
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

    //method get friends posts
    static public void get_friends_posts_(final Context context1, int userid, int statfrom ,
                                                              int rows_number,int type,final on_getting_friends_posts_listner listner) {
        Log.d(TAG, "get_friends_posts_: ");
        AsyncHttpClient myclient = new AsyncHttpClient();

        RequestParams myparam = new RequestParams();
        myparam.put("userid",userid);
        myparam.put("startfrom",statfrom);
        myparam.put("rowsnum", rows_number);
        myparam.put("type",type);

        final result myresult1 = new result();

        myclient.get(context1.getString(R.string.httpclient_url) + context1.getString(R.string.get_friends_posts),
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
                                    Log.d(TAG, "onSuccess: ");
                                    ArrayList<friends_posts>friends_postslist=new ArrayList<friends_posts>();
                                    //get array json
                                    JSONArray jsonArray = new JSONArray(myresult1.getJson_data());
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(i);
                                        Gson gson = new Gson();
                                        friends_posts post=gson.fromJson(String.valueOf(jsonObject), friends_posts.class);
                                        friends_postslist.add(post);
                                         Log.d(TAG, "onSuccess: " +post );
                                    }
                                    listner.onsuccess_getting_personprofile_posts(friends_postslist);
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
