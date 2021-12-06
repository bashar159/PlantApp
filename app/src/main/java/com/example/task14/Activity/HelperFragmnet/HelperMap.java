package com.example.task14.Activity.HelperFragmnet;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.task14.Activity.HomePageActivity;
import com.example.task14.Classes.CustomDialogClass;
import com.example.task14.R;
import com.example.task14.Users.Fragment.NearbyHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelperMap extends Fragment implements OnMapReadyCallback {


    public HelperMap() {

    }

    private List<Address> addressList = null;
    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient client;
    public SearchView searchLocation;
    public GoogleMap map;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_helper_map, container, false);

        searchLocation = view.findViewById(R.id.helper_location);

        supportMapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.helperGoogleMap);


        client = LocationServices.getFusedLocationProviderClient(getActivity());
        searchLocation.setQuery(HomePageActivity.LOCATION,true);
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

          search();

        } else {
            //When permission denied
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


        return view;
    }

    public void search(){
        searchLocation.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location_ = searchLocation.getQuery().toString();
                Log.println(Log.ASSERT,"location_","location_="+location_);

                if (location_ !=null || !location_.equals("")){
                    Log.println(Log.ASSERT,"location_","Inside If"+location_);

                    Geocoder geocoder = new Geocoder(getActivity());
                    try {
                        addressList = geocoder.getFromLocationName(location_,1);
                        Log.println(Log.ASSERT,"Size","Address List Size"+addressList.size());

                    }catch (IOException e){
                        Toast.makeText(getActivity(), "Error Bio \n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    if (addressList.size()==0) {
                        Toast.makeText(getActivity(), "No Adress Found", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.println(Log.ASSERT,"above","Above snapshot");

                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                        map.addMarker(new MarkerOptions().position(latLng).title(location_));
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));

                    }

                }
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        supportMapFragment.getMapAsync(this::onMapReady);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
    }

    @Override
    public void onStart() {
        super.onStart();
        search();

    }
}