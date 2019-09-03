package com.example.testavocado;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hbb20.CountryCodePicker;

public class phonenumber_edit_fragment extends Fragment {
    public static final String TAG = "phonenumber_edit_fragme";
    private Context mcontext;
    private View myview;
    private TextView txtv_title;
    private ImageView imgv_arrow_back;
    private CountryCodePicker countryCodePicker1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=getLayoutInflater().inflate(R.layout.fragment_phonenumber_edit,container,false);
        load_widgets();
        return myview;
    }

    public void load_widgets()
    {
        txtv_title=(TextView)myview.findViewById(R.id.txtv_post_created_firstname_merge_topbar_back_arrow);
        imgv_arrow_back=(ImageView)myview.findViewById(R.id.imgv_arrow_back_merge_topbar_back_arrow);
        countryCodePicker1=(CountryCodePicker)myview.findViewById(R.id.coutrycodepicker1) ;
        //set on click
        imgv_arrow_back.setOnClickListener(new onclick());
        countryCodePicker1.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                Toast.makeText(mcontext, "Updated " + countryCodePicker1.getSelectedCountryName(), Toast.LENGTH_SHORT).show();
            }
        });

        //set values
        txtv_title.setText(getString(R.string.phonenumber_title)+"");
    }


    class onclick implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.imgv_arrow_back_merge_topbar_back_arrow:
                    close_all_fragments();
                    break;
            }
        }
    }

    //closeing all of the fragment in backstack
    public void close_all_fragments()
    {
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
