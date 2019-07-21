package com.example.testavocado.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.testavocado.R;

public class ConfirmDialogEditeText extends DialogFragment {
    private static final String TAG = "CallenderDialog";

    public interface OnConfirmListener {
        void onConfirm(String text);
    }


    private TextView confirmDialog, cancelDialog, mTitle;


    private OnConfirmListener OnConfirmListener;
    private String title,hint;
    private EditText mEd;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm_witheditetext, container, false);

        initWidgets(view);
        return view;
    }


    private void initWidgets(View view) {
        confirmDialog = view.findViewById(R.id.dialogConfirm);
        cancelDialog = view.findViewById(R.id.dialogCancel);
        mTitle = view.findViewById(R.id.dialogTitle);
        mEd=view.findViewById(R.id.ed);

        mTitle.setText(title);
        mEd.setHint(hint);

        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



        confirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text= mEd.getText().toString();

                if (!text.trim().isEmpty()){
                    if (OnConfirmListener != null)
                        OnConfirmListener.onConfirm(text);
                }
                else {
                    Toast.makeText(getContext(), "input cant be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void setOnConfirm(OnConfirmListener OnConfirmListener) {
        this.OnConfirmListener = OnConfirmListener;
    }

    public void setTitle(String title) {
            this.title=title;
    }


    public void setHind(String hint){
        this.hint=hint;
    }



}

