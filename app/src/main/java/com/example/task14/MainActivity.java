package com.example.task14;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.task14.Activity.HomePageActivity;
import com.example.task14.Activity.LoginActivity;
import com.example.task14.Users.UserHomePage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    protected Button user,helper;
    public static int userOrHelper = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = findViewById(R.id.userButton);
        helper = findViewById(R.id.helperButton);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                userOrHelper = 0;

            }
        });

        helper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                userOrHelper = 1;

            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();

        DatabaseReference usersRef;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            usersRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("user");
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String jopType = snapshot.getValue(String.class);
                        if (jopType.equals("is User")){
                            startActivity(new Intent(getApplicationContext(), UserHomePage.class));
                            finish();
                        }else if(jopType.equals("is Helper")){
                            startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                            finish();
                        }
                    }

                    else{

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Error..."+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{

        }
    }
}