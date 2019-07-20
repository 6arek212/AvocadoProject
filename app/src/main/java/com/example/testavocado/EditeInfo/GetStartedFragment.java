package com.example.testavocado.EditeInfo;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.testavocado.R;

public class GetStartedFragment extends Fragment implements FragmentLifecycle{
    private static final String TAG = "GetStartedFragment";

    @Override
    public void showArrow() {
    }
    @Override
    public void onFirstPage() {

    }

    @Override
    public void onLastPageSelected() {
        Log.d(TAG, "onLastPageSelected: ");
        Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
    }


    //widgets
    private Button mDone;

    //vars
    onClickNextListener onClickNextListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_layout_getstarted,container,false);

        mDone=view.findViewById(R.id.done);


        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickNextListener.onClickNext();
            }
        });


        return view;
    }



    /**
     * getting the activity referenecce for updating the viewpager
     *
     * @param context
     */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onClickNextListener = (com.example.testavocado.EditeInfo.onClickNextListener) context;

        } catch (ClassCastException e) {
            Log.d(TAG, "onAttach: error ClassCastException " + e.getMessage());
        }
    }
}
