package com.example.testavocado.Settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testavocado.R;

public class SavedPostsFragment extends Fragment {


    //widgets
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_saved_posts,container,false);

        initWidgets(view);



        return view;
    }




    private void initWidgets(View view) {
        mRecyclerView=view.findViewById(R.id.recyclerView);

    }


}
