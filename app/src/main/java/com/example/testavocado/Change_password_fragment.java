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

import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.Validation;
import com.example.testavocado.validation.validations;

public class Change_password_fragment extends Fragment {
    private static final String TAG = "Change_password_fragmen";
    private Context mcontext;
    private View myview;
    private TextView txt_title;
    private EditText edtxt_current_password,edtxt_new_password,edtxt_retype_newpassword;
    private Button btn_save,btn_cancel;
    private LinearLayout linearLayout_full1;
    private ImageView img_arrow;
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
        linearLayout_full1=(LinearLayout)myview.findViewById(R.id.linearLayout_full_change_password);
        txt_title=(TextView)myview.findViewById(R.id.txtv_post_created_firstname_merge_topbar_back_arrow);
        txt_title.setText("Change Password");
        edtxt_current_password=(EditText)myview.findViewById(R.id.edtxt_current_password);
        edtxt_new_password=(EditText)myview.findViewById(R.id.edtxt_new_password);
        edtxt_retype_newpassword=(EditText)myview.findViewById(R.id.edtxt_retype_new_password);
        btn_save=(Button)myview.findViewById(R.id.btn_save_newpassword);
        btn_cancel=(Button)myview.findViewById(R.id.btn_cancel_newpassword);
        img_arrow=(ImageView)myview.findViewById(R.id.imgv_arrow_back_merge_topbar_back_arrow);

        //set on click listner
        img_arrow.setOnClickListener(new onclick());
        linearLayout_full1.setOnClickListener(new onclick());
        btn_save.setOnClickListener(new onclick());
        btn_cancel.setOnClickListener(new onclick());
    }

    class onclick implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.btn_save_newpassword:
                    validations validation1=new validations(mcontext);
                    final String current_password,new_password,retype_new_password;
                    //------------------------------------------------------------------------->
                    current_password=edtxt_current_password.getText().toString().trim();
                    new_password=edtxt_new_password.getText().toString().trim();
                    retype_new_password=edtxt_retype_newpassword.getText().toString();
                    //------------------------------------------------------------------------->
                    Boolean current_password_validation=false,new_retype_isequals=false,new_password_validtion=false;

                    if(!current_password.isEmpty()&&!new_password.isEmpty()&&!retype_new_password.isEmpty())
                    {
                        current_password_validation=validation1.PasswordValidation(current_password);

                        if (!current_password_validation)
                            Toast.makeText(mcontext, "Wrong password Type", Toast.LENGTH_SHORT).show();

                        else if (new_password.equals(retype_new_password)) {
                            new_retype_isequals = true;
                            new_password_validtion = validation1.PasswordValidation(new_password);

                            if (current_password_validation == true && new_password_validtion == true) {


                                //aler dialog
                                final AlertDialog dialog=new AlertDialog.Builder(mcontext)
                                        .setTitle("Change Email")
                                        .setMessage("Are you sure about that?")
                                        .setPositiveButton("Yes",null)
                                        .setNegativeButton("Cancel",null)
                                        .show();
                                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);

                                Button positivebutton=dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                                positivebutton.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view) {

                                        Update_information_Methods.Update_password(mcontext, HelpMethods.get_userid_sharedp(mcontext), current_password, new_password,
                                                new Update_information_Methods.on_first_last_name_updated() {
                                                    @Override
                                                    public void onSuccessListener(int result) {
                                                        Toast.makeText(mcontext, getString(R.string.UPDATED), Toast.LENGTH_SHORT).show();
                                                        clear_values();
                                                        dialog.dismiss();
                                                    }

                                                    @Override
                                                    public void onServerException(String ex) {
                                                        if(ex.equals("0"))
                                                            Toast.makeText(mcontext, "The current password is invalid", Toast.LENGTH_SHORT).show();
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
                        } else
                            Toast.makeText(mcontext, " password not match ", Toast.LENGTH_SHORT).show();

                    }

                    else Toast.makeText(mcontext, "U need to fill all of the rows", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.btn_cancel_newpassword:
                    close_all_fragments();
                    break;

                case R.id.imgv_arrow_back_merge_topbar_back_arrow:
                    close_all_fragments();
                    break;
            }
        }
    }

    //clear values after change
public void clear_values()
{
    edtxt_new_password.setText("");
    edtxt_retype_newpassword.setText("");
    edtxt_current_password.setText("");
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
