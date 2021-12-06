package com.example.task14.Classes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.task14.Activity.HelperFragmnet.HelperMap;
import com.example.task14.Activity.HelpermMapActivity;
import com.example.task14.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HelperDialog extends Dialog {

    int position;
    ArrayList<ToHelperModel> list ;
    ArrayList<String> id;



    public Activity c;
    public Dialog d;

    public HelperDialog(Activity a ,  ArrayList<String> id ,  ArrayList<ToHelperModel> list , int position) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.id = id;
        this.list=list;
        this.position = position;
    }
    TextView nameText,carType,carColor;
    ImageView true_,false_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.helper_dialog);

         nameText = findViewById(R.id.helperNameText);
         carType = findViewById(R.id.helperCarType);
         carColor = findViewById(R.id.helperCarColor);

         true_ = findViewById(R.id.helperDialogTrue);
         false_ = findViewById(R.id.helperDialogLocation);


        nameText.setText(list.get(position).getName());
        carColor.setText(list.get(position).getCarColor());
        carType.setText(list.get(position).getCarType());

        true_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c, "Request sent to user successfully", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        false_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.startActivity(new Intent(c.getApplicationContext(), HelpermMapActivity.class));

            }
        });


    }


    public void getData(){

    }
}


