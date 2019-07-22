package com.example.testavocado.user_login_register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.testavocado.Login.RegisterMethods;
import com.example.testavocado.Models.Setting;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.TimeMethods;
import com.example.testavocado.Utils.Validation;
import com.example.testavocado.fill_objects;
import com.example.testavocado.methods.emil_methods.check_if_emil_exists;
import com.example.testavocado.methods.emil_methods.on_checking_emil_exists;
import com.example.testavocado.methods.help_methods.Help_methods;
import com.example.testavocado.objects.user;
import com.example.testavocado.validation.validations;
import com.google.android.material.textfield.TextInputLayout;

public class registeraccount_page extends AppCompatActivity {
    public static final String TAG = "registeraccount";
    private ImageView arrow_image;
    private Context mycontext = registeraccount_page.this;
    private RelativeLayout parentreltive;
    private Button registerbtn;
    private EditText edtxt_user_firstname, edtxt_user_lastname, edtxt_user_emil, edtxt_user_password;
    private TextInputLayout txtinputlayoutfirstname, txtinputlayoutlastname, txtinputlayoutemil, txtinputlayoutpassword;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeraccount_page);
        load_widgets();
    }



    class myclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imgv_merge_register_arrow:
                    Log.d(TAG, "onClick: imgview arrow clicked");
                    finish();
                    break;

                case R.id.realtive_registeraccountboss_page:
                    Help_methods.hideKeyboard(registeraccount_page.this);
                    break;

                case R.id.btn_register_merge_registeraccount:
                    Log.d(TAG, "onClick: button register clicked");
                    final user newaccount = fill_objects.fill_register_object(edtxt_user_firstname, edtxt_user_lastname, edtxt_user_emil, edtxt_user_password);
                    // checking if emil used or not if used get error message if not pass to second register page1

                  //  Validation validation=new Validation(mycontext);

                   // validation.Email(newaccount.getUser_emil());

                    if(validateinfo(newaccount)==1) {
                        mProgressBar.setVisibility(View.VISIBLE);

                        RegisterMethods.onRegisteringNewUser(newaccount.getUser_firstname(), newaccount.getUser_lastname(), newaccount.getUser_emil(), newaccount.getUser_password()
                                , TimeMethods.getUTCdatetimeAsString(), mycontext, new RegisterMethods.onLoginRegister() {
                                    @Override
                                    public void onSuccessListener(int user_id) {

                                        HelpMethods.save_user_id(user_id, mycontext);
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        register_page1_Fragment fragmentpage1 = new register_page1_Fragment();
                                        fragmentpage1.setting = new Setting();
                                        fragmentpage1.setting.setUser_first_name(edtxt_user_firstname.getText().toString());
                                        fragmentpage1.setting.setUser_last_name(edtxt_user_lastname.getText().toString());
                                        fragmentpage1.setting.setUser_id(user_id);
                                        //realtive activty layout
                                        fragmentTransaction.add(R.id.realtive_registeraccountboss_page, fragmentpage1).commit();

                                        mProgressBar.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onServerException(String ex) {
                                        Log.d(TAG, "onServerException: " + ex);
                                        txtinputlayoutemil.setError(getString(R.string.emilused));
                                        mProgressBar.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onFailureListener(String ex) {
                                        Log.d(TAG, "onFailureListener: " + ex);
                                        mProgressBar.setVisibility(View.GONE);
                                        Toast.makeText(mycontext, getString(R.string.ERROR_TOAST), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                    break;

            }
        }
    }

    // check if all edit text boxes is not empty to make button register enabeld
    private TextWatcher checkedtxtboxsifnotempty = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String firstname2 = edtxt_user_firstname.getText().toString().trim();
            String lastname2 = edtxt_user_lastname.getText().toString().trim();
            String emil = edtxt_user_emil.getText().toString().trim();
            String password = edtxt_user_password.getText().toString().trim();

            registerbtn.setEnabled(!firstname2.isEmpty() && !lastname2.isEmpty() && !emil.isEmpty() && !password.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    // load widgets function
    public void load_widgets() {
        Log.i(getString(R.string.log_i), "sucess load widgets_regusteracciunt_page");

        mProgressBar=findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        arrow_image = (ImageView) findViewById(R.id.imgv_merge_register_arrow);
        parentreltive = (RelativeLayout) findViewById(R.id.realtive_registeraccountboss_page);
        registerbtn = (Button) findViewById(R.id.btn_register_merge_registeraccount);
        registerbtn.setEnabled(false);
        // register widgets
        edtxt_user_firstname = (EditText) findViewById(R.id.edtxt_firstname_merge_registeraccount);
        edtxt_user_lastname = (EditText) findViewById(R.id.edtxt_lastname_merge_registeraccount);
        edtxt_user_emil = (EditText) findViewById(R.id.edtxt_emil_merge_registeraccount);
        edtxt_user_password = (EditText) findViewById(R.id.edtxt_password_merge_registeraccount);
        txtinputlayoutfirstname = (TextInputLayout) findViewById(R.id.textinput_layout1_registeraccount);
        txtinputlayoutlastname = (TextInputLayout) findViewById(R.id.textinput_layout2_registeraccount);
        txtinputlayoutemil = (TextInputLayout) findViewById(R.id.textinput_layout3_registeraccount);
        txtinputlayoutpassword = (TextInputLayout) findViewById(R.id.textinput_layout4_registeraccount);
        //------------------------------------------------------------------------>
        arrow_image.setOnClickListener(new myclick());
        parentreltive.setOnClickListener(new myclick());
        registerbtn.setOnClickListener(new myclick());
        edtxt_user_firstname.addTextChangedListener(checkedtxtboxsifnotempty);
        edtxt_user_lastname.addTextChangedListener(checkedtxtboxsifnotempty);
        edtxt_user_emil.addTextChangedListener(checkedtxtboxsifnotempty);
        edtxt_user_password.addTextChangedListener(checkedtxtboxsifnotempty);

    }

    //validate sign up informations
    public int validateinfo(user account) {
        Log.d(TAG, "validateinfo: ");
        //input layout emil------------------------------------------------------------------>
        validations myvalidate = new validations(mycontext);
        if (!myvalidate.EMailValidation(account.getUser_emil())) {
            edtxt_user_emil.setError(getString(R.string.invalid_emiladdress));
            return -1;
        } else
            edtxt_user_emil.setError(null);
        //unputlayout password---------------------------------------------------------------->
        if (!myvalidate.PasswordValidation(account.getUser_password())) {
            edtxt_user_password.setError(getString(R.string.password_conditions));
            return -1;
        } else edtxt_user_password.setError(null);
        //unputlayout firstname---------------------------------------------------------------->
        if (myvalidate.Name_validate(account.getUser_firstname()) == -1) {
            edtxt_user_firstname.setError(getString(R.string.first_last_charcter_numbers_only));
            return -1;
        } else
            edtxt_user_firstname.setError(null);
        //unputlayout password---------------------------------------------------------------->
        if (myvalidate.Name_validate(account.getUser_lastname()) == -1) {
            edtxt_user_lastname.setError(getString(R.string.first_last_charcter_numbers_only));
            return -1;
        } else edtxt_user_lastname.setError(null);

        return 1;
    }

    // dump all edit text------------------------------------------>
    public void Dump_the_registerwidgets() {
        Log.d(TAG, "Dump_the_registerwidgets: ");
        edtxt_user_lastname.setText("");
        edtxt_user_firstname.setText("");
        edtxt_user_emil.setText("");
        edtxt_user_password.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    //when press back in the smartphone
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
