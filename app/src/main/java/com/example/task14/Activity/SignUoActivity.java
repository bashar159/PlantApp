package com.example.task14.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task14.Classes.Model;
import com.example.task14.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SignUoActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    private DatabaseReference reference;

    private EditText fName, email, password;
    private EditText tExperience, lExperience;
    private EditText phoneNumber;
    private TextView location_text;
    private Model model;
    private Button signUp;

    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_uo);
        initialize();
    }

    public void initialize() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


        fName = findViewById(R.id.signUpFullName_editText);
        email = findViewById(R.id.signUpEmail_editText);
        password = findViewById(R.id.signUpPassword_editText);

        tExperience = findViewById(R.id.signUpTypeOfExp_editText);
        lExperience = findViewById(R.id.signUplevelOfExp_editText);

        location_text = findViewById(R.id.signUpLocation_editText);
        phoneNumber = findViewById(R.id.signUpPhone_editText);
        signUp = findViewById(R.id.signUpButton);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        model = new Model();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterClick();
            }
        });
        location_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check permission
                if (ActivityCompat.checkSelfPermission(SignUoActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // when permission granted
                    getLocation();
                } else {
                    //When permission denied
                    ActivityCompat.requestPermissions(SignUoActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });

    }

    @SuppressLint("MissingPermission")

    private void getLocation() {


        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                // Initialize location
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(SignUoActivity.this, Locale.getDefault());

                    //Initialize address list
                    try {
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        //Set Address
                        location_text.setText(Html.fromHtml(
                                "<font color='#6200EE'><b></b><br></font>"
                                + addressList.get(0).getAddressLine(0)
                        ));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void RegisterClick(){

        String text_email = email.getText().toString().trim();
        String text_password = password.getText().toString().trim();
        String name = fName.getText().toString().trim();

        String tExp = tExperience.getText().toString().trim();
        String lExp = lExperience.getText().toString().trim();

        String loc = location_text.getText().toString().trim();
        String phone = phoneNumber.getText().toString().trim();


        if (TextUtils.isEmpty(text_email) || (TextUtils.isEmpty(text_password)) || (TextUtils.isEmpty(name)) ||
                TextUtils.isEmpty(tExp) || TextUtils.isEmpty(lExp) || TextUtils.isEmpty(loc)
        || TextUtils.isEmpty(phone)){
            Toast.makeText(getApplicationContext(), "Empty Credentials", Toast.LENGTH_SHORT).show();
        }else if (text_password.length()<6){
            Toast.makeText(getApplicationContext(), "Password too short", Toast.LENGTH_SHORT).show();
        }else if(!text_email.contains("@")|| !text_email.contains(".com")){
            Toast.makeText(getApplicationContext(), "Email badly formatted", Toast.LENGTH_SHORT).show();
        }

        else {
            registerUser(text_email,text_password);
        }
    }

    private void registerUser(String text_email, String text_password) {
            auth.createUserWithEmailAndPassword(text_email,text_password)
                    .addOnCompleteListener(SignUoActivity.this ,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                String name = fName.getText().toString().trim();

                                String tExp = tExperience.getText().toString().trim();
                                String lExp = lExperience.getText().toString().trim();

                                String loc = location_text.getText().toString().trim();
                                String phone = phoneNumber.getText().toString().trim();

                                model = new Model(name,tExp,lExp,loc,phone,"is Helper",FirebaseAuth.getInstance().getCurrentUser().getUid());

                                reference = firebaseDatabase.getReference("users")
                                        .child(auth.getCurrentUser().getUid());

                                reference.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Registering user successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                                            finish();
                                        }

                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "Cannot Upload User Data", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }else {
                                Toast.makeText(getApplicationContext(), "Registration Failed! \n"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Error\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.println(Log.ASSERT,"error",e.getMessage());
                }
            });

    }

}