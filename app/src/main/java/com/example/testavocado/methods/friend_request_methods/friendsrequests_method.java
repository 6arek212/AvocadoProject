package com.example.testavocado.methods.friend_request_methods;

import android.content.Context;
import android.util.Log;

import com.example.testavocado.R;
import com.example.testavocado.fill_json_objects;
import com.example.testavocado.objects.friends_requests;
import com.example.testavocado.objects.result;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class friendsrequests_method {
    public static final String TAG="friendsrequests_method";
    private Context mycontext;

    //---------------------------------------------------------------------------->
    public friendsrequests_method(Context mycontext) {
        this.mycontext = mycontext;
    }
    public friendsrequests_method()
    {
    }
    //------------------------------------------------------------------------------>

    public Context getMycontext() {
        return mycontext;
    }

    public void setMycontext(Context mycontext) {
        this.mycontext = mycontext;
    }
    //method----------------------------------------------------------------------------------->
    //method send friends request
     static public void send_freind_request (final Context mycontext1, int userid_sender, int userid_recipient,
                                     final on_sending_friendsrequest_listner listner)
    {
        AsyncHttpClient myclient=new AsyncHttpClient();
        RequestParams myparam=new RequestParams();

        myparam.put("userid_sender",userid_sender);
        myparam.put("userid_recipient",userid_recipient);
        myparam.put("friends_type",1);
        myclient.get(mycontext1.getString(R.string.httpclient_url)+mycontext1.getString(R.string.send_friend_request),myparam,
                new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        result myresult = new result();
                        try {
                            myresult.setState(response.getString(mycontext1.getString(R.string.State)));
                            myresult.setException(response.getString(mycontext1.getString(R.string.Exception)));
                            myresult.setJson_data(response.getString(mycontext1.getString(R.string.Json_data)));
                            switch (myresult.getState()) {
                                case "1":
                                    listner.onsuccess_sending_freind_request(myresult.getException());
                                    break;
                                case "0":
                                    listner.onserverException(myresult.getException());
                                    break;

                                case "-1":
                                    listner.onserverException(myresult.getException());
                                    break;
                            }
                        } catch (Exception ex) {
                            listner.onserverException(ex.getMessage().toString());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        listner.onFailure(throwable.getMessage());
                    }
                });

    }
    //method check friends type
    static public void check_friends_request_type(final Context mycontext1, int userid_sender, int userid_recipient,
                                                  final on_checking_freinds_request_type listner)
    {
        final AsyncHttpClient myclient=new AsyncHttpClient();
        RequestParams myparam=new RequestParams();

        myparam.put("userid_sender",userid_sender);
        myparam.put("userid_recipient",userid_recipient);

        myclient.get(mycontext1.getString(R.string.httpclient_url)+mycontext1.getString(R.string.check_if_request_sent),myparam,
                new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        result myresult = new result();
                        try {
                            myresult.setState(response.getString(mycontext1.getString(R.string.State)));
                            myresult.setException(response.getString(mycontext1.getString(R.string.Exception)));
                            myresult.setJson_data(response.getString(mycontext1.getString(R.string.Json_data)));
                            switch (myresult.getState()) {
                                case "1":

                                    ArrayList<friends_requests> requests_list=new ArrayList<friends_requests>();
                                    JSONArray jsonArray=new JSONArray(myresult.getJson_data());
                                    JSONObject jsonObject=new JSONObject();
                                    for (int i=0;i<jsonArray.length();i++)
                                    {
                                        jsonObject=jsonArray.getJSONObject(i);
                                        requests_list.add(fill_json_objects.fill_object_friends_requests(jsonObject));
                                        Log.d(TAG, "onSuccess: "+jsonObject);
                                    }
                                    listner.onsuccess_sending_freind_request(requests_list);

                                    break;
                                case "0":
                                    listner.onserverException(myresult.getState());
                                    break;

                                case "-1":
                                    listner.onserverException(myresult.getException());
                                    break;
                            }
                        } catch (Exception ex) {
                            listner.onserverException(ex.getMessage().toString());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        listner.onFailure(throwable.getMessage());
                    }
                });

    }

    static public void delete_friends_request(final Context mycontext1, int userid_sender, int userid_recipient,
                                                  final on_deleteing_friend_request_listner listner)
    {
        final AsyncHttpClient myclient=new AsyncHttpClient();
        RequestParams myparam=new RequestParams();

        myparam.put("userid_sender",userid_sender);
        myparam.put("userid_recipient",userid_recipient);

        myclient.get(mycontext1.getString(R.string.httpclient_url)+mycontext1.getString(R.string.delete_friend_request),myparam,
                new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        result myresult = new result();
                        try {
                            myresult.setState(response.getString(mycontext1.getString(R.string.State)));
                            myresult.setException(response.getString(mycontext1.getString(R.string.Exception)));
                            myresult.setJson_data(response.getString(mycontext1.getString(R.string.Json_data)));
                            switch (myresult.getState()) {
                                case "1":
                                    listner.onsuccess_deleting_freind_request(myresult.getException());

                                    break;
                                case "0":
                                    listner.onserverException(myresult.getState());
                                    break;

                                case "-1":
                                    listner.onserverException(myresult.getException());
                                    break;
                            }
                        } catch (Exception ex) {
                            listner.onserverException(ex.getMessage().toString());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        listner.onFailure(throwable.getMessage());
                    }
                });

    }

    //change freinds request
    static public void updatefriends_request(final Context mycontext1, int userid_sender, int userid_recipient,int friends_type,
                                              final on_updating_friends_reques_listner listner)
    {
        final AsyncHttpClient myclient=new AsyncHttpClient();
        RequestParams myparam=new RequestParams();

        myparam.put("userid_sender",userid_sender);
        myparam.put("userid_recipient",userid_recipient);
        myparam.put("friends_type",friends_type);

        myclient.get(mycontext1.getString(R.string.httpclient_url)+mycontext1.getString(R.string.update_friend_request),myparam,
                new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        result myresult = new result();
                        try {
                            myresult.setState(response.getString(mycontext1.getString(R.string.State)));
                            myresult.setException(response.getString(mycontext1.getString(R.string.Exception)));
                            myresult.setJson_data(response.getString(mycontext1.getString(R.string.Json_data)));
                            switch (myresult.getState()) {
                                case "1":
                                    listner.onsuccess_updating_freind_request(myresult.getException());

                                    break;
                                case "0":
                                    listner.onserverException(myresult.getState());
                                    break;

                                case "-1":
                                    listner.onserverException(myresult.getException());
                                    break;
                            }
                        } catch (Exception ex) {
                            listner.onserverException(ex.getMessage().toString());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        listner.onFailure(throwable.getMessage());
                    }
                });

    }
    //get friends requests
    static public void get_friends_requests(final Context mycontext1, int userid_recipient,final on_getting_friends_requests_listner listner)
    {
        final AsyncHttpClient myclient=new AsyncHttpClient();
        RequestParams myparam=new RequestParams();

        myparam.put("userid_recipient",userid_recipient);

        myclient.get(mycontext1.getString(R.string.httpclient_url)+mycontext1.getString(R.string.get_all_friends_requests),myparam,
                new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        result myresult = new result();
                        try {
                            myresult.setState(response.getString(mycontext1.getString(R.string.State)));
                            myresult.setException(response.getString(mycontext1.getString(R.string.Exception)));
                            myresult.setJson_data(response.getString(mycontext1.getString(R.string.Json_data)));
                            switch (myresult.getState()) {
                                case "1":
                                    ArrayList<friends_requests> friendsrequests_list=new ArrayList<friends_requests>();
                                    JSONArray jsonArray=new JSONArray(myresult.getJson_data());
                                    JSONObject jsonobject;
                                    friends_requests requests1;
                                    for (int i=0;i<jsonArray.length();i++)
                                    {
                                        jsonobject=jsonArray.getJSONObject(i);
                                        Gson gson = new Gson();
                                         requests1 = gson.fromJson(String.valueOf(jsonobject), friends_requests.class);
                                       friendsrequests_list.add(requests1);
                                    }
                                    listner.onsuccess_geeting_friends_requests(friendsrequests_list);
                                    break;
                                case "0":
                                    listner.onserverException(myresult.getState());
                                    break;

                                case "-1":
                                    listner.onserverException(myresult.getException());
                                    break;
                            }
                        } catch (Exception ex) {
                            listner.onserverException(ex.getMessage().toString());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        listner.onFailure(throwable.getMessage());
                    }
                });

    }
}
