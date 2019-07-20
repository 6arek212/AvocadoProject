package com.example.testavocado.Chat;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.testavocado.Models.ChatUser;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;

import java.util.List;

public class NewChatFragment  extends Fragment {
    private static final String TAG = "NewChatFragment";




    //widgets

    private TextView mClose;
    private EditText mSearchName;
    private SwipeRefreshLayout mSwipe;
    private RecyclerView mRecyclerView;

    //vars
    private int current_user_id;
    private Context mContext;
    private NewChatRecyclerAdapter adapter;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private static boolean loading;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_new_chat,container,false);
        initWidgets(view);


        return view;
    }




    private void initWidgets(View view) {
        mSearchName=view.findViewById(R.id.searchName);
        mClose=view.findViewById(R.id.close);
        mRecyclerView=view.findViewById(R.id.recyclerView);
        mSwipe=view.findViewById(R.id.swipe);
        mContext=getContext();
        adapter=new NewChatRecyclerAdapter(mContext);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL
                , false));        mRecyclerView.setAdapter(adapter);
        current_user_id= HelpMethods.checkSharedPreferencesForUserId(mContext);
        recyclerViewBottomDetectionListener();

        getUsers(0);


        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsers(0);
            }
        });



        mSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getUsers(0);

            }
        });


        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }







    /**
     * Attaching a listener to detect when the list reached the bottom
     */
    private void recyclerViewBottomDetectionListener() {
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView
                .getLayoutManager();


        mRecyclerView
                .addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView,
                                           int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        totalItemCount = linearLayoutManager.getItemCount();
                        lastVisibleItem = linearLayoutManager
                                .findLastVisibleItemPosition();
                        if (!loading
                                && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                            // End has been reached
                            // Do something

                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    getUsers(adapter.getItemCount());
                                }
                            });


                            loading = true;
                        }
                    }
                });
    }





    private void getUsers(final int offset){
        if(offset==0) {
            adapter.clearList();
            adapter.is_endOfPosts=false;
            adapter.clearList();
            loading = true;
        }

        adapter.addNull();

        ChatMethodsHandler.OnGettingUserAndChats(current_user_id, mSearchName.getText().toString(), offset, mContext, new ChatMethodsHandler.OnGettingUsersAndChatsListener() {
            @Override
            public void onSuccessListener(List<ChatUser> chats) {
                loading = false;
                adapter.removeProg();
                adapter.addUsers(chats,adapter.getItemCount());
                mSwipe.setRefreshing(false);


                if(mSearchName.getText().toString().isEmpty()&&offset!=0)
                    adapter.clearList();

                Log.d(TAG, "OnSuccessfullyGettingUsersAndChats: added users for new chat");
            }

            @Override
            public void onServerException(String ex) {
                Log.d(TAG, "OnServerException: error server exceptino "+ex);
                adapter.removeProg();
                mSwipe.setRefreshing(false);

                if (!adapter.is_endOfPosts) {
                    adapter.is_endOfPosts = true;
                    adapter.addNull();
                }
            }

            @Override
            public void onFailureListener(String ex) {
                Log.d(TAG, "OnFailure: error "+ex);
            //    Snackbar.make(mRequestLayout, getString(R.string.CHECK_INTERNET), Snackbar.LENGTH_SHORT).show();

                adapter.removeProg();
                adapter.clearList();
                mSwipe.setRefreshing(false);

                if (!adapter.is_endOfPosts) {
                    adapter.is_endOfPosts = true;
                    adapter.addNull();
                }
            }
        });

    }




    /**
     * RecyclerView LinearLayout Manager
     */
    public class WrapContentLinearLayoutManager extends LinearLayoutManager {


        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TAG", "meet a IOOBE in RecyclerView");
            }
        }
    }

}
