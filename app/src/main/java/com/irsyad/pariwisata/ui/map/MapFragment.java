package com.irsyad.pariwisata.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.IntentSender;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.irsyad.pariwisata.R;
import com.irsyad.pariwisata.adapter.TempatAdapter;
import com.irsyad.pariwisata.api.tempat.ITempat;
import com.irsyad.pariwisata.api.tempat.Tempat;
import com.irsyad.pariwisata.api.tempat.TempatModel;
import com.irsyad.pariwisata.api.tempat.TempatUtil;
import com.irsyad.pariwisata.helper.Endpoint;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapFragment extends Fragment {

    //fungsi gps
    GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    Double myLatitude = 0d;
    Double myLongitude = 0d;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates;
    TempatModel tm = new TempatModel();

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new AsyncTaskInit().execute();
    }

    private void updateLocationUI() {
        try{
            if (mCurrentLocation != null) {
                myLatitude = mCurrentLocation.getLatitude();
                myLongitude = mCurrentLocation.getLongitude();
                if(mGoogleMap != null){ //prevent crashing if the map doesn't exist yet (eg. on starting activity)
                    mGoogleMap.clear();
                    LatLng posisiku = new LatLng(myLatitude, myLongitude);
                    mGoogleMap.addMarker(new MarkerOptions().position(posisiku).title("Posisi Anda"));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(posisiku));
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posisiku, 15));
                    getAllTempat();
                }
            }
        }catch (Exception e){

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        new AsyncTaskResume().execute();
    }

    void getAllTempat(){

        if ( tm.getPesan() == null){
            ProgressDialog progressDialog =new ProgressDialog(getActivity());
            progressDialog.setMessage(Endpoint.mohon_tunggu);
            progressDialog.show();

            ITempat iTempat = TempatUtil.getTempatInterface();
            iTempat.allTempat().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<TempatModel>() {
                @Override
                public void onCompleted() {
                    progressDialog.dismiss();
                }
                @Override
                public void onError(Throwable e) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),"Error " + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onNext(TempatModel tempatModel) {
                    tm.setPesan(tempatModel.getPesan());
                    if (tm.getPesan().isEmpty()){

                    }else{
                        for (Tempat tempat : tm.getPesan()){
                            Double tLatitude = Double.parseDouble(tempat.getLatitude());
                            Double tLongitude = Double.parseDouble(tempat.getLongitude());

                            LatLng lokasiTempat = new LatLng(tLatitude, tLongitude);
                            mGoogleMap.addMarker(new MarkerOptions()
                                    .position(lokasiTempat)
                                    .title(tempat.getNama())
                            );
                        }
                    }
                }
            });
        }


    }

    private class AsyncTaskInit extends AsyncTask<Void,Void,Void>{

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

    private class AsyncTaskResume extends AsyncTask<Void,Void,Void>{
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