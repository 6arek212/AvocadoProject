package com.example.testavocado.Connection;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.testavocado.Models.Friend;
import com.example.testavocado.Models.Status;
import com.example.testavocado.Models.UserAdd;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.NetworkClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class ConnectionsHandler {
    private static final String TAG = "ConnectionsHandler";


    public interface ConnectionsInterface {
        @GET("api/Connections/getInCommeingConnectionsRequest")
        Call<Status> getRequests(@Query("User_id") int user_id, @Query("offset") int offset, @Query("datetime") String datetime);

        @GET("api/Connections/searchUsersByName")
        Call<Status> searchUserByName(@Query("User_id") int user_id, @Query("Text_cmp") String txt_name, @Query("datetime") String datetime, @Query("offset") int offset);

        @GET("api/Connections/deleteFriendRequest")
        Call<Status> deleteRequest(@Query("Request_id") int request_id);


        @POST("api/Connections/deleteFriend")
        Call<Status> deleteFriend(@Query("Request_id") int request_id, @Query("u1") int u1, @Query("u2") int u2);


        @GET("api/Connections/sendFriendRequest")
        Call<Status> sendFriendRequest(@Query("Sender_id") int sender_id, @Query("Receiver_id") int receiver_id, @Query("Date_time_sent") String datetime);


        @GET("api/Connections/accepteFriendRequest")
        Call<Status> acceptFriendRequest(@Query("Request_id") int request_id, @Query("Date_time_accepted") String datetime);


        @GET("api/Location/getNearByUsers")
        Call<Status> getNearByUsers(@Query("user_id") int user_id, @Query("latitude") double latitude, @Query("longitude") double longitude, @Query("distance") int distance,
                                    @Query("text_cmp") String text_cmp, @Query("datetime") String datetime, @Query("offset") int offset);


        @GET("api/Connections/getFriends")
        Call<Status> getFriends(@Query("user_id") int user_id, @Query("offset") int offset, @Query("datetime") String datetime);

    }


    public interface OnGettingNearByUsersListener {
        public void onSuccessListener(List<UserAdd> userAddList);

        public void onServerException(String ex);

        public void onFailureListener(String ex);
    }


    public static void getNearByUsers(int user_id, double latitude, double longitude, int distance, String text_cmp, String datetime, int offset, final OnGettingNearByUsersListener listener) {
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        ConnectionsInterface interface1 = retrofit.create(ConnectionsInterface.class);

        final Call<Status> ca = interface1.getNearByUsers(user_id, latitude, longitude, distance, text_cmp, datetime, offset);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                final Status status = response.body();
                Log.d(TAG, "onResponse:  friend request sent  " + status);

                if (response.isSuccessful()) {
                    if (status.getState() == 1) {
                        Log.d(TAG, "onResponse: " + status);


                        try {
                            JSONArray jsonArray = new JSONArray(status.getJson_data());
                            List<UserAdd> list = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                list.add(new Gson().fromJson(jsonArray.get(i).toString(), UserAdd.class));
                            }

                            listener.onSuccessListener(list);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onFailureListener(e.getMessage());
                        }


                    } else if (status.getState() == 0) {
                        listener.onServerException(status.getException());
                    } else {
                        listener.onServerException(status.getException());
                    }
                } else {
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


    public interface OnStatusRegisterListener {
        public void onSuccessListener(int request_id);

        public void onServerException(String ex);

        public void onFailureListener(String ex);
    }


    public static void onSendingNewFriendRequest(int current_user_id, int receiver_id,
                                                 String current_date, final OnStatusRegisterListener listener) {
        Log.d(TAG, "onSendingNewFriendRequest: attempting to send new friend request sender" + current_user_id + "   rec" + receiver_id);


        Retrofit retrofit = NetworkClient.getRetrofitClient();
        ConnectionsInterface interface1 = retrofit.create(ConnectionsInterface.class);

        final Call<Status> ca = interface1.sendFriendRequest(current_user_id, receiver_id, current_date);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                Log.d(TAG, "onResponse:  friend request sent  " + status);

                if (status.getState() == 1) {
                    Log.d(TAG, "onResponse: " + status);

                    JSONObject json = null;
                    try {
                        json = new JSONObject(status.getJson_data());
                        int request_id = json.getInt("request_id");
                        //   RecyclerViewAddConnectionAdapter.userAddList.get(index).setRequest_id(request_id);
                        listener.onSuccessListener(request_id);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (status.getState() == 0) {
                    listener.onServerException(status.getException());
                } else {
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


    public interface OnGettingUsersByNameListener {
        public void onSuccessListener(List<UserAdd> list);

        public void onServer(String ex);

        public void onFailure(String ex);
    }


    public static void getUsersByName(final int user_id, String text_cmp, String datetime, int offset, final OnGettingUsersByNameListener listener) {
        Log.d(TAG, "getUsersByName:attemting to get users by name   user_id " + user_id + " text " + text_cmp);

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        ConnectionsInterface interface1 = retrofit.create(ConnectionsInterface.class);

        final Call<Status> ca = interface1.searchUserByName(user_id, text_cmp, datetime, offset);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                final Status status = response.body();
                Log.d(TAG, "onResponse: got users by name   " + status);

                if (response.isSuccessful()) {
                    if (status.getState() == 1) {
                        Log.d(TAG, "onResponse: " + status);
                        final List<UserAdd> list = new ArrayList<>();
                        JSONArray json = null;
                        try {
                            json = new JSONArray(status.getJson_data());
                            for (int i = 0; i < json.length(); i++) {
                                list.add(new Gson().fromJson(json.get(i).toString(), UserAdd.class));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        listener.onSuccessListener(list);
                    } else if (status.getState() == 0) {
                        listener.onServer(status.getException());
                    } else {
                        listener.onServer(status.getException());
                    }
                } else {
                    listener.onFailure(response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailure(t.getMessage());
            }
        });

    }


    public interface OnRemovingFriendRequestListener {
        public void onSuccessListener();

        public void onServer(String ex);

        public void onFailure(String ex);
    }


    public static void RemoveFriendRequest(int request_id, final OnRemovingFriendRequestListener listener) {
        Log.d(TAG, "RemoveFriendRequest:  attempting to remove friend request ");
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        ConnectionsInterface interface1 = retrofit.create(ConnectionsInterface.class);

        final Call<Status> ca = interface1.deleteRequest(request_id);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                Log.d(TAG, "onResponse: removed frined request   " + status);

                if (status.getState() == 1) {
                    listener.onSuccessListener();
                } else if (status.getState() == 0) {
                    listener.onServer(status.getException());
                } else {
                    listener.onServer(status.getException());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailure(t.getMessage());
            }
        });

    }


    public interface OnRemovingFriendListener {
        public void onSuccessListener();

        public void onServer(String ex);

        public void onFailure(String ex);
    }


    public static void RemoveFriend(int request_id,final int u1,final int u2, final OnRemovingFriendListener listener) {
        Log.d(TAG, "RemoveFriend:  attempting to remove friend  ");
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        ConnectionsInterface interface1 = retrofit.create(ConnectionsInterface.class);

        final Call<Status> ca = interface1.deleteFriend(request_id, u1, u2);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                Log.d(TAG, "onResponse: removed frined request   " + status);

                if (response.isSuccessful()) {
                    if (status.getState() == 1) {
                        listener.onSuccessListener();

                       final DatabaseReference fr= FirebaseDatabase.getInstance().getReference();
                        fr.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String chatId=dataSnapshot.child(String.valueOf(u1)).child("friends").child(String.valueOf(u2))
                                        .child("chatId").getValue(String.class);

                                fr.child("users").child(String.valueOf(u1)).child("friends").child(String.valueOf(u2)).removeValue();
                                fr.child("users").child(String.valueOf(u2)).child("friends").child(String.valueOf(u1)).removeValue();


                                if(chatId!=null){
                                    fr.child("users").child(String.valueOf(u1)).child("chats").child(chatId).removeValue();
                                    fr.child("users").child(String.valueOf(u2)).child("chats").child(chatId).removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {


                            }
                        });








                    } else if (status.getState() == 0) {
                        listener.onServer(status.getException());
                    } else {
                        listener.onServer(status.getException());
                    }
                } else {
                    listener.onFailure("failed");
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailure(t.getMessage());
            }
        });

    }


    public interface OnAcceptingFriendRequestListener {
        public void onSuccessListener();

        public void onServer(String ex);

        public void onFailure(String ex);
    }

    public static void acceptFriendRequest(int request_id, final int otherId ,final int userId, String date_time_accepted, final OnAcceptingFriendRequestListener listener) {
        Log.d(TAG, "acceptFriendRequest: attempting to accepting firnd request ");
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        ConnectionsInterface interface1 = retrofit.create(ConnectionsInterface.class);

        final Call<Status> ca = interface1.acceptFriendRequest(request_id, date_time_accepted);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                Log.d(TAG, "onResponse: accpted frined request   " + status);

                if (status.getState() == 1) {
                    listener.onSuccessListener();



                    final DatabaseReference fr= FirebaseDatabase.getInstance().getReference();
                    fr.child("users").child(String.valueOf(userId)).child("friends").child(String.valueOf(otherId))
                            .child("with").setValue(otherId);
                    fr.child("users").child(String.valueOf(otherId)).child("friends").child(String.valueOf(userId))
                            .child("with").setValue(userId);



                } else if (status.getState() == 0) {
                    listener.onServer(status.getException());
                } else {
                    listener.onServer(status.getException());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.onFailure(t.getMessage());
            }
        });

    }


    public interface OnGettingConnectionsRequestsListener {
        public void OnSuccessfullyGettingRequests(List<UserAdd> userAddList);

        public void OnServerException(String exception);

        public void OnFailure(String exception);

    }


    public static void getConnectionsRequest(final int user_id, int offset, String datetime, final Context context, final OnGettingConnectionsRequestsListener listener) {
        Log.d(TAG, "getConnectionsRequest: attempting to get firends requests ");
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        ConnectionsInterface interface1 = retrofit.create(ConnectionsInterface.class);

        final Call<Status> ca = interface1.getRequests(user_id, offset, datetime);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                Log.d(TAG, "onResponse: getting friends  requests   " + status);

                if (status.getState() == 1) {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(status.getJson_data());
                        Log.d(TAG, "onSuccess: json array size " + jsonArray.length());


                        List<UserAdd> userAddList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            String json = String.valueOf(jsonArray.getJSONObject(i));
                            userAddList.add(new Gson().fromJson(String.valueOf(json), UserAdd.class));
                        }

                        listener.OnSuccessfullyGettingRequests(userAddList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (status.getState() == 0) {
                    listener.OnServerException(status.getException());
                } else {
                    listener.OnServerException(status.getException());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.OnFailure(t.getMessage());
            }
        });
    }


    public interface OnUserFriendsListener {
        public void OnSuccessfullyGettingFriends(List<Friend> friends);

        public void OnServerException(String e);

        public void OnFailure(String e);
    }


    public static void getUserFriends(final int user_id, int offset, String datetime, final OnUserFriendsListener listener) {
        Log.d(TAG, "getUserFriends: attempting to get firends requests user_id " + user_id + "  offset " + offset + " datetime " + datetime);
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        ConnectionsInterface interface1 = retrofit.create(ConnectionsInterface.class);

        final Call<Status> ca = interface1.getFriends(user_id, offset, datetime);
        ca.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Status status = response.body();
                Log.d(TAG, "onResponse: getting friends  requests   " + status);

                if (status.getState() == 1) {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(status.getJson_data());
                        Log.d(TAG, "onSuccess: json array size " + jsonArray.length());


                        List<Friend> friends = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            String json = String.valueOf(jsonArray.getJSONObject(i));
                            friends.add(new Gson().fromJson(String.valueOf(json), Friend.class));
                        }

                        listener.OnSuccessfullyGettingFriends(friends);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (status.getState() == 0) {
                    listener.OnServerException(status.getException());
                } else {
                    listener.OnServerException(status.getException());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d(TAG, "onFailure: " + call + "  " + t.getMessage());
                listener.OnFailure(t.getMessage());
            }
        });
    }


}
