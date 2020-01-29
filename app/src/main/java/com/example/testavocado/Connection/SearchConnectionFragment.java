package com.example.testavocado.Connection;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appyvet.materialrangebar.RangeBar;
import com.example.testavocado.Models.Setting;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.Permissions;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.testavocado.Models.UserAdd;
import com.example.testavocado.R;
import com.example.testavocado.Utils.LocationMethods;
import com.example.testavocado.Utils.TimeMethods;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener;

import java.util.List;
import java.util.Set;

import static android.content.Context.LOCATION_SERVICE;
import static com.example.testavocado.Utils.Permissions.GPS;
import static com.example.testavocado.Utils.Permissions.GPS2;

public class SearchConnectionFragment extends Fragment {
    private static final String TAG = "SearchConnectionFragmen";

    //widgets
    private RecyclerView mRecyclerView;
    private EditText mSearchName;
    private CheckBox mNearByUsers;
    private RangeBar numberPicker;


    //vars
    private Context mContext;
    private int user_current_id;
    private RecyclerViewAddConnectionAdapter adapter;
    private String datetime;
    private double lat, longi;
    private LocationManager manager;
    private RelativeLayout mainLayout;
    private SwipeRefreshLayout mSwipe;
    private boolean isGotLocation;


    private static boolean loading;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_serach_connections, container, false);
        initWidgets(view);

        return view;
    }


    /*****
     *
     *
     *
     *              init all widgets
     *
     * @param view
     */
    private void initWidgets(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mSearchName = view.findViewById(R.id.searchName);
        mNearByUsers = view.findViewById(R.id.nearbyUsersBox);
        numberPicker = view.findViewById(R.id.number_picker_horizontal);
        mainLayout = view.findViewById(R.id.layoutSearch);
        mSwipe = view.findViewById(R.id.swipe);
        mContext = getContext();
        user_current_id = ConnectionsActivity.user_current_id;
        numberPicker.setVisibility(View.GONE);


        initRecyclerView();
        initTextListener();

        numberPicker.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                Log.d(TAG, "onRangeChangeListener: " + rightPinIndex);
                if (mNearByUsers.isChecked())
                    getNearbyUsers(0, lat, longi, rightPinIndex);
            }

            @Override
            public void onTouchStarted(RangeBar rangeBar) {

            }

            @Override
            public void onTouchEnded(RangeBar rangeBar) {

            }
        });


        getUsers(0);


        mNearByUsers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged: " + isChecked);
                if (isChecked) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                    boolean _switch = sharedPreferences.getBoolean(getString(R.string.location_switch), false);

                    if (_switch) {
                        adapter.searchByLocation = true;
                        numberPicker.setVisibility(View.VISIBLE);
                        adapter.clearList();
                        getLocation();
                    } else {
                        Snackbar.make(requireActivity().findViewById(android.R.id.content), "You have to enable the location in settings", Snackbar.LENGTH_SHORT).show();
                    }

                } else {
                    numberPicker.setVisibility(View.GONE);
                    adapter.searchByLocation = false;
                    getUsers(0);
                }
            }
        });


        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mNearByUsers.isChecked()) {
                    getNearbyUsers(0, lat, longi, numberPicker.getRightIndex());

                } else {
                    getUsers(0);
                }
            }
        });
    }


    private void initRecyclerView() {
        adapter = new RecyclerViewAddConnectionAdapter(mContext, user_current_id, getActivity(), requireActivity().getSupportFragmentManager());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(adapter);
        recyclerViewBottomDetectionListener();
    }


    private void initTextListener() {
        mSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (mSearchName.getText().toString().trim().isEmpty()) {
//                    Log.d(TAG, "afterTextChanged: if " + mSearchName.getText().toString().length());
//                    adapter.clearList();
//                    getUsers(0);
//
//                } else {
//                    Log.d(TAG, "afterTextChanged: else " + mSearchName.getText().toString().length());
//                    if (mNearByUsers.isChecked()) {
//                        getNearbyUsers(0, lat, longi,numberPicker.getRightIndex());
//
//                    } else {
//                        getUsers(0);
//                    }
//                }
                if (mNearByUsers.isChecked()) {
                    getNearbyUsers(0, lat, longi, numberPicker.getRightIndex());

                } else {
                    getUsers(0);
                }
            }
        });
    }


    /**
     * Attaching a listener to detect when the list reached the bottom
     */
    private void recyclerViewBottomDetectionListener() {
        mRecyclerView
                .addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView,
                                           int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (isRecyclerScrollable())
                            if (!mRecyclerView.canScrollVertically(1) && !loading) {
                                Log.d(TAG, "recyclerViewBottomDetectionListener: bottom");

                                loading = true;

                                if (mNearByUsers.isChecked())
                                    getNearbyUsers(adapter.getItemCount(), lat, longi, numberPicker.getRightIndex());
                                else
                                    getUsers(adapter.getItemCount());

                            }
                    }
                });
    }


    /**
     * using this when detecting if the bottom reached
     */

    public static void setLoaded() {
        loading = false;
    }


    public boolean isRecyclerScrollable() {
        return mRecyclerView.computeVerticalScrollRange() > mRecyclerView.getHeight();
    }


    /***
     *
     *              getting users by name
     *
     * @param offset
     */

    private void getUsers(final int offset) {
        if (offset == 0) {
            datetime = TimeMethods.getUTCdatetimeAsString();
            adapter.is_endOfPosts = false;
            adapter.clearList();
            loading = true;
        }
        adapter.addNull();

        ConnectionsHandler.getUsersByName(user_current_id, mSearchName.getText().toString(), datetime,
                offset, new ConnectionsHandler.OnGettingUsersByNameListener() {
                    @Override

                    public void onSuccessListener(List<UserAdd> list) {
                        Log.d(TAG, "onSuccessListener: " + list.size());
                        adapter.removeProg();
                        if (offset == 0)
                            adapter.clearList();
                        adapter.addNewSetUserToAdd(list, adapter.getItemCount());
                        mSwipe.setRefreshing(false);
                        loading = false;
                    }

                    @Override
                    public void onServer(String ex) {
                        Log.d(TAG, "onServer: " + ex);
                        adapter.removeProg();
                        mSwipe.setRefreshing(false);

                        if (!adapter.is_endOfPosts) {
                            adapter.is_endOfPosts = true;
                            adapter.addNull();
                        }
                    }

                    @Override
                    public void onFailure(String ex) {
                        Log.d(TAG, "onFailure: " + ex);
                        adapter.removeProg();
                        adapter.clearList();
                        mSwipe.setRefreshing(false);

                        if (!adapter.is_endOfPosts) {
                            adapter.is_endOfPosts = true;
                            adapter.addNull();
                        }
                    }
                });
    }


    /***
     *
     *              getting users by name and location
     *
     *
     * @param offset
     * @param latitude
     * @param longitude
     */
    private void getNearbyUsers(int offset, double latitude, double longitude, final int km) {
        if (offset == 0) {
            adapter.clearList();
            datetime = TimeMethods.getUTCdatetimeAsString();
            adapter.is_endOfPosts = false;
            loading = true;
        }
        adapter.addNull();


        ConnectionsHandler.getNearByUsers(user_current_id, latitude, longitude, km, mSearchName.getText().toString(), datetime, offset, new ConnectionsHandler.OnGettingNearByUsersListener() {
            @Override
            public void onSuccessListener(List<UserAdd> userAddList) {
                Log.d(TAG, "onSuccessListener: " + userAddList.size());
                adapter.removeProg();

                if (km == numberPicker.getRightIndex())
                    adapter.addNewSetUserToAdd(userAddList, adapter.getItemCount());


                mSwipe.setRefreshing(false);
                loading = false;
            }

            @Override
            public void onServerException(String ex) {
                Log.d(TAG, "onServerException: " + ex);
                adapter.removeProg();
                mSwipe.setRefreshing(false);

                if (!adapter.is_endOfPosts) {
                    adapter.is_endOfPosts = true;
                    adapter.addNull();
                }
            }

            @Override
            public void onFailureListener(String ex) {
                Log.d(TAG, "onFailureListener: " + ex);
                adapter.removeProg();
                adapter.clearList();
                mSwipe.setRefreshing(false);
                Snackbar.make(requireActivity().findViewById(android.R.id.content), getString(R.string.CHECK_INTERNET), Snackbar.LENGTH_SHORT).show();

                if (!adapter.is_endOfPosts) {
                    adapter.is_endOfPosts = true;
                    adapter.addNull();
                }

            }
        });
    }


    private final android.location.LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            Log.d(TAG, "onLocationChanged: " + location);
            isGotLocation=true;
            Toast.makeText(mContext, "got location", Toast.LENGTH_SHORT).show();
            adapter.searchByLocation = true;
            lat = location.getLatitude();
            longi = location.getLongitude();
            getNearbyUsers(0, location.getLatitude(), location.getLongitude(), numberPicker.getRightIndex());
            updateLocationInServer(location.getLatitude(), location.getLongitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged: ");

        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled: ");

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled: ");

        }
    };


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    private boolean hasGPSDevice() {
        PackageManager packageManager = mContext.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
    }


    private void normalSearch() {
        numberPicker.setVisibility(View.GONE);
        adapter.searchByLocation = false;
        Snackbar.make(requireActivity().findViewById(android.R.id.content), getString(R.string.GPS_ERROR), Snackbar.LENGTH_SHORT).show();
        mNearByUsers.setChecked(false);
    }


    /***
     *
     *
     *              updating location
     *
     */
    Handler handler = new Handler();

    private void getLocation() {
        manager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        isGotLocation = false;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            String str[] = {GPS, GPS2};
            Permissions.verifyPermission(str, getActivity());
            normalSearch();
            return;
        }

        if (!hasGPSDevice()) {
            HelpMethods.showAlert("You'r device dosent has gps", mContext, new HelpMethods.OnClickDialog() {
                @Override
                public void onClick() {
                }
            });
            normalSearch();
            return;
        }


        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            normalSearch();
            return;
        }

        Toast.makeText(mContext, "We are getting location it may take few seconds", Toast.LENGTH_SHORT).show();

        final LocationManager mLocationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, mLocationListener, Looper.myLooper());


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(40000);
                    mLocationManager.removeUpdates(mLocationListener);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!isGotLocation && mNearByUsers.isChecked()) {
                                normalSearch();
                            }
                        }
                    });

                } catch (Exception e) {

                }
            }
        }).start();


//        Location l1 = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        Location l2 = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        Location l3 = manager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

//        Log.d(TAG, "getLocation: get location " + l1 + " " + l2 + " " + l3 + "   ");
//        if (l1 != null) {
//            Log.d(TAG, "updateLocation12: got it l1");
//            lat = l1.getLatitude();
//            longi = l1.getLongitude();
//            adapter.searchByLocation = true;
//            updateLocationInServer(l1.getLatitude(), l1.getLongitude());
//        } else if (l2 != null) {
//            Log.d(TAG, "updateLocation12: got it l2 " + l2);
//            lat = l2.getLatitude();
//            longi = l2.getLongitude();
//            adapter.searchByLocation = true;
//            updateLocationInServer(l2.getLatitude(), l2.getLongitude());
//        } else if (l3 != null) {
//            Log.d(TAG, "updateLocation12: got it l3 " + l3);
//            lat = l3.getLatitude();
//            longi = l3.getLongitude();
//            adapter.searchByLocation = true;
//            updateLocationInServer(l3.getLatitude(), l3.getLongitude());
//        } else {
//            numberPicker.setVisibility(View.GONE);
//            adapter.searchByLocation = false;
//            Snackbar.make(requireActivity().findViewById(android.R.id.content), getString(R.string.GPS_ERROR), Snackbar.LENGTH_SHORT).show();
//            mNearByUsers.setChecked(false);
//        }

//        getNearbyUsers(0, lat, longi, numberPicker.getRightIndex());
    }


    public void updateLocationInServer(double lati, double longitude) {
        Log.d(TAG, "updateLocationInServer: " + lati + "  " + longitude);
        LocationMethods.updateLocation(user_current_id, lati, longitude, new LocationMethods.OnUpdateListener() {
            @Override
            public void onUpdate() {
                Log.d(TAG, "onUpdate: ");
            }

            @Override
            public void onServerException(String ex) {
                Log.d(TAG, "onServerException: " + ex);
                Snackbar.make(requireActivity().findViewById(android.R.id.content), getString(R.string.GPS_ERROR), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String ex) {
                Log.d(TAG, "onFailure: " + ex);
                Snackbar.make(requireActivity().findViewById(android.R.id.content), getString(R.string.GPS_ERROR), Snackbar.LENGTH_SHORT).show();
            }
        });
    }


}
