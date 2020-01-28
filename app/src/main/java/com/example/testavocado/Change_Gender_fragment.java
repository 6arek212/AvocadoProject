package com.example.testavocado;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Change_Gender_fragment extends Fragment {
    public static final String TAG = "Change_Gender_fragment";
    private Context mcontext;
    private View myview;
    private LinearLayout linearLayout_full1;
    private Button btn_save,btn_cancel;
    private ImageView imgv_arrow_back;
    private TextView txtv_title,txtv_title_gender_text;
    private RadioGroup mradioGroup;
    private RadioButton radio_btn_male, radio_btn_female,checked_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=getLayoutInflater().inflate(R.layout.fragment_gender_change,container,false);
        load_widgets();
        return myview;
    }

    public void load_widgets()
    {
        //set widgets
        linearLayout_full1=(LinearLayout)myview.findViewById(R.id.linearlayout_full_changegender);
        btn_save=(Button)myview.findViewById(R.id.btn_save_gender_update);
        btn_cancel=(Button)myview.findViewById(R.id.btn_cancel_gender_update);
        imgv_arrow_back=(ImageView)myview.findViewById(R.id.imgv_arrow_back_merge_topbar_back_arrow) ;
        txtv_title=(TextView)myview.findViewById(R.id.txtv_post_created_firstname_merge_topbar_back_arrow) ;
        txtv_title_gender_text=(TextView)myview.findViewById(R.id.txtv__merge_gender_change);
        radio_btn_male=(RadioButton)myview.findViewById(R.id.radio_btn_male_merge_gender_change);
        radio_btn_female=(RadioButton)myview.findViewById(R.id.radio_btn_female_merge_gender_change);
        mradioGroup=(RadioGroup)myview.findViewById(R.id.radiogrup_merge_gender_change);
        //set values
        txtv_title.setText("Gender Changer");
        txtv_title_gender_text.setText("Your Gender");

        //set onclick
        linearLayout_full1.setOnClickListener(new onclick());
        btn_save.setOnClickListener(new onclick());
        btn_cancel.setOnClickListener(new onclick());
        imgv_arrow_back.setOnClickListener(new onclick());
        //radio group------------------------------------------------------------------------------------>
        mradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checked_btn = (RadioButton) myview.findViewById(checkedId);
            }
        });
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

                case R.id.btn_save_gender_update:
                    if(checked_btn==null)
                    {
                        Log.d(TAG, "not selection ");
                        Toast.makeText(mcontext,  getString(R.string.select_your_gender)+"", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else
                        Toast.makeText(mcontext, checked_btn.getText()+"", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.btn_cancel_gender_update:
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
