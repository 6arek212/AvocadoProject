package com.example.testavocado.methods.messages_methods;

import android.content.Context;
import android.util.Log;

import com.example.testavocado.R;
import com.example.testavocado.objects.Messages_obj;
import com.example.testavocado.objects.messages_contacts;
import com.example.testavocado.objects.result;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class messages_methods {
    public static final String TAG = "messages_methods";
    private Context mycontext;

    public messages_methods(Context mycontext) {
        this.mycontext = mycontext;
    }

    //when person send message to another person will add the another person to messages contact list (the all of contacts when u send messages)
    static public void add_contacts_messages_list(final Context mycontext1, int userid_sender, int userid_recipient,
                                                  String date, final on_adding_contact_message_list_listner listner) {
        AsyncHttpClient myclient = new AsyncHttpClient();
        RequestParams myparam = new RequestParams();
        myparam.put("userid_sender", userid_sender);
        myparam.put("userid_recipient", userid_recipient);
        myparam.put("date", date);

        myclient.get(mycontext1.getString(R.string.httpclient_url) + mycontext1.getString(R.string.add_contact_mymessages_list)
                , myparam, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        result myresult = new result();
                        Log.d(TAG, "onSuccess: ");
                        try {
                            myresult.setState(response.getString(mycontext1.getString(R.string.State)));
                            myresult.setException(response.getString(mycontext1.getString(R.string.Exception)));
                            myresult.setJson_data(response.getString(mycontext1.getString(R.string.Json_data)));
                            Log.d(TAG, "onSuccess:myresult=" + myresult.getException());
                            switch (myresult.getState()) {
                                case "1":
                                    listner.onsuccess_adding_contact_message_list(myresult.getException());

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
                            Log.d(TAG, "onSuccess: catch ex=" + ex.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        listner.onFailure(throwable.getMessage());
                        Log.d(TAG, "onFailure: exception=" + throwable.getMessage());
                    }
                });
    }

    //method get list contacts where messages sent
    static public void get_contacts_messages_list(final Context mycontext1, int userid_sender, final on_getting_list_contacts_messages_listner listner) {
        final AsyncHttpClient myclient = new AsyncHttpClient();
        RequestParams myparam = new RequestParams();
        myparam.put("userid_sender", userid_sender);


        myclient.get(mycontext1.getString(R.string.httpclient_url) + mycontext1.getString(R.string.get_contacts_mymessages_list)
                , myparam, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        result myresult = new result();
                        Log.d(TAG, "onSuccess: ");
                        try {
                            myresult.setState(response.getString(mycontext1.getString(R.string.State)));
                            myresult.setException(response.getString(mycontext1.getString(R.string.Exception)));
                            myresult.setJson_data(response.getString(mycontext1.getString(R.string.Json_data)));
                            Log.d(TAG, "onSuccess:myresult=" + myresult.getException());
                            switch (myresult.getState()) {
                                case "1":
                                    ArrayList<messages_contacts> contacts_list = new ArrayList<messages_contacts>();
                                    JSONArray array = new JSONArray(myresult.getJson_data());
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        Gson gson = new Gson();
                                        messages_contacts contacts = gson.fromJson(String.valueOf(object), messages_contacts.class);
                                        contacts_list.add(contacts);
                                        Log.d(TAG, "onSuccess:" + contacts);
                                    }
                                    listner.onsuccess_getting_contacts_list_messages(contacts_list);

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
                            Log.d(TAG, "onSuccess: catch ex=" + ex.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        listner.onFailure(throwable.getMessage());
                        Log.d(TAG, "onFailure: exception=" + throwable.getMessage());
                    }
                });
    }


    //function get messages
    static public void get_chat_messages(final Context mycontext1, int userid_sender,int userid_recipient,
                                         final on_getting_chat_messages_listner listner) {

        final AsyncHttpClient myclient = new AsyncHttpClient();
        RequestParams myparam = new RequestParams();
        myparam.put("userid_sender", userid_sender);
        myparam.put("userid_recipient", userid_recipient);

        myclient.get(mycontext1.getString(R.string.httpclient_url) + mycontext1.getString(R.string.get_chat_messages)
                , myparam, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        result myresult = new result();
                        Log.d(TAG, "onSuccess: ");
                        try {
                            myresult.setState(response.getString(mycontext1.getString(R.string.State)));
                            myresult.setException(response.getString(mycontext1.getString(R.string.Exception)));
                            myresult.setJson_data(response.getString(mycontext1.getString(R.string.Json_data)));
                            Log.d(TAG, "onSuccess:myresult=" + myresult.getException());
                            switch (myresult.getState()) {
                                case "1":
                                    ArrayList<Messages_obj> messages_list = new ArrayList<Messages_obj>();
                                    JSONArray array = new JSONArray(myresult.getJson_data());
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        Gson gson = new Gson();
                                        Messages_obj message = gson.fromJson(String.valueOf(object), Messages_obj.class);
                                        messages_list.add(message);
                                    }
                                    listner.onsuccess_on_getting_chat_messages(messages_list);

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
                            Log.d(TAG, "onSuccess: catch ex=" + ex.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        listner.onFailure(throwable.getMessage());
                        Log.d(TAG, "onFailure: exception=" + throwable.getMessage());
                    }
                });
    }

    //function send message to person
    static public void send_chat_message(final Context mycontext1, int userid_sender,int userid_recipient,
                                         String message_text,String message_date,final on_sendding_chat_message_listner listner) {

        final AsyncHttpClient myclient = new AsyncHttpClient();
        RequestParams myparam = new RequestParams();
        myparam.put("userid_sender", userid_sender);
        myparam.put("userid_recipient", userid_recipient);
        myparam.put("message_text", message_text);
        myparam.put("message_date", message_date);

        myclient.get(mycontext1.getString(R.string.httpclient_url) + mycontext1.getString(R.string.send_chat_message)
                , myparam, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        result myresult = new result();
                        Log.d(TAG, "onSuccess: ");
                        try {
                            myresult.setState(response.getString(mycontext1.getString(R.string.State)));
                            myresult.setException(response.getString(mycontext1.getString(R.string.Exception)));
                            myresult.setJson_data(response.getString(mycontext1.getString(R.string.Json_data)));
                            Log.d(TAG, "onSuccess:myresult=" + myresult.getException());
                            switch (myresult.getState()) {
                                case "1":
                                    listner.onsuccess_sendding_chat_message(myresult.getState());
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
                            Log.d(TAG, "onSuccess: catch ex=" + ex.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        listner.onFailure(throwable.getMessage());
                        Log.d(TAG, "onFailure: exception=" + throwable.getMessage());
                    }
                });
    }
}
