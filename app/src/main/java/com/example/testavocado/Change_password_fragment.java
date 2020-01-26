package com.example.testavocado;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Change_password_fragment extends Fragment {
    private static final String TAG = "Change_password_fragmen";
    private View myview;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=getLayoutInflater().inflate(R.layout.fragment_change_password,container,false);
        load_widgets();
        return myview;
    }

    // load all of widgets
    public void load_widgets()
    {

    }

}
