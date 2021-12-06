package com.example.task14.Classes;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.task14.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoneDialog extends Dialog {

    public Activity c;
    public Dialog d;

    public ArrayList<String> idList=null;
    public int position;

    public static int TO_HELPER_NULL=0;



    public DoneDialog(Activity a , int position , ArrayList<String> list) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.idList=list;
        this.position=position;
    }

    public DoneDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    private TextView name,tExp ;
    private RatingBar ratingBar ;

    private FirebaseDatabase toUserDatabase;
    private DatabaseReference toUSerREf;
    private DatabaseReference helperRef;
    private Button Done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.done_dialog);

        name = findViewById(R.id.DoneNAmeTExt);
        ratingBar = findViewById(R.id.DoneRatingBar);
        tExp = findViewById(R.id.DoneEXPType);

        toUserDatabase = FirebaseDatabase.getInstance();
        toUSerREf = toUserDatabase.getReference("toUser")
        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        Done = findViewById(R.id.DoneButton);

        getData();


    }

    public void getData(){
        toUSerREf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        name.setText(snapshot1.child("name").getValue(String.class));
                        tExp.setText(snapshot1.child("tExperience").getValue(String.class));
                        String userId = snapshot1.child("userId").getValue(String.class);

                        Done.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("rating")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child(userId);


                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            String starRating = ratingBar.getRating() +" Star";
                                            Toast.makeText(c, starRating, Toast.LENGTH_SHORT).show();
                                            databaseReference.setValue(starRating);


                                        DatabaseReference databaseReference1
                                                = FirebaseDatabase.getInstance().getReference("toUser")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        databaseReference1.setValue(null);
                                        deleteRef();
                                        dismiss();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(c, "Error...\n"+error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void deleteRef(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String user = dataSnapshot.child("user").getValue(String.class);
                        if (user.equals("is Helper")){
                            String helperId = dataSnapshot.child("userId").getValue(String.class);

                            helperRef = toUserDatabase.getReference("toHelper")
                                    .child(helperId).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                            helperRef.setValue(null);
                            Log.println(Log.ASSERT,"delete","Data deleted successfully");
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "toHelper Error...\n"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
