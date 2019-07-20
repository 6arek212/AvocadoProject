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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testavocado.R;
import com.example.testavocado.Utils.Permissions;
import com.example.testavocado.Utils.Validation;

public class ProfileLocationFragment extends Fragment implements FragmentLifecycle {
    private static final String TAG = "ProfileLocationFragment";



    /**
     *
     *                   interface
     */


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
    public void onFirstPage() {
        backArrow.setVisibility(View.GONE);
    }

    @Override
    public void onLastPageSelected() {
        mNext.setText("finish");
    }





    //vars
    private onClickNextListener onClickNextListener;



    //widgets
    private Button mNext;
    private ImageView backArrow;
    private Button mPermission;
    private TextView mCountry;
    private EditText mCityName;
    private Context mContext;






    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_info_location, container, false);

        initWidgets(view);
        if (BaseProfileEditActivity.position == 0)
            backArrow.setVisibility(View.GONE);

        return view;
    }










    /**
     *              initializing all the widgets + attaching the ocClicks
     *
     * @param view
     */

    private void initWidgets(View view) {
        mNext = view.findViewById(R.id.btnAction);
        backArrow = view.findViewById(R.id.backArror);
        mPermission = view.findViewById(R.id.btnPermission);
        mCountry = view.findViewById(R.id.pickCountry);
        mCityName = view.findViewById(R.id.city_name);
        mContext=getContext();


      /*  mCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CountryCurrencyPicker pickerDialog = CountryCurrencyPicker.newInstance(PickerType.COUNTRYandCURRENCY, new CountryCurrencyPickerListener() {
                    @Override
                    public void onSelectCountry(Country country) {
                        mCountry.setText(country.getName());
                    }

                    @Override
                    public void onSelectCurrency(Currency currency) {
                    }
                });

                pickerDialog.show(getActivity().getSupportFragmentManager(), CountryCurrencyPicker.DIALOG_NAME);


            }
        });*/

        mPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Permissions.checkPermission(Permissions.GPS, getContext())) {

                } else {
                    Permissions.verifyOnePermission(Permissions.GPS, getActivity());
                }


            }
        });


        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String city, country;
                city = mCityName.getText().toString();
                country = mCountry.getText().toString();

                if (city.trim().isEmpty() || country.trim().isEmpty()) {


                } else {
                    Validation validation = new Validation(getContext());
                    if (validation.Name(city)) {
                        if (BaseProfileEditActivity.USER_ID != -1)
                            InfoMethodsHandler.updatingUserLocationSetting(BaseProfileEditActivity.USER_ID, country, city, new InfoMethodsHandler.OnUpdateListener() {
                                @Override
                                public void onSuccessListener() {
                                    Toast.makeText(mContext, "Updated ", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onServerException(String ex) {
                                    Log.d(TAG, "onServerException: "+ex);

                                }

                                @Override
                                public void onFailureListener(String ex) {
                                    Log.d(TAG, "onFailureListener: "+ex);

                                }
                            });
                    }
                }


            }
        });


    }


    /**
     * Updating the user location setting
     *
     *
     */

/*
    private void updatingUserLocationSetting(int user_id, String country, String city) {
        Log.d(TAG, "updatingUserLocationSetting: attempting to update location settings");

        RequestParams params = new RequestParams();

        params.add(getString(R.string.user_id), String.valueOf(user_id));
        params.add(getString(R.string.user_country), country);
        params.add(getString(R.string.user_city), city);


        ServerConnection.get(getString(R.string.updateCityCountry), params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {


                    Status status = new Gson().fromJson(response.toString(),Status.class);


                    String jsonString = response.getString(getString(R.string.Json_data));

                    if (status.getState()==1) {
                        Log.d(TAG, "onSuccess: task is successfull");

                        onClickNextListener.onClickNext();
                    } else if (status.getState()==0) {
                        Toast.makeText(getContext(), getString(R.string.ERROR_TOAST), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onSuccess: error state " + status.getException());
                    } else {
                        Toast.makeText(getContext(), getString(R.string.ERROR_TOAST), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onSuccess: error state system exception " + status.getException());
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), getString(R.string.ERROR_TOAST), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onSuccess: JSONException " + e.getMessage());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getContext(), getString(R.string.ERROR_TOAST), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: error " + throwable.getMessage());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getContext(), getString(R.string.ERROR_TOAST), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: error " + throwable.getMessage());

            }
        });


    }
*/

    /**
     * getting Base Activity reference
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
