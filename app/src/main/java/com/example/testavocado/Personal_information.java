package com.example.testavocado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testavocado.Models.Setting;
import com.example.testavocado.Settings.SettingsFragment;
import com.example.testavocado.Utils.HelpMethods;

public class Personal_information extends AppCompatActivity {

    public static final String TAG = "Personal_information";

    private Context mcontext=Personal_information.this;
    private TextView txtv_name,txtv_emailaddress,txtv_phonenumber;
    private LinearLayout linearLayout_name,linearLayout_email,linearLayout_phonenumber,linearLayout_changepassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        load_widgets();
    }

//aa
    public void load_widgets()
    {
        txtv_name=(TextView)findViewById(R.id.txtv_account_name);
        txtv_emailaddress=(TextView)findViewById(R.id.txtv_account_emailaddress);
        txtv_phonenumber=(TextView)findViewById(R.id.txtv_account_phonenumber);
        linearLayout_name=(LinearLayout)findViewById(R.id.linear_layout_account_name);
        linearLayout_email=(LinearLayout)findViewById(R.id.linear_layout_account_emailaddress);
        linearLayout_phonenumber=(LinearLayout)findViewById(R.id.linear_layout_account_phonenumber);
        linearLayout_changepassword=(LinearLayout)findViewById(R.id.linear_layout_account_Changepassword);
        //set on click
        linearLayout_name.setOnClickListener(new onclick());
        linearLayout_email.setOnClickListener(new onclick());
        linearLayout_phonenumber.setOnClickListener(new onclick());
        linearLayout_changepassword.setOnClickListener(new onclick());
        // set widgets values
        Setting setting1= HelpMethods.getSharedPreferences(mcontext);
        txtv_name.setText(setting1.getUser_first_name()+" "+setting1.getUser_last_name());

    }


    class onclick  implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.linear_layout_account_name :
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.constrainlayout1, new change_first_last_name_fragment())
                            .addToBackStack("done").commit();
                    break;

                case R.id.linear_layout_account_emailaddress:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.constrainlayout1, new change_emailaddress_fragment())
                            .addToBackStack("done").commit();
                    break;

                case R.id.linear_layout_account_phonenumber :
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.constrainlayout1, new phonenumber_edit_fragment())
                            .addToBackStack("done").commit();
                    break;
                case R.id.linear_layout_account_Changepassword :
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.constrainlayout1, new Change_password_fragment())
                            .addToBackStack("done").commit();
                    break;
            }
        }
    }
}
