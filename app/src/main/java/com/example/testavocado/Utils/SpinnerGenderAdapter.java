package com.example.testavocado.Utils;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testavocado.R;

import java.util.List;

public class SpinnerGenderAdapter extends ArrayAdapter<GenderItem> {


    public SpinnerGenderAdapter(Context context, int resource, List<GenderItem> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }



    private View initView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_gender_row, parent, false);

        ImageView imageView=convertView.findViewById(R.id.img_gender);
        TextView textView=convertView.findViewById(R.id.tv_gender);


        imageView.setImageResource(getItem(position).getmImag());
        textView.setText(getItem(position).getmGender());
        return convertView;
    }

}
