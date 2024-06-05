package com.irsyad.pariwisata.base;

import android.annotation.SuppressLint;
import android.content.IntentSender;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class BaseFragment extends Fragment{

    //fungsi gps
   public GoogleMap mGoogleMap;
    public LocationRequest mLocationRequest;
    public Double myLatitude = 0d;
    public Double myLongitude = 0d;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    public static final int REQUEST_CHECK_SETTINGS = 100;
    public FusedLocationProviderClient mFusedLocationClient;
    public SettingsClient mSettingsClient;
    public LocationSettingsRequest mLocationSettingsRequest;
    public LocationCallback mLocationCallback;
    public Location mCurrentLocation;
    public Boolean mRequestingLocationUpdates;


    private void updateLocationUI(){
        if (mCurrentLocation != null) {
            myLatitude = mCurrentLocation.getLatitude();
            myLongitude = mCurrentLocation.getLongitude();
            if(mGoogleMap != null){ //prevent crashing if the map doesn't exist yet (eg. on starting activity)
                mGoogleMap.clear();
                LatLng posisiku = new LatLng(myLatitude, myLongitude);
                mGoogleMap.addMarker(new MarkerOptions().position(posisiku).title("Posisi Anda"));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(posisiku));
            }
        }
    }
    public class AsyncTaskInit extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            mSettingsClient = LocationServices.getSettingsClient(getActivity());
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    mCurrentLocation = locationResult.getLastLocation();
                    onPostExecute(null);

                }
            };

            mRequestingLocationUpdates = false;
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
            mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(mLocationRequest);
            mLocationSettingsRequest = builder.build();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            updateLocationUI();
        }
    }

    public class AsyncTaskResume extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                mSettingsClient
                        .checkLocationSettings(mLocationSettingsRequest)
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                                onPostExecute(null);
                            }
                        })
                        .addOnFailureListener(getActivity(), new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                int statusCode = ((ApiException) e).getStatusCode();
                                switch (statusCode) {
                                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                        Log.i("tag", "Location settings are not satisfied. Attempting to upgrade location settings ");
                                        try {
                                            ResolvableApiException rae = (ResolvableApiException) e;
                                            rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                                        } catch (IntentSender.SendIntentException sie) {
                                            Log.i("tag", "PendingIntent unable to execute request.");
                                        }
                                        break;
                                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                        String errorMessage = "Location settings are inadequate, and cannot be fixed here. Fix in Settings.";
                                        Log.e("tag", errorMessage);
//                                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                                }
                                onPostExecute(null);
                            }
                        });
                return null;
            }catch (Exception e){
                Log.d("gagal", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            updateLocationUI();
        }
    }
}
