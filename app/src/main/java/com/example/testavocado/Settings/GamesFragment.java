package com.example.testavocado.Settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testavocado.DamkaGame.DamkaActivity;
import com.example.testavocado.R;
import com.example.testavocado.XOGame.XOActivity;

public class GamesFragment extends Fragment {


    private ImageView mDamaka,mXO;
    private Context mContext;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chose_game,container,false);

        initWidgets(view);
        return view;
    }




    private void initWidgets(View view) {
        mContext=getContext();
        mDamaka=view.findViewById(R.id.damkaGame);
        mXO=view.findViewById(R.id.xoGame);


        mDamaka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, DamkaActivity.class));
            }
        });



        mXO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, XOActivity.class));
            }
        });
    }

}
