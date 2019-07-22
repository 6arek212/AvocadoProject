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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testavocado.Models.Setting;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.validation.validations;

public class change_first_last_name_fragment extends Fragment {
    public static final String TAG = "change_first_last_name_";
    private Context mcontext;
    private View myview;
    private EditText edtxt_first_name,edtxt_last_name;
    private Button btn_save;
    private TextView txtv_title;
    private ImageView imgv_arrowback;
    private Setting setting1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=getLayoutInflater().inflate(R.layout.fragment_change_first_last_name,container,false);
        load_widgets();
        return myview;

    }

    public void load_widgets()
    {
        edtxt_first_name=(EditText)myview.findViewById(R.id.edtxt_first_name_edit);
        edtxt_last_name=(EditText)myview.findViewById(R.id.edtxt_last_name_edit);
        btn_save=(Button)myview.findViewById(R.id.btn_save_edit);
        txtv_title=(TextView)myview.findViewById(R.id.txtv_post_created_firstname_merge_topbar_back_arrow);
        imgv_arrowback=(ImageView)myview.findViewById(R.id.imgv_arrow_back_merge_topbar_back_arrow);
        //set on click
        btn_save.setOnClickListener(new onclick());
        txtv_title.setOnClickListener(new onclick());
        imgv_arrowback.setOnClickListener(new onclick());
        //set values
        txtv_title.setText(getString(R.string.name));
        setting1= HelpMethods.getSharedPreferences(mcontext);
        edtxt_first_name.setText(setting1.getUser_first_name());
        edtxt_last_name.setText(setting1.getUser_last_name());
    }

    class onclick implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.btn_save_edit:
                    String firstname,lastname;
                    firstname=edtxt_first_name.getText().toString();
                    lastname=edtxt_last_name.getText().toString();

                    validations validations1=new validations(mcontext);
                    if(validations1.Name_validate(firstname)==1&&validations1.lastname_validate(lastname)==1)
                    {
                        Update_information_Methods.Update_first_last_name(mcontext,setting1.getUser_id(), firstname, lastname,
                                new Update_information_Methods.on_first_last_name_updated() {
                            @Override
                            public void onSuccessListener(int result) {
                                if(result==1)
                                    Toast.makeText(mcontext, R.string.succuess_updated+"", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onServerException(String ex) {

                            }

                            @Override
                            public void onFailureListener(String ex) {
                                Toast.makeText(mcontext, R.string.no_intrent_connection+"", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    break;

                case R.id.imgv_arrow_back_merge_topbar_back_arrow:
                    for (Fragment fragment : getActivity().getSupportFragmentManager().getFragments()) {
                       getActivity(). getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    }
                    break;
            }
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
