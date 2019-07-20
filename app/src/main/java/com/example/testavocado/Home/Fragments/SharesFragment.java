package com.example.testavocado.Home.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testavocado.Home.PostMethods;
import com.example.testavocado.Home.adapters.LikesAdapter;
import com.example.testavocado.Models.Like;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;

import java.util.List;

public class SharesFragment extends Fragment {
    private static final String TAG = "SharesFragment";



    private RecyclerView recyclerView;


    //vars
    private Context mContext;
    private LikesAdapter adapter;
    private  int user_id;//TODO !!!!!!!!!!!



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_shares,container,false);

        initWidgets(view);

        return view;
    }






    private void initWidgets(View view) {
        mContext=getContext();
        user_id= HelpMethods.checkSharedPreferencesForUserId(mContext);
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new LikesAdapter(getContext(),user_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        getShares();
    }







    private void getShares(){
        PostMethods.getPostShares(LikesDislikesFragment.post_id, LikesDislikesFragment.datetime, adapter.getItemCount(), new PostMethods.OnGettingSharesListener() {
            @Override
            public void OnGettingLikes(List<Like> likeList) {
                Log.d(TAG, "OnGettingLikes: got shares ");
                adapter.addLikesSet(likeList,adapter.getItemCount());
            }

            @Override
            public void OnServerException(String ex) {
                Log.d(TAG, "OnServerException: "+ex);
            }

            @Override
            public void OnFailure(String ex) {
                Log.d(TAG, "OnFailure: "+ex);
            }
        });
    }


}
