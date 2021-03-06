package com.example.testavocado;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
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

import com.example.testavocado.Models.Setting;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.validation.validations;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class change_first_last_name_fragment extends Fragment {
    public static final String TAG = "change_first_last_name_";
    private Context mcontext;
    private View myview;
    private EditText edtxt_first_name,edtxt_last_name;
    private Button btn_save,btn_cancel;
    private TextView txtv_title;
    private ImageView imgv_arrowback;
    private Setting setting1;
    private LinearLayout linearLayout_full1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=getLayoutInflater().inflate(R.layout.fragment_change_first_last_name,container,false);
        load_widgets();
        return myview;

    }

    public void load_widgets()
    {
        btn_cancel=(Button)myview.findViewById(R.id.btn_cancel2);
        linearLayout_full1=(LinearLayout)myview.findViewById(R.id.linearLayout_full_firstname_lastname_change);
        edtxt_first_name=(EditText)myview.findViewById(R.id.edtxt_first_name_edit);
        edtxt_last_name=(EditText)myview.findViewById(R.id.edtxt_last_name_edit);
        btn_save=(Button)myview.findViewById(R.id.btn_save_edit);
        txtv_title=(TextView)myview.findViewById(R.id.txtv_post_created_firstname_merge_topbar_back_arrow);
        imgv_arrowback=(ImageView)myview.findViewById(R.id.imgv_arrow_back_merge_topbar_back_arrow);

        //set on click
        linearLayout_full1.setOnClickListener(new onclick());
        btn_save.setOnClickListener(new onclick());
        txtv_title.setOnClickListener(new onclick());
        imgv_arrowback.setOnClickListener(new onclick());
        btn_cancel.setOnClickListener(new onclick());
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
                case R.id.imgv_arrow_back_merge_topbar_back_arrow:
                    getFragmentManager().popBackStack();
                    break;

                case R.id.btn_cancel2:
                    getFragmentManager().popBackStack();
                    break;

                case R.id.btn_save_edit:

                    final String firstname,lastname;
                    firstname=edtxt_first_name.getText().toString();
                    lastname=edtxt_last_name.getText().toString();

                    validations validations1=new validations(mcontext);
                    if(validations1.Name_validate(firstname)==1&&validations1.lastname_validate(lastname)==1)
                    {
                        //aler dialog
                        final AlertDialog dialog=new AlertDialog.Builder(mcontext)
                                .setTitle("Change First Last name")
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

                                Update_information_Methods.Update_first_last_name(mcontext,setting1.getUser_id(), firstname, lastname,
                                        new Update_information_Methods.on_first_last_name_updated() {
                                            @Override
                                            public void onSuccessListener(int result) {
                                                if(result==1) {
                                                    Toast.makeText(mcontext, getString(R.string.succuess_updated), Toast.LENGTH_SHORT).show();
                                                    HelpMethods.updateName(firstname,lastname,mcontext);

                                                    DatabaseReference fr= FirebaseDatabase.getInstance().getReference();
                                                    fr.child("users").child(String.valueOf(setting1.getUser_id())).child("name").setValue(firstname+" "+lastname);
                                                    dialog.dismiss();
                                                }
                                            }

                                            @Override
                                            public void onServerException(String ex) {
                                                Toast.makeText(mcontext, getString(R.string.ERROR_TOAST), Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }

                                            @Override
                                            public void onFailureListener(String ex) {
                                                Toast.makeText(mcontext, R.string.no_intrent_connection+"", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        });

                            }
                        });

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
