package com.example.task14.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task14.Classes.Model;
import com.example.task14.R;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HelperProfile extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    private DatabaseReference reference;

    private EditText fName, email, password;
    private EditText tExperience, lExperience;
    private EditText phoneNumber;
    private TextView location_text;

    private Model model;
    private Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper_profile);

        initialize();
    }

    public void initialize() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


        fName = findViewById(R.id.helperEdit_signUpFullName_editText);
        email = findViewById(R.id.helperEdit_signUpEmail_editText);
        password = findViewById(R.id.helperEdit_signUpPassword_editText);

        tExperience = findViewById(R.id.helperEdit_signUpTypeOfExp_editText);
        lExperience = findViewById(R.id.helperEdit_signUplevelOfExp_editText);

        location_text = findViewById(R.id.helperEdit_signUpLocation_editText);
        phoneNumber = findViewById(R.id.helperEdit_signUpPhone_editText);
        signUp = findViewById(R.id.helperEdit_signUpButton);

        getData();
        model = new Model();


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference myRef = firebaseDatabase.getReference("users")
                        .child(auth.getCurrentUser().getUid());


                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String name = fName.getText().toString().trim();

                            String tExp = tExperience.getText().toString().trim();
                            String lExp = lExperience.getText().toString().trim();

                            String loc = location_text.getText().toString().trim();
                            String phone = phoneNumber.getText().toString().trim();

                            model = new Model(name,tExp,lExp,loc,phone,"is Helper",FirebaseAuth.getInstance().getCurrentUser().getUid());
                            myRef.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Data Changed Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Error..."+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void getData(){
        reference = firebaseDatabase.getReference("users")
        .child(auth.getCurrentUser().getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    fName.setText(snapshot.child("name").getValue(String.class));
                    tExperience.setText(snapshot.child("tExperience").getValue(String.class));
                    lExperience.setText(snapshot.child("lExperience").getValue(String.class));
                    location_text.setText(snapshot.child("location").getValue(String.class));
                    phoneNumber.setText(snapshot.child("phoneNumber").getValue(String.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error..."+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}