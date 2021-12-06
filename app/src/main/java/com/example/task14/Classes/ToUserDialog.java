package com.example.task14.Classes;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.task14.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ToUserDialog extends Dialog
        implements  ActivityCompat.OnRequestPermissionsResultCallback{

    public Activity c;
    public Dialog d;

    public ArrayList<String> idList;
    public int position;
    public final int REQUEST_CODE = 55;
    private static final int REQUEST_CALL = 1;

    public ToUserDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    public ToUserDialog(Activity a , int position , ArrayList<String> list) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.idList=list;
        this.position=position;
    }


    private TextView name , tExp ;
    private ImageView cancel , phone ;

    private RatingBar ratingBar ;

    private FirebaseDatabase toUserDatabase;
    private DatabaseReference toUSerREf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.to_user_dialog);

        name = findViewById(R.id.toUserNAmeTExt);
        ratingBar = findViewById(R.id.ratingBar);
        tExp = findViewById(R.id.toUserEXPType);

        cancel = findViewById(R.id.cancelImage);
        phone = findViewById(R.id.contactImage);


        toUserDatabase = FirebaseDatabase.getInstance();
        toUSerREf = toUserDatabase.getReference("users")
        .child(idList.get(position));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        getData();

    }

    public void getData(){
        toUSerREf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("name").getValue(String.class));
                tExp.setText(snapshot.child("tExperience").getValue(String.class));
                String rating = snapshot.child("lExperience").getValue(String.class);
                String _phone_ = snapshot.child("phoneNumber").getValue(String.class);
                rating = rating.toLowerCase();
                {
                    if (rating.equals("expert")) {
                        ratingBar.setRating(5f);

                    } else if (rating.equals("high")) {
                        ratingBar.setRating(4f);

                    } else if (rating.equals("medium")) {
                        ratingBar.setRating(3.5f);

                    } else if (rating.equals("low")) {
                        ratingBar.setRating(2.5f);
                    }
                }

                phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CallPhone(_phone_);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void CallPhone(@NonNull String PHONE){


            String s = "tel:" + PHONE;
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(s));

            //getContext().startActivity(intent);


        if (ActivityCompat.checkSelfPermission( c.getApplicationContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            c.startActivity(intent);
        }
        else {
            ActivityCompat.requestPermissions(
                    c,
                    new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_CODE);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case REQUEST_CODE :
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getContext(), "Permissao aceite", Toast.LENGTH_SHORT).show();
                }
            break;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }
}
