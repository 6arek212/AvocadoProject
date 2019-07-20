package com.example.testavocado.EditeInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testavocado.Dialogs.CallenderDialog;
import com.example.testavocado.R;
import com.example.testavocado.Utils.GenderItem;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.SpinnerGenderAdapter;
import com.example.testavocado.Utils.Validation;

import java.util.ArrayList;

public class UserInfoFragment extends Fragment implements CallenderDialog.OnDatePickListener, FragmentLifecycle {
    private static final String TAG = "UserInfoFragment";

    @Override
    public void showArrow() {
        backArrow.setVisibility(View.VISIBLE);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickNextListener.onClickArrow();
            }
        });
    }

    @Override
    public void onLastPageSelected() {
        mNext.setText("finish");

    }

    @Override
    public void onFirstPage() {
        backArrow.setVisibility(View.GONE);
    }

    @Override
    public void onDatePick(String date) {
        mBirthDate.setText(date);
    }


    //widgets
    private TextView mBirthDate, mLastName;
    private Button mNext;
    private Spinner mGenderSpinner;
    private ImageView backArrow;


    ///vars
    private ArrayList<GenderItem> genderItems;
    private SpinnerGenderAdapter mAdapter;
    private onClickNextListener onClickNextListener;
    private Context mContext;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_info, container, false);


        initWidgets(view);


        return view;
    }


    /**
     * init all the widgets + attaching on click listener
     *
     * @param view
     */

    private void initWidgets(View view) {
        mGenderSpinner = view.findViewById(R.id.spinner);
        mBirthDate = view.findViewById(R.id.date);
        mNext = view.findViewById(R.id.btnAction);
        backArrow = view.findViewById(R.id.backArror);
        mLastName = view.findViewById(R.id.lastname);
        mContext=getContext();
        initSpinner();

        if (BaseProfileEditActivity.position == 0)
            backArrow.setVisibility(View.GONE);


        mBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallenderDialog dialog = new CallenderDialog();
                dialog.show(getFragmentManager(), "c1");
                dialog.setTargetFragment(UserInfoFragment.this, 1);
            }
        });


        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String lastName, birthDate;
                lastName = mLastName.getText().toString();
                birthDate = mBirthDate.getText().toString();
                int genderNum = mGenderSpinner.getSelectedItemPosition();
                int userid = checkSharedPreferencesForUserId();
                Log.d(TAG, "onClick: next"+userid);

                if (userid != getResources().getInteger(R.integer.defaultValue))
                    if (lastName.isEmpty() || genderNum == 0 || birthDate.isEmpty())
                        Snackbar.make(mNext, getString(R.string.fields_required_toUpdate), Snackbar.LENGTH_SHORT).show();
                    else {
                        String gender = ((GenderItem) mGenderSpinner.getSelectedItem()).getmGender();
                        if (gender.equals(R.string.male))
                            genderNum = 1;
                        else
                            genderNum = 0;

                        Validation validation = new Validation(getContext());
/*
                        if (validation.Name(lastName))
                            InfoMethodsHandler.(userid, lastName, genderNum, birthDate, new InfoMethodsHandler.OnUpdateListener() {
                                @Override
                                public void onSuccessListener() {
                                    ((BaseProfileEditActivity)getActivity()).setting.setUser_last_name(lastName);
                                    Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onServerException(String ex) {
                                    Log.d(TAG, "onServerException: "+ex);

                                }

                                @Override
                                public void onFailureListener(String ex) {
                                    Log.d(TAG, "onFailureListener: "+ex);

                                }
                            });*/
                    }
                else {
                    Toast.makeText(getContext(), getString(R.string.ERROR_TOAST), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    /**
     * initializing the spinner
     */

    private void initSpinner() {
        genderItems = new ArrayList<GenderItem>();
        genderItems.add(new GenderItem("Gender...", 0));
        genderItems.add(new GenderItem("male", R.drawable.male_icon));
        genderItems.add(new GenderItem("female", R.drawable.female_icon));

        mAdapter = new SpinnerGenderAdapter(getActivity(), 0, genderItems);
        mGenderSpinner.setAdapter(mAdapter);
    }


    /**
     * get the current user id form shared preferences
     * return getResources().getInteger(R.integer.defaultValue) = -1 if not found
     *
     * @return
     */

    private int checkSharedPreferencesForUserId() {
        Log.d(TAG, "checkSharedPreferences: checking the shared preferences");
        SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.sharedPref_userId), Context.MODE_PRIVATE);
        int user_id = preferences.getInt(getString(R.string.sharedPref_userId), getResources().getInteger(R.integer.defaultValue));
        return user_id;
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
