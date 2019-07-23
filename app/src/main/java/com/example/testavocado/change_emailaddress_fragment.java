package com.example.testavocado;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testavocado.Login.LoginMethods;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=getLayoutInflater().inflate(R.layout.fragment_change_emailaddress,container,false);
        load_widgets();
        return myview;
    }

    public void load_widgets()
    {
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
                    String email=edtxt_email.getText().toString();
                    validations validations1=new validations(mcontext);
                    if(validations1.EMailValidation(email))
                    {
                        //check if email exists or not if not exists then sending message to old email and new email
                    }
                    else Toast.makeText(mcontext, getString(R.string.wrong_email_format)+"", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.imgv_arrow_back_merge_topbar_back_arrow:
                    close_all_fragments();
                    break;
            }
        }
    }


    //closeing all of the fragment in backstack
    public void close_all_fragments()
    {
        for (Fragment fragment : getActivity().getSupportFragmentManager().getFragments()) {
            getActivity(). getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mcontext = (Activity) context;
            Log.d(TAG, "onAttach: " + mcontext);
        }
    }
}
