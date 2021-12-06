package com.example.task14.Users.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task14.Classes.CustomDialogClass;
import com.example.task14.Classes.DoneDialog;
import com.example.task14.Classes.RecyclerViewOnClick;
import com.example.task14.Classes.ToUserDialog;
import com.example.task14.Classes.UserModel;
import com.example.task14.R;
import com.example.task14.UserAdapter.DropDwonListAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GoogleMaps extends Fragment implements OnMapReadyCallback {



    public GoogleMaps() {

    }


    private FirebaseDatabase toAddHelperDataBase;
    private DatabaseReference userRef , helperRef , childRef;

    public UserModel toHelperModel;

    public static ArrayList<String> idList;
    private SupportMapFragment searchFragment;
    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient client;
    public  SearchView searchLocation;
    public  GoogleMap map;
    private RecyclerView recyclerView;
    private  DropDwonListAdapter adapter;
    private TextView spinner;
    private  List<Address> addressList = null;
    private  ArrayList<String> list;

    private String pos;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_google_maps, container, false);
        list = new ArrayList<>();
        idList = new ArrayList<>();
        list.add("Helper Location");
        spinner = view.findViewById(R.id.spinner);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("toUser")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    DoneDialog doneDialog = new DoneDialog(getActivity());
                    doneDialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView.getVisibility() == View.VISIBLE){
                    spinner.setRotation(180);
                    recyclerView.setVisibility(View.GONE);
                }else {
                    recyclerView.setVisibility(View.VISIBLE);
                    spinner.setRotation(spinner.getRotation()+180);
                }
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.addOnItemTouchListener(new RecyclerViewOnClick(getContext(), recyclerView, new RecyclerViewOnClick.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                searchLocation.setQuery(list.get(position),true);
                getUserData();
                get_get_Location(position);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        adapter = new DropDwonListAdapter(getContext(),list,getActivity());

        recyclerView.setAdapter(adapter);
        searchLocation = view.findViewById(R.id.sv_location);

         supportMapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.googleMap);


        // initialize  Fused Location
        client = LocationServices.getFusedLocationProviderClient(getActivity());

        //Check permission
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // when permission granted
            getLocation();
            getDBLocation();
            search();

        } else {
            //When permission denied
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        return view;
    }

    @SuppressLint("MissingPermission")

    public void getLocation() {
        Log.println(Log.ASSERT,"getLocation","getLocation");

        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Log.println(Log.ASSERT,"loc!null","OnSuccess");
                if (location !=null){
                    Log.println(Log.ASSERT,"loc!null","Mio bio");
                    // Sync map
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            // Initilaize lat lang
                            LatLng latLng = new LatLng(location.getLatitude()
                            ,location.getLongitude());

                            String position = latLng.toString();
                            pos = position.substring(9);
                            pos = pos.replace("(","");
                            pos = pos.replace(")","");
                            list.add(" currentlocation "+pos);
                            adapter.notifyDataSetChanged();
                            Log.println(Log.ASSERT,"Stromg locatiob",pos);
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("location");
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        reference.setValue(pos);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getContext(), "Error..\n"+error.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                            // create marker options
                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title("I am there");

                            //Zoom map
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                            // aDD MARKER ON MAP
                             googleMap.addMarker(options);

                        }
                    });
                }
            }
        });

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
                return false;
            }
                     @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        supportMapFragment.getMapAsync(this::onMapReady);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44 ){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // When permission grated
                // Call Method
                getLocation();
            }
        }
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
    }

    public void getDBLocation(){

     DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");


     databaseReference.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {

             if (snapshot.exists()){
                 for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                     if (dataSnapshot.child("user").getValue(String.class).equals("is Helper")){


                     String LocationFromDataBase = dataSnapshot.child("helperLocation").getValue(String.class);
                     String helperId = dataSnapshot.child("userId").getValue(String.class);
                     Log.println(Log.ASSERT,"in snapshot","LocationFromDataBase "+LocationFromDataBase);

                     if (!list.contains(LocationFromDataBase) && LocationFromDataBase!=null){
                         list.add(LocationFromDataBase);
                         adapter.notifyDataSetChanged();

                     }

                     if (!idList.contains(helperId) && helperId!=null){
                         idList.add(helperId);
                         Log.println(Log.ASSERT,"idList",idList.toString());
                     }

                   }


                 }
                 Log.println(Log.ASSERT,"in snapshot","exists");

             }

             else {
                 Log.println(Log.ASSERT,"in snapshot","Not exists");
             }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {
             Toast.makeText(getContext(), "Error..\n"+error.getMessage(), Toast.LENGTH_SHORT).show();
         }
     });
 }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void get_get_Location(int position) {
        toAddHelperDataBase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = toAddHelperDataBase.getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.println(Log.DEBUG,"Above","Above the snapshot.exists()");
                if (snapshot.exists()){
                    System.out.println("");
                    Log.println(Log.DEBUG,"OnBio","Inside the snapshot.exists()");
                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                        String user = snapshot1.child("user").getValue(String.class);
                        String _location_= snapshot1.child("helperLocation").getValue(String.class);
                        String id = snapshot1.child("userId").getValue(String.class);
                        Log.println(Log.DEBUG,"snapshot1","Inside the snapshot1\n"+_location_ +" "+list.get(position));

                        if (user.equals("is Helper") && list.get(position).equals(_location_) && idList.get(position-1).equals(id)){
                            System.out.println("Inside the isHelper");

                            Log.println(Log.DEBUG,"isHelper","Inside the isHelper");

                            String helperId = snapshot1.child("userId").getValue(String.class);
                            Log.e("helperId",helperId);
                            helperRef = toAddHelperDataBase.getReference("toHelper")
                                    .child(helperId).child(toHelperModel.getUserId());

                            Log.e("getUserId",toHelperModel.getUserId());


                            helperRef.setValue(toHelperModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        System.out.println("Inside the onComplete");
                                        Log.d("onComplete","Inside the onComplete");

                                        Toast.makeText(getContext(), "Request Send Successfully", Toast.LENGTH_SHORT).show();

                                        CustomDialogClass cc1 = new CustomDialogClass(getActivity());
                                        ToUserDialog cc2 = new ToUserDialog(getActivity(),position-1,idList);
                                        DoneDialog cc3 = new DoneDialog(getActivity(),position-1,idList);




                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("toUser")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                        databaseReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()){
                                                    cc3.show();
                                                }else
                                                    {
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            cc1.show();

                                                            Handler handler2 = new Handler();
                                                            handler2.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    cc1.dismiss();
                                                                    cc2.show();


                                                                }
                                                            },3000);

                                                        }
                                                    },3000);
                                                    }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                    }
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "toHelper Error...\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                }
                else {
                    Toast.makeText(getContext(), "Snapshot Dose Not Exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(getContext(), "Error...\n"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });


    }
    public void getUserData(){
        DatabaseReference _databaseReference_ = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Log.println(Log.ASSERT , "bbb",FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
        _databaseReference_.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {

                if (snapshot1.exists()) {
                    Log.d("OnBio", "Inside the snapshot.exists()");

                        String user = snapshot1.child("user").getValue(String.class);
                        Log.d("snapshot1", "Inside the snapshot1");
                        Log.println(Log.ASSERT , "bbb",user);

                        if (user.equals("is User"))
                        {
                            Log.d("isUser", "Inside the isUser");

                            toHelperModel = new UserModel();

                                        toHelperModel =
                                         new UserModel(snapshot1.child("fName").getValue(String.class)
                                        ,snapshot1.child("carType").getValue(String.class)
                                        ,snapshot1.child("carColor").getValue(String.class)
                                        ,snapshot1.child("carModel").getValue(String.class)
                                        ,snapshot1.child("phoneNumber").getValue(String.class)
                                        ,snapshot1.child("user").getValue(String.class)
                                        ,snapshot1.child("userId").getValue(String.class)
                                         ,snapshot1.child("location").getValue(String.class));
                                        Log.println(Log.ASSERT,"sad",toHelperModel.toString());
                        }

                }else{
                    Toast.makeText(getContext(), "Snapshot not exists", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

