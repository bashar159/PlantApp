package com.example.task14.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.task14.Activity.HomePageActivity;
import com.example.task14.Classes.UserModel;
import com.example.task14.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserSignUp extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    private DatabaseReference reference;

    private EditText fName, email, password;
    private EditText carType, carColor;
    private EditText phoneNumber;
    private EditText careModel;
    private UserModel model;


    private Button signUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        initialize();

    }

    public void initialize() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


        fName = findViewById(R.id.userSignUpFullName_editText);
        email = findViewById(R.id.userSignUpEmail_editText);
        password = findViewById(R.id.userSignUpPassword_editText);

        carType = findViewById(R.id.userSignUpTypeOfExp_editText);
        carColor = findViewById(R.id.userSignUplevelOfExp_editText);

        careModel = findViewById(R.id.userSignUpLocation_editText);
        phoneNumber = findViewById(R.id.userSignUpPhone_editText);
        signUp = findViewById(R.id.userSignUpButton);



        model = new UserModel();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterClick();
            }
        });

    }

    public void RegisterClick(){

        String text_email = email.getText().toString().trim();
        String text_password = password.getText().toString().trim();
        String name = fName.getText().toString().trim();

        String tExp = carType.getText().toString().trim();
        String lExp = carColor.getText().toString().trim();

        String loc = careModel.getText().toString().trim();
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
                .addOnCompleteListener(UserSignUp.this ,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            String name = fName.getText().toString().trim();

                            String tExp = carType.getText().toString().trim();
                            String lExp = carColor.getText().toString().trim();

                            String loc = careModel.getText().toString().trim();
                            String phone = phoneNumber.getText().toString().trim();

                            model = new UserModel(name,tExp,lExp,loc,phone,"is User",FirebaseAuth.getInstance().getCurrentUser().getUid(),"As-Salt");

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