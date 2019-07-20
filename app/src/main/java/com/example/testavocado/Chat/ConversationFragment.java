package com.example.testavocado.Chat;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testavocado.Models.ChatUser;
import com.example.testavocado.Models.Message;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.TimeMethods;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.testavocado.Chat.ChatActivity.SQL_VER;

public class ConversationFragment extends Fragment {
    private static final String TAG = "ConversationFragment";



    //widgets
    private TextView mUsername,mClose;
    private CircleImageView mProfileImage;
    private RecyclerView mRecyclerView;
    private ConversationRecycerAdapter adapter;
    private EditText mText_to_send;
    FloatingActionButton mSend;


    //vars
    public ChatUser chatUser;
    private SQLiteMethods db;
    private Context mContext;
    private int current_user_id;
    private boolean chat_id_created;
    private mAsync async;
    private boolean asyncOn = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        mContext = getContext();

        initWidgets(view);


        return view;
    }

    private void initWidgets(View view) {
        mUsername = view.findViewById(R.id.userName);
        mProfileImage = view.findViewById(R.id.profileImage);
        mSend = view.findViewById(R.id.send);
        mRecyclerView = view.findViewById(R.id.recyclerViewMessages);
        mText_to_send = view.findViewById(R.id.text_send);
        mClose=view.findViewById(R.id.close);


        Glide.with(mContext)
                .load(chatUser.getProfile_photo())
                .centerCrop()
                .error(R.drawable.error)
                .into(mProfileImage);

        mUsername.setText(chatUser.getUser_first_name() + " " + chatUser.getUser_last_name());
        current_user_id = HelpMethods.checkSharedPreferencesForUserId(mContext);
        adapter = new ConversationRecycerAdapter(current_user_id);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        chat_id_created = (chatUser.getChat_id() != -1 ? true : false);

        db = new SQLiteMethods(mContext, "db1", null, SQL_VER);
        adapter.setMessagesList(new ArrayList<Message>());
        async = new mAsync();

        if (chat_id_created) {
            adapter.setMessagesList(db.GetMessages(chatUser.getChat_id(), 0));
            Log.d(TAG, "initWidgets: sqllist size " + db.GetMessages(chatUser.getChat_id(), 0).size());
            asyncOn = true;
            async.execute();
            //updateListener.OnReadCountUpdate();
        }


        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                async.cancel(true);
                asyncOn=false;
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        OnSendClick();
    }


    public class mAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {


            while (asyncOn) {

                try {
                    Log.d(TAG, "doInBackground: getting new messages AsyncTask");
                    publishProgress();
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            getUpdatedMessages();
        }
    }






    private void getUpdatedMessages(){
        db.OnGettingUnreadMessages(current_user_id, chatUser.getChat_id(), new OnRequestingUnrededMessagesListener() {
            @Override
            public void OnSuccessfullyGettingUnrededMessages(List<Message> messages) {
                Log.d(TAG, "OnSuccessfullyGettingUnrededMessages: got new messages ");

                for (int i = 0; i < messages.size(); i++) {
                    messages.get(i).setMessage_datetime(TimeMethods.convertDateTimeFormat(TimeMethods.getNewLocalDate(messages.get(i).getMessage_datetime())));
                    adapter.addMessageTop(messages.get(i));
                }

            }

            @Override
            public void OnServerException(String exception) {
                Log.d(TAG, "OnServerException: no messages or error  "+exception);

            }

            @Override
            public void OnFailure(String exception) {
                Log.d(TAG, "OnFailure: error gettin unread messages "+exception);

            }
        });

    }









    public void OnSendClick() {
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String text = mText_to_send.getText().toString();

                if (text.trim().isEmpty()) {
                    Toast.makeText(mContext, "cant send empty message", Toast.LENGTH_SHORT).show();


                } else {
                    final String time = TimeMethods.getUTCdatetimeAsString();


                    if (!chat_id_created) {


                        ChatMethodsHandler.OnCheckingCreatingChat(current_user_id, chatUser.getUser_id(),
                                text, time, mContext, new ChatMethodsHandler.OnCheckingCreatingChatListener() {
                                    @Override
                                    public void onSuccessListener(int chat_id, int message_id) {
                                        Log.d(TAG, "OnSuccess: successfully created the chat ");

                                        Message message = new Message();
                                        message.setMessage_id(message_id);
                                        message.setMessage_text(text);
                                        message.setMessage_datetime(time);
                                        message.setMessage_sender_id(current_user_id);

                                        db.insetMessage(message, chat_id);
                                        chatUser.setChat_id(chat_id);
                                        message.setMessage_datetime(TimeMethods.convertDateTimeFormat(TimeMethods.getNewLocalDate(time)));
                                        adapter.addMessageTop(message);
                                        chat_id_created = true;
                                        asyncOn = true;
                                        async.execute();
                                    }

                                    @Override
                                    public void onServerException(String ex) {
                                        Log.d(TAG, "OnServerException: error while creating the chat "+ex);

                                    }

                                    @Override
                                    public void onFailureListener(String ex) {
                                        Log.d(TAG, "OnFailure: error while creating the chat "+ex);

                                    }
                                });


                    } else {
                        Log.d(TAG, "onClick: sending messagge "+chatUser.getChat_id()+"   "+current_user_id+"   "+chatUser.getUser_id()+"   "+text);


                        ChatMethodsHandler.OnSendingMessage(chatUser.getChat_id(), current_user_id, text, time, new ChatMethodsHandler.OnSendingMessageListener() {
                            @Override
                            public void onSuccessListener(int message_id) {
                                Log.d(TAG, "OnSuccess: message sent");
                                Message message = new Message();
                                message.setMessage_id(message_id);
                                message.setMessage_text(text);
                                message.setMessage_datetime(time);
                                message.setMessage_sender_id(current_user_id);


                                db.insetMessage(message, chatUser.getChat_id());
                                message.setMessage_datetime(TimeMethods.convertDateTimeFormat(TimeMethods.getNewLocalDate(time)));

                               // message.setMessage_datetime(TimeMethods.convertDateTimeFormat(time));
                                adapter.addMessageTop(message);
                                Toast.makeText(mContext, "sent  " + adapter.getItemCount(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onServerException(String ex) {
                                Log.d(TAG, "OnServerException: error while sendding a message "+ex);

                            }

                            @Override
                            public void onFailureListener(String ex) {
                                Log.d(TAG, "OnFailure: error while sendding a message "+ex);

                            }
                        });

                    }
                    mText_to_send.setText("");
                }
            }
        });

    }






    @Override
    public void onStop() {
        super.onStop();
        asyncOn=false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        asyncOn=false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        asyncOn=false;
    }
}
