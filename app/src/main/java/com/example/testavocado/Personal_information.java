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
import com.example.testavocado.objects.user_information;

public class Personal_information extends AppCompatActivity {

    public static final String TAG = "Personal_information";

    private Context mcontext=Personal_information.this;
    private TextView txtv_name,txtv_emailaddress,txtv_phonenumber,txtv_birthdate,txtv_gender;
    private LinearLayout linearLayout_name,linearLayout_email,linearLayout_phonenumber,linearLayout_changepassword;
    private LinearLayout linearLayout_birthdate_change,linearLayout_Gender_changer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        load_widgets();

    }
    // set values (my email my phone number ....)
    public void fill_user_information()
    {
        //get user infromation
        Get_user_information_Methods.getuser_info(HelpMethods.get_userid_sharedp(mcontext), new Get_user_information_Methods.OnGettinguser_information() {
            @Override
            public void successfullyGettingInfo(user_information user) {
                txtv_emailaddress.setText(""+user.getUser_email());
                txtv_phonenumber.setText(""+user.getUser_phonenumber());
                txtv_birthdate.setText(""+user.getUser_birth_date());
                txtv_gender.setText(""+user.getUser_gender());
            }

            @Override
            public void serverException(String exception) {

            }

            @Override
            public void OnFailure(String exception) {

            }
        });
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
        linearLayout_birthdate_change=(LinearLayout)findViewById(R.id.linear_layout_account_Changebirthdate);
        linearLayout_Gender_changer=(LinearLayout)findViewById(R.id.linear_layout_account_ChangeGender);
        txtv_birthdate=(TextView)findViewById(R.id.txtv_birthdate_modify);
        txtv_gender=(TextView)findViewById(R.id.txtv_Gender_modify);
        //set on click
        linearLayout_name.setOnClickListener(new onclick());
        linearLayout_email.setOnClickListener(new onclick());
        linearLayout_phonenumber.setOnClickListener(new onclick());
        linearLayout_changepassword.setOnClickListener(new onclick());
        linearLayout_birthdate_change.setOnClickListener(new onclick());
        linearLayout_Gender_changer.setOnClickListener(new onclick());
        // set widgets values
        Setting setting1= HelpMethods.getSharedPreferences(mcontext);
        txtv_name.setText(setting1.getUser_first_name()+" "+setting1.getUser_last_name());

        fill_user_information();

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

                    //birthdate change
                case R.id.linear_layout_account_Changebirthdate :
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.constrainlayout1, new change_age_fragment())
                            .addToBackStack("done").commit();
                    break;

                case R.id.linear_layout_account_ChangeGender:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.constrainlayout1, new Change_Gender_fragment())
                            .addToBackStack("done").commit();

                    break;
            }
        }
    }
}
