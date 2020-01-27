package com.example.testavocado;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testavocado.Login.LoginMethods;
import com.example.testavocado.Models.Setting;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.validation.validations;

public class change_emailaddress_fragment extends Fragment {
    public static final String TAG = "change_emailaddress_fra";
    private Context mcontext;
    private View myview;
    private TextView txtv_title;
    private EditText edtxt_email,edtxt_password;
    private Button btn_add,btn_cancel;
    private ImageView imgv_backarrow;
    private LinearLayout linearLayout_full1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=getLayoutInflater().inflate(R.layout.fragment_change_emailaddress,container,false);
        load_widgets();
        return myview;
    }

    public void load_widgets()
    {
        linearLayout_full1=(LinearLayout)myview.findViewById(R.id.linearlayout_full_changemail);
        edtxt_email=(EditText)myview.findViewById(R.id.edtxt_emailaddress_edit);
        edtxt_password=(EditText)myview.findViewById(R.id.edtxt_password_edit);
        btn_add=(Button)myview.findViewById(R.id.btn_addnewemail_edit);
        btn_cancel=(Button)myview.findViewById(R.id.btn_cancel_edit);
        txtv_title=(TextView)myview.findViewById(R.id.txtv_post_created_firstname_merge_topbar_back_arrow);
        imgv_backarrow=(ImageView)myview.findViewById(R.id.imgv_arrow_back_merge_topbar_back_arrow);
        //set on clikc------------------------------------------------------------------>
        btn_cancel.setOnClickListener(new onclick());
        btn_add.setOnClickListener(new onclick());
        imgv_backarrow.setOnClickListener(new onclick());
        linearLayout_full1.setOnClickListener(new onclick());

        //set value
        txtv_title.setText(getString(R.string.addemail));
    }


    class onclick implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.btn_cancel_edit:
                    close_all_fragments();
                    break;

                case R.id.btn_addnewemail_edit:
                    final String email=edtxt_email.getText().toString();
                    validations validations1=new validations(mcontext);
                   
                    if(edtxt_password.getText().toString().trim().isEmpty()){
                        Toast.makeText(mcontext, "enter a password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                   
                    if(validations1.EMailValidation(email))
                    {

                        //aler dialog
                        final AlertDialog dialog=new AlertDialog.Builder(mcontext)
                                .setTitle("Change Email")
                                .setMessage("Are you sure about this?")
                                .setPositiveButton("Yes",null)
                                .setNegativeButton("Cancel",null)
                                .show();
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);

                        Button positivebutton=dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positivebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Update_information_Methods.Update_emailaddress(mcontext, HelpMethods.get_userid_sharedp(mcontext), email,edtxt_password.getText().toString(), new Update_information_Methods.on_first_last_name_updated() {
                                    @Override
                                    public void onSuccessListener(int result) {
                                        Toast.makeText(mcontext, getString(R.string.UPDATED), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onServerException(String ex) {
                                        Toast.makeText(mcontext, ex, Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "onServerException: "+ex);
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onFailureListener(String ex) {
                                        Toast.makeText(mcontext, getString(R.string.CHECK_INTERNET), Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "onFailureListener: "+ex);
                                        dialog.dismiss();
                                    }
                                });

                            }
                        });

                    }
                    break;

                case R.id.imgv_arrow_back_merge_topbar_back_arrow:
                    close_all_fragments();
                    break;
            }
        }
    }


    //pop back stack
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
