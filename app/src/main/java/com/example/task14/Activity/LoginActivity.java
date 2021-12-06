package com.example.task14.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task14.MainActivity;
import com.example.task14.R;
import com.example.task14.Users.UserHomePage;
import com.example.task14.Users.UserSignUp;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText pass , email;
    private Button login;

    private TextView toSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.loginEmail);
        pass = findViewById(R.id.loginPass);
        login = findViewById(R.id.loginLogin);

        toSignUp = findViewById(R.id.dontHaveAccount);

        toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.userOrHelper == 1){
                    startActivity(new Intent(getApplicationContext(),SignUoActivity.class));
                }else {
                    startActivity(new Intent(getApplicationContext(), UserSignUp.class));
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginClick(v);
            }
        });
    }

    public void LoginClick(View view){
        String text_email= email.getText().toString().trim();
        String text_password = pass.getText().toString().trim();
        if (text_email.isEmpty() || text_password.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please Enter All Fields", Toast.LENGTH_SHORT).show();
        }else {

            loginUsers(view,text_email,text_password);
        }
    }
    private void loginUsers(View view,String email, String password) {
        auth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    Log.println(Log.ASSERT,"login",FirebaseAuth.getInstance().getCurrentUser().getUid());
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if ( MainActivity.userOrHelper == 1 && snapshot.child("user").getValue(String.class).equals("is Helper")){
                                        Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                                        finish();
                                    }else if(MainActivity.userOrHelper == 1 && snapshot.child("user").getValue(String.class).equals("is User")){
                                        Toast.makeText(getApplicationContext(), "Not helper Account", Toast.LENGTH_SHORT).show();
                                    }else if(MainActivity.userOrHelper == 0 && snapshot.child("user").getValue(String.class).equals("is User")){
                                        Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), UserHomePage.class));
                                        finish();
                                    }else if(MainActivity.userOrHelper == 0 && snapshot.child("user").getValue(String.class).equals("is Helper")){
                                        Toast.makeText(getApplicationContext(), "Not User Account", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });





                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}