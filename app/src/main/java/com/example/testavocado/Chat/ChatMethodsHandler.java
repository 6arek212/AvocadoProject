package com.example.testavocado.Chat;

import android.content.Context;
import android.util.Log;

import com.example.testavocado.Connection.ConnectionsHandler;
import com.example.testavocado.Models.Chat;
import com.example.testavocado.Models.ChatUser;
import com.example.testavocado.Models.Message;
import com.example.testavocado.Models.Status;
import com.example.testavocado.R;
import com.example.testavocado.Utils.NetworkClient;
import com.example.testavocado.Utils.ServerConnection;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class ChatMethodsHandler {
    private static final String TAG = "ChatMethodsHandler";


    public interface ChatInterface {
        @GET("api/ChatMessages/GettingUserAndChats")
        Call<Status> getUserAndChats(@Query("User_id") int user_id, @Query("Username") String username,@Query("Offset") int offset);

        @GET("api/ChatMessages/CheckingCreatingChat")
        Call<Status> creatingChat(@Query("Sender_id") int sender_id, @Query("Receiver_id") int receiver_id,@Query("Datetime") String  datetime,@Query("Message") String message);


        @GET("api/ChatMessages/SendingMessage")
        Call<Status> sendMessage(@Query("Chat_id") int chat_id,@Query("User_id") int user_id,@Query("Message") String message,@Query("Datetime") String  datetime);


        @GET("api/ChatMessages/GettingChats")
        Call<Status> gettingChats(@Query("User_id") int user_id);


        @GET("api/ChatMessages/GettingUserChatsWithUpdatedValues")
        Call<Status> GettingUserChatsWithUpdatedValues(@Query("User_id") int user_id);


        @GET("api/ChatMessages/GettingUnreadedMessages")
        Call<Status> GettingUnreadedMessages(@Query("User_id") int user_id,@Query("Chat_id") int chat_id);

    }






    public interface OnSGettingChatsListener {
        public void onSuccessListener(List<Chat> chats);
        public void onServerException(String ex);
        public void onFailureListener(String ex);
    }

    public static void gettingChats(int user_id, final OnSGettingChatsListener listener) {

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        ChatInterface interface1=retrofit.create(ChatInterface.class);

        final Call<Status> ca=interface1.gettingChats(user_id) ;
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status=response.body();
                Log.d(TAG, "onResponse:  getting chats  "+status);

                if(status.getState()==1){
                    Log.d(TAG, "onResponse: "+status);
                    try {
                        JSONArray jsonArray = new JSONArray(status.getJson_data());
                        Log.d(TAG, "onSuccess: json array size " + jsonArray.length());

                        List<Chat> chats = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++)
                            chats.add(new Gson().fromJson(jsonArray.get(i).toString(),Chat.class));
                        Log.d(TAG, "onSuccess: " + chats.get(0));

                        listener.onSuccessListener(chats);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else if(status.getState()==0)
                {
                    listener.onServerException(status.getException());
                }else
                {
                    listener.onServerException(status.getException());
                }
            }
            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailureListener(t.getMessage());
            }
        });
    }


    /*
    private static void gettingChats(int user_id, final Context context, final OnRequestingChatsListener requestingChatsListener) {
        RequestParams params = new RequestParams();
        params.add(context.getString(R.string.user_id), String.valueOf(user_id));


        ServerConnection.get(context.getString(R.string.GettingChats), params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);


                Log.d(TAG, "onSuccess: connection done");
                Status status = new Gson().fromJson(String.valueOf(response), Status.class);

                try {
                    Log.d(TAG, "onSuccess: state = " + status.getState());

                    if (status.getState()==1) {
                        JSONArray jsonArray = new JSONArray(response.getString(context.getString(R.string.Json_data)));
                        Log.d(TAG, "onSuccess: json array size " + jsonArray.length());



                        List<Chat> chats = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++)
                            chats.add(new Gson().fromJson(jsonArray.get(i).toString(),Chat.class));
                        Log.d(TAG, "onSuccess: " + chats.get(0));


                        requestingChatsListener.OnSuccessfullyGettingChats(chats);


                    } else if (status.getState()==0) {
                        Log.d(TAG, "onSuccess: " + status.getException());
                        requestingChatsListener.OnServerException(status.getException());


                    } else {
                        Log.d(TAG, "onSuccess: " + status.getException());
                        requestingChatsListener.OnServerException(status.getException());

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "onSuccess: JSONException   " + e.getMessage());
                    requestingChatsListener.OnFailure(e.getMessage());

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                requestingChatsListener.OnFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                requestingChatsListener.OnFailure(throwable.getMessage());
            }
        });

    }*/




    public interface OnGettingUsersAndChatsListener {
        public void onSuccessListener(List<ChatUser> chats);
        public void onServerException(String ex);
        public void onFailureListener(String ex);
    }






    public static void OnGettingUserAndChats(int user_id, String text, int offset, final Context context, final OnGettingUsersAndChatsListener listener) {
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        ChatInterface interface1=retrofit.create(ChatInterface.class);

        final Call<Status> ca=interface1.getUserAndChats(user_id,text,offset) ;
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status=response.body();
                Log.d(TAG, "onResponse:  getting chats  "+status);

                if(status.getState()==1){
                    Log.d(TAG, "onResponse: "+status);
                    try {
                        JSONArray jsonArray = new JSONArray(status.getJson_data());
                        Log.d(TAG, "onSuccess: json array size " + jsonArray.length());

                        List<ChatUser> chatUsers =new ArrayList<>();

                        for(int i=0;i<jsonArray.length();i++){
                            chatUsers.add(new Gson().fromJson(jsonArray.get(i).toString(),ChatUser.class));
                        }

                        listener.onSuccessListener(chatUsers);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else if(status.getState()==0)
                {
                    listener.onServerException(status.getException());
                }else
                {
                    listener.onServerException(status.getException());
                }
            }
            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailureListener(t.getMessage());
            }
        });



    }


        /**
         * using it for when creating a chat
         *

         */

    /*
    public static void OnGettingUserAndChats(int user_id, String text, int offset, final Context context, final OnRequestingUserAndChatsListener listener) {
        RequestParams params = new RequestParams();

        params.add(context.getString(R.string.user_id), String.valueOf(user_id));
        params.add(context.getString(R.string.username), text);
        params.add(context.getString(R.string.offset), String.valueOf(offset));

        ServerConnection.get(context.getString(R.string.GettingUserAndChats), params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);


                Log.d(TAG, "onSuccess: connection done");
                Status status = new Gson().fromJson(String.valueOf(response), Status.class);

                try {
                    Log.d(TAG, "onSuccess: state = " + status.getState());

                    if (status.getState()==1) {
                        JSONArray jsonArray = new JSONArray(response.getString(context.getString(R.string.Json_data)));
                        Log.d(TAG, "onSuccess: json array size " + jsonArray.length());

                        List<ChatUser> chatUsers =new ArrayList<>();

                        for(int i=0;i<jsonArray.length();i++){
                            chatUsers.add(new Gson().fromJson(jsonArray.get(i).toString(),ChatUser.class));
                        }



                        listener.OnSuccessfullyGettingUsersAndChats(chatUsers);


                        Log.d(TAG, "onSuccess: " + chatUsers.get(0));


                    } else if (status.getState()==0) {
                        Log.d(TAG, "onSuccess: " + status.getException());
                        listener.OnServerException(status.getException());


                    } else {
                        Log.d(TAG, "onSuccess: " + status.getException());
                        listener.OnServerException(status.getException());

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "onSuccess: JSONException   " + e.getMessage());
                    listener.OnServerException(e.getMessage());

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                listener.OnFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                listener.OnFailure(throwable.getMessage());
            }
        });


    }*/


        public interface OnGettingUnreadMessagesListener {
            public void onSuccessListener(List<Message> messages);
            public void onServerException(String ex);
            public void onFailureListener(String ex);
        }

    public static void OnGettingUnreadedMessages(int user_id, int chat_id, final OnGettingUnreadMessagesListener listener) {

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        ChatInterface interface1=retrofit.create(ChatInterface.class);

        final Call<Status> ca=interface1.GettingUnreadedMessages(user_id,chat_id) ;
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status=response.body();
                Log.d(TAG, "onResponse:  getting chats  "+status);

                if(status.getState()==1){
                    Log.d(TAG, "onResponse: "+status);
                    try {
                        JSONArray jsonArray = new JSONArray(status.getJson_data());
                        Log.d(TAG, "onSuccess: json array size " + jsonArray.length());

                        List<Message> messages = new ArrayList<>();

                        for(int i=0;i<jsonArray.length();i++){
                            messages.add(new Gson().fromJson(jsonArray.get(i).toString(),Message.class));
                        }

                        listener.onSuccessListener(messages);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else if(status.getState()==0)
                {
                    listener.onServerException(status.getException());
                }else
                {
                    listener.onServerException(status.getException());
                }
            }
            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailureListener(t.getMessage());
            }
        });


    }


    /*
    public static void OnGettingUnreadedMessages(int user_id, int chat_id, final Context context, final OnRequestingUnrededMessagesListener listener) {

        RequestParams params = new RequestParams();

        params.add(context.getString(R.string.user_id), String.valueOf(user_id));
        params.add(context.getString(R.string.chat_id), String.valueOf(chat_id));


        ServerConnection.get(context.getString(R.string.GettingUnreadedMessages), params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);


                Log.d(TAG, "onSuccess: connection done");
                Status status = new Gson().fromJson(String.valueOf(response), Status.class);

                try {
                    Log.d(TAG, "onSuccess: state unread messages = " + status.getState());

                    if (status.getState()==1) {
                        JSONArray jsonArray = new JSONArray(response.getString(context.getString(R.string.Json_data)));
                        Log.d(TAG, "onSuccess: json array size " + jsonArray.length());

                        List<Message> messages = new ArrayList<>();

                        for(int i=0;i<jsonArray.length();i++){
                            messages.add(new Gson().fromJson(jsonArray.get(i).toString(),Message.class));
                        }


                        listener.OnSuccessfullyGettingUnrededMessages(messages);


                        Log.d(TAG, "onSuccess: " + messages.get(0));


                    } else if (status.getState()==0) {
                        Log.d(TAG, "onSuccess: " + status.getException());
                        listener.OnServerException(status.getException());


                    } else {
                        Log.d(TAG, "onSuccess: " + status.getException());
                        listener.OnServerException(status.getException());

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "onSuccess: JSONException   " + e.getMessage());
                    listener.OnServerException(e.getMessage());

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                listener.OnFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                listener.OnFailure(throwable.getMessage());
            }

            @Override
            public boolean getUseSynchronousMode() {
                return false;
            }
        });

    }*/




    public interface OnSendingMessageListener {
        public void onSuccessListener(int message_id);
        public void onServerException(String ex);
        public void onFailureListener(String ex);
    }

    public static void OnSendingMessage(int chat_id, int user_id, String message, String datetime, final OnSendingMessageListener listener) {

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        ChatInterface interface1=retrofit.create(ChatInterface.class);

        final Call<Status> ca=interface1.sendMessage(chat_id,user_id,message,datetime) ;
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status=response.body();
                Log.d(TAG, "onResponse:  sending chats  "+status);

                if(status.getState()==1){
                    try {
                        JSONObject json=new JSONObject(status.getJson_data());
                        int message_id=json.getInt("Message_id");

                        listener.onSuccessListener(message_id);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else if(status.getState()==0)
                {
                    listener.onServerException(status.getException());
                }else
                {
                    listener.onServerException(status.getException());
                }
            }
            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailureListener(t.getMessage());
            }
        });
    }




/*
    public static void OnSendingMessage(int chat_id, int user_id, String message, String datetime, final Context context, final OnSendingMessage listener) {

        RequestParams params = new RequestParams();

        params.add(context.getString(R.string.user_id), String.valueOf(user_id));
        params.add(context.getString(R.string.chat_id), String.valueOf(chat_id));
        params.add(context.getString(R.string.message), message);
        params.add(context.getString(R.string.datetime), datetime);


        ServerConnection.get(context.getString(R.string.SendingMessage), params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);


                Log.d(TAG, "onSuccess: connection done");


                try {
                    Status status = new Gson().fromJson(String.valueOf(response), Status.class);
                    Log.d(TAG, "onSuccess: state = " + status);

                    if (status.getState()==1) {

                        JSONObject json=new JSONObject(status.getJson_data());
                        int message_id = json.getInt(context.getString(R.string.message_id));
                        listener.OnSuccess(message_id);

                    } else if (status.getState()==0) {
                        Log.d(TAG, "onSuccess: " + status.getException());
                        listener.OnServerException(status.getException());


                    } else {
                        Log.d(TAG, "onSuccess: " + status.getException());
                        listener.OnServerException(status.getException());

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "onSuccess: JSONException   " + e.getMessage());
                    listener.OnServerException(e.getMessage());

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                listener.OnFailure(throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                listener.OnFailure(throwable.getMessage());
            }
        });

    }

*/



    public interface OnCheckingCreatingChatListener {
        public void onSuccessListener(int chat_id,int message_id);
        public void onServerException(String ex);
        public void onFailureListener(String ex);
    }

    public static void OnCheckingCreatingChat(int user_id,int receiver_id ,String message, String datetime, final Context context, final OnCheckingCreatingChatListener listener) {
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        ChatInterface interface1=retrofit.create(ChatInterface.class);

        final Call<Status> ca=interface1.creatingChat(user_id,receiver_id,datetime,message) ;
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status=response.body();
                Log.d(TAG, "onResponse:  getting chats  "+status);

                if(status.getState()==1){
                    Log.d(TAG, "onResponse: "+status);
                    try {
                        JSONObject json=new JSONObject(status.getJson_data());

                        int chat_id = json.getInt(context.getString(R.string.chat_id));
                        int message_id = json.getInt(context.getString(R.string.message_id));

                        listener.onSuccessListener(chat_id,message_id);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else if(status.getState()==0)
                {
                    listener.onServerException(status.getException());
                }else
                {
                    listener.onServerException(status.getException());
                }
            }
            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailureListener(t.getMessage());
            }
        });



    }

    /*

    public static void OnCheckingCreatingChat(int user_id,int receiver_id ,String message, String datetime, final Context context, final OnCreatingChat listener) {

        RequestParams params = new RequestParams();

        params.add(context.getString(R.string.sender_id), String.valueOf(user_id));
        params.add(context.getString(R.string.receiver_id), String.valueOf(receiver_id));
        params.add(context.getString(R.string.message), message);
        params.add(context.getString(R.string.datetime), datetime);


        ServerConnection.get(context.getString(R.string.CheckingCreatingChat), params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);


                Log.d(TAG, "onSuccess: connection done OnCheckingCreatingChat");


                try {
                    Status status = new Gson().fromJson(String.valueOf(response), Status.class);
                    Log.d(TAG, "onSuccess: state = " + status.getState());

                    if (status.getState()==1) {

                        JSONObject json=new JSONObject(status.getJson_data());


                        int chat_id = json.getInt(context.getString(R.string.chat_id));
                        int message_id = json.getInt(context.getString(R.string.message_id));



                        listener.OnSuccess(chat_id,message_id);

                    } else if (status.getState()==0) {
                        Log.d(TAG, "onSuccess: " + status.getException());
                        listener.OnServerException(status.getException());


                    } else {
                        Log.d(TAG, "onSuccess: " + status.getException());
                        listener.OnServerException(status.getException());

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "onSuccess: JSONException   " + e.getMessage());
                    listener.OnServerException(e.getMessage());

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                listener.OnFailure(throwable.getMessage()+"   "+errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                listener.OnFailure(throwable.getMessage()+"  "+responseString);
            }
        });




    }*/

}
