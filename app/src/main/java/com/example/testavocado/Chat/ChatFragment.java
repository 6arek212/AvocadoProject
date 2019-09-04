package com.example.testavocado.Chat;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testavocado.Models.Chat;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;

import java.util.List;

import static com.example.testavocado.Chat.ChatActivity.SQL_VER;

public class ChatFragment extends Fragment {
    private static final String TAG = "ChatFragment";



    //widgets
    private RecyclerView mRecyclerView;
    private ShowChatsRecyclerAdapter adapter;
    private TextView mNewChat,mNoChats;


    //vars
    private Context mContext;
    private int current_user_id;
    private SQLiteMethods db;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chat,container,false);

        initWidgets(view);

        return view;
    }

    private void initWidgets(View view) {
        mContext = getContext();
        current_user_id = HelpMethods.checkSharedPreferencesForUserId(mContext);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mNoChats=view.findViewById(R.id.noChats);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        adapter = new ShowChatsRecyclerAdapter(mContext,current_user_id);
        mNewChat = view.findViewById(R.id.newChat);
        db=new SQLiteMethods(mContext,"db1",null,SQL_VER);

        mRecyclerView.setAdapter(adapter);


        Log.d(TAG, "initWidgets: chat from sqlite "+db.getChatsFromSQL().size());
        adapter.addChatSet(db.getChatsFromSQL());


        db.getChatsFromDb(current_user_id, new OnRequestingChatsListener() {
            @Override
            public void OnSuccessfullyGettingChats(List<Chat> chats) {
                Log.d(TAG, "OnSuccessfullyGettingChats: ");
                adapter.addChatSet(chats);
            }

            @Override
            public void OnServerException(String exception) {
                Log.d(TAG, "OnServerException: "+exception);

            }

            @Override
            public void OnFailure(String exception) {
                Log.d(TAG, "OnFailure: "+exception);

            }
        });



        mNewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewChatFragment fragment = new NewChatFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.mainLayoutChat, fragment);
                transaction.addToBackStack(mContext.getString(R.string.new_chat_fragment));
                transaction.commit();
            }
        });


        if(adapter.getItemCount()==0){
            mNoChats.setVisibility(View.VISIBLE);
        }else{
            mNoChats.setVisibility(View.GONE);
        }

    }








    @Override
    public void onStop() {
        super.onStop();
        db.close();
    }

    public void readCountUpdate(){
        adapter.OnUpdate();
    }
}
