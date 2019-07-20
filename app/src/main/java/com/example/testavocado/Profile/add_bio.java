package com.example.testavocado.Profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testavocado.BaseActivity;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.methods.add_bio_methods.add_bio_method;
import com.example.testavocado.methods.add_bio_methods.on_adding_bio;
import com.example.testavocado.methods.help_methods.Help_methods;
import com.example.testavocado.methods.users_method.on_getting_users_listner;
import com.example.testavocado.methods.users_method.users_method;
import com.example.testavocado.objects.user;
import com.google.android.material.textfield.TextInputLayout;

public class add_bio extends AppCompatActivity {
    public static final String TAG = "add_bio";
    private Context mycontext = add_bio.this;
    private Button btn_save;
    private ImageButton btn_arrowback;
    private TextView add_bio_logo, txtv_first_name, txtv_last_name;
    private EditText edtxt_bio_text;
    private TextInputLayout inputlayout_editxt_bio_text;
    private user myuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_add_bio);
        load_widgets();
    }

    class onclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: ");
            switch (v.getId()) {
                case R.id.btn_save_merge_activity_add_bio_topbar:
                    
                    final String bio_text = edtxt_bio_text.getText().toString();
                    if (bio_text.length() <= 110) {
                        if (!bio_text.isEmpty())
                        {
                            int userid1=HelpMethods.get_userid_sharedp(mycontext);
                            Bio_Methods.UpdateBio(userid1, bio_text, new Bio_Methods.on_bio_updated() {
                                @Override
                                public void onSuccessListener(int result) {
                                    Intent myintent = new Intent();
                                    myintent.putExtra("bio", bio_text);
                                    setResult(RESULT_OK, myintent);
                                    Toast.makeText(mycontext, "sucess", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Help_methods.hideKeyboard((Activity) mycontext);
                                }

                                @Override
                                public void onServerException(String ex) {
                                    Toast.makeText(mycontext, ex+"", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailureListener(String ex) {
                                    Toast.makeText(mycontext, R.string.no_intrent_connection+"", Toast.LENGTH_SHORT).show();
                                }
                            });



                        } else
                            Toast.makeText(mycontext, "Bio cant be empty", Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(mycontext, "Only 110 charcter", Toast.LENGTH_SHORT).show();
                    
                    break;

                case R.id.imgv_merge_activity_add_bio_topbar:
                    String bio_text1 = edtxt_bio_text.getText().toString();
                    Intent myintent = new Intent();
                    myintent.putExtra("bio", bio_text1);
                    setResult(RESULT_CANCELED, myintent);
                    finish();
                    Help_methods.hideKeyboard((Activity) mycontext);
                    break;
            }
        }
    }

    public void load_widgets() {
        Log.d(TAG, "load_widgets: ");
        // set widgets
        btn_save = (Button) findViewById(R.id.btn_save_merge_activity_add_bio_topbar);
        btn_save.setVisibility(View.GONE);
        btn_arrowback = (ImageButton) findViewById(R.id.imgv_merge_activity_add_bio_topbar);
        add_bio_logo = (TextView) findViewById(R.id.txtv_addbio_merge_activity_add_bio_topbar);
        txtv_first_name = (TextView) findViewById(R.id.first_name_merge_activit_addbio_center);
        txtv_last_name = (TextView) findViewById(R.id.last_name_merge_activit_addbio_center);
        edtxt_bio_text = (EditText) findViewById(R.id.edtxt_bio_text_merge_activit_addbio_center);
        inputlayout_editxt_bio_text = (TextInputLayout) findViewById(R.id.textinputlayount_1_merge_activit_addbio_center);
        //set on click----------------------------------------------------------------------------->
        btn_save.setOnClickListener(new onclick());
        btn_arrowback.setOnClickListener(new onclick());
        edtxt_bio_text.addTextChangedListener(edtxt);
        //get some data from mvc----------------------------------------------------------------------------------->
        //set first and last name
        String []name=HelpMethods.get_user_name_sharedprefernces(mycontext).split(" ");
        txtv_first_name.setText(HelpMethods.get_user_name_sharedprefernces(mycontext));
        txtv_last_name.setText("");
        int userid = Help_methods.get_user_id_sharedprefernces(mycontext);
        /*users_method.getuser_by_userid(userid, mycontext, new on_getting_users_listner() {
            @Override
            public void onSuccess_getting_user_byid(user user1) {
                myuser = user1;
                Log.d(TAG, "onSuccess_getting_user_byid: myuser=" + myuser);
                txtv_first_name.setText(myuser.getUser_firstname());
                txtv_last_name.setText(myuser.getUser_lastname());
            }

            @Override
            public void onserverException(String exception) {
                Log.d(TAG, "onserverException: exception=" + exception);
                myuser = null;
                Toast.makeText(mycontext, exception + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String exception) {
                Log.d(TAG, "onFailure: exception=" + exception);
                myuser = null;
                Toast.makeText(mycontext, "Check interent!", Toast.LENGTH_SHORT).show();
            }
        });
        */
    }

    private TextWatcher edtxt = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String text = s.toString().trim();
            if (text.isEmpty())
                btn_save.setVisibility(View.GONE);
            else
                btn_save.setVisibility(View.VISIBLE);

            if (edtxt_bio_text.length() > 110)
                inputlayout_editxt_bio_text.setError("Only 110 charcter");
            else
                inputlayout_editxt_bio_text.setError(null);


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


}
