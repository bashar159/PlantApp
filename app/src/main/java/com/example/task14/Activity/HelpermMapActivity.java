package com.example.task14.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.task14.Activity.HelperFragmnet.HelperMap;
import com.example.task14.R;

public class HelpermMapActivity extends AppCompatActivity {


    ImageView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helperm_map);

        switchFragment(new HelperMap());

        view=findViewById(R.id.helperBack_image);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void switchFragment(Fragment fragment){

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.helperMapFrameLayout, fragment).commit();


    }
}