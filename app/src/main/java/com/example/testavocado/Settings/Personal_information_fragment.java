package com.example.testavocado.Settings;

import android.app.Activity;
import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testavocado.Models.Setting;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;

public class Personal_information_fragment extends Fragment {
    public static final String TAG = "Personal_information_fragment";
    private Context mcontext;
    private View myview;
    private TextView txtv_name,txtv_emailaddress,txtv_phonenumber;
    private LinearLayout linearLayout_name,linearLayout_email,linearLayout_phonenumber;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       myview=getLayoutInflater().inflate(R.layout.fragment_personal_iformation,container,false);
        return myview;
    }

    public void load_widgets()
    {
        txtv_name=(TextView)myview.findViewById(R.id.txtv_account_name);
        txtv_emailaddress=(TextView)myview.findViewById(R.id.txtv_account_emailaddress);
        txtv_phonenumber=(TextView)myview.findViewById(R.id.txtv_account_phonenumber);
        linearLayout_name=(LinearLayout)myview.findViewById(R.id.linear_layout_account_name);
        linearLayout_email=(LinearLayout)myview.findViewById(R.id.linear_layout_account_emailaddress);
        linearLayout_phonenumber=(LinearLayout)myview.findViewById(R.id.linear_layout_account_phonenumber);
        // set widgets values
        Setting setting1=HelpMethods.getSharedPreferences(mcontext);
        txtv_name.setText(setting1.getUser_first_name()+" "+setting1.getUser_last_name());
    }


    class onclick  implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.linear_layout_account_name :
                    Toast.makeText(mcontext, "test", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.linear_layout_account_emailaddress:
                    break;

                case R.id.linear_layout_account_phonenumber :
                    break;
            }
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mcontext = (Activity) context;
        }

    }
}
