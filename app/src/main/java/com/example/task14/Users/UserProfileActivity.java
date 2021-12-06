package com.example.task14.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.task14.Classes.Model;
import com.example.task14.Classes.UserModel;
import com.example.task14.MainActivity;
import com.example.task14.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    private DatabaseReference reference;

    private EditText fName, email, password;
    private EditText carType, carColor;
    private EditText phoneNumber;
    private EditText carModel;

    private UserModel model;
    private Button signUp;


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ImageView menu_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initialize();

    }

    public void initialize() {

        drawerLayout = findViewById(R.id.profileDrawer);
        toolbar = findViewById(R.id.profileToolbar);


        navigationView = findViewById(R.id.profileNav_view);

        setSupportActionBar(toolbar);

        menu_image = findViewById(R.id.profileMenu_image);
        menu_image.setOnClickListener(this::add_Nav);


        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


        fName = findViewById(R.id.userEdit_signUpFullName_editText);
        email = findViewById(R.id.userEdit_signUpEmail_editText);
        password = findViewById(R.id.userEdit_signUpPassword_editText);

        carType = findViewById(R.id.userEdit_signUpTypeOfExp_editText);
        carColor = findViewById(R.id.userEdit_signUplevelOfExp_editText);

        carModel = findViewById(R.id.userEdit_signUpLocation_editText);
        phoneNumber = findViewById(R.id.user_signUpPhone_editText);

        signUp = findViewById(R.id.userEdit_signUpButton);

        getData();

        model = new UserModel();


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

                            String tExp = carType.getText().toString().trim();
                            String lExp = carColor.getText().toString().trim();

                            String loc = carModel.getText().toString().trim();
                            String phone = phoneNumber.getText().toString().trim();

                            model = new UserModel(name,tExp,lExp,loc,phone,"is User",FirebaseAuth.getInstance().getCurrentUser().getUid());
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

    private void add_Nav(View view) {

        Toast.makeText(getApplicationContext(), "Image Click", Toast.LENGTH_SHORT).show();
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        navigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "menu Selected", Toast.LENGTH_SHORT).show();
            }
        });

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Navigation_drawer_open, R.string.Navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        drawerLayout.openDrawer(Gravity.LEFT);

    }

    private void getData(){
        reference = firebaseDatabase.getReference("users")
                .child(auth.getCurrentUser().getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    fName.setText(snapshot.child("fName").getValue(String.class));
                    carType.setText(snapshot.child("carColor").getValue(String.class));
                    carColor.setText(snapshot.child("carColor").getValue(String.class));
                    carModel.setText(snapshot.child("carModel").getValue(String.class));
                    phoneNumber.setText(snapshot.child("phoneNumber").getValue(String.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error..."+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_logout:
                Toast.makeText(getApplicationContext(), "LogOut", Toast.LENGTH_SHORT).show();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case R.id.nav_orders:
                Toast.makeText(getApplicationContext(), "Order Selected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),OrdersActivity.class));
                finish();
                break;
        }

        return true;
    }

}