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
import retrofit2.http.POST;
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

        @POST("api/ChatMessages/deleteChat")
        Call<Status> deleteChat(@Query("user_id") int user_id,@Query("chat_id") int chat_id);
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










    public interface OnDeletingChatListener {
        public void onDeleted();
        public void onServerException(String ex);
        public void onFailureListener(String ex);
    }

    public static void OnDeletingChat(int user_id,int chat_id, final OnDeletingChatListener listener) {
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        ChatInterface interface1=retrofit.create(ChatInterface.class);

        final Call<Status> ca=interface1.deleteChat(user_id,chat_id) ;
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status=response.body();
                Log.d(TAG, "onResponse:  getting chats  "+status);

                if(response.isSuccessful()){
                    if(status.getState()==1){
                        Log.d(TAG, "onResponse: "+status);
                            listener.onDeleted();
                    }
                    else if(status.getState()==0)
                    {
                        listener.onServerException(status.getException());
                    }else
                    {
                        listener.onServerException(status.getException());
                    }
                }
                else{
                    listener.onFailureListener(response.message());
                }
            }
            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailureListener(t.getMessage());
            }
        });



    }








}
