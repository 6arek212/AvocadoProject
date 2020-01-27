package com.example.testavocado;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class change_age_fragment extends Fragment {
    public static final String TAG = "change_age_fragment";
    private View myview;
    private Context mcontext;
    private ImageView img_arrow;
    private Button btn_save,btn_cancel;
    private LinearLayout linearLayout_full1;
    private TextView txt_title;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=getLayoutInflater().inflate(R.layout.fragment_change_age,container,false);
        load_widgets();
        return myview;
    }

    public void load_widgets()
    {
        //set widgets
        linearLayout_full1=(LinearLayout)myview.findViewById(R.id.linearlayout_full_changeage);
        img_arrow=(ImageView)myview.findViewById(R.id.imgv_arrow_back_merge_topbar_back_arrow);
        txt_title=(TextView)myview.findViewById(R.id.txtv_post_created_firstname_merge_topbar_back_arrow);

        //set values
        txt_title.setText("Birth date modify");

        //set on click
        linearLayout_full1.setOnClickListener(new onclick());
        img_arrow.setOnClickListener(new onclick());
    }

    class onclick implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.imgv_arrow_back_merge_topbar_back_arrow:
                    close_all_fragments();
                    break;
            }
        }
    }

    //closeing all of the fragment in backstack
    public void close_all_fragments() {
        getFragmentManager().popBackStack();

    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mcontext = (Activity) context;
            Log.d(TAG, "onAttach: " + mcontext);
        }
    }
}
