package com.example.testavocado.Dialogs;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testavocado.R;

public class ConfirmDialog extends DialogFragment {
    private static final String TAG = "CallenderDialog";

    public interface OnConfirmListener {
        void onConfirm();
    }


    private TextView confirmDialog, cancelDialog, mTitle;


    private OnConfirmListener OnConfirmListener;
    private String title;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm, container, false);

        initWidgets(view);
        return view;
    }


    private void initWidgets(View view) {
        confirmDialog = view.findViewById(R.id.dialogConfirm);
        cancelDialog = view.findViewById(R.id.dialogCancel);
        mTitle = view.findViewById(R.id.dialogTitle);

        mTitle.setText(title);

        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



        confirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OnConfirmListener != null)
                    OnConfirmListener.onConfirm();
            }
        });
    }


    public void setOnConfirm(OnConfirmListener OnConfirmListener) {
        this.OnConfirmListener = OnConfirmListener;
    }

    public void setTitle(String title) {
            this.title=title;
    }





}

