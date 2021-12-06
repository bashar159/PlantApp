package com.example.task14.HelperAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task14.Activity.HelperFragmnet.HelperMap;
import com.example.task14.Classes.HelperDialog;
import com.example.task14.Classes.Model;
import com.example.task14.Classes.ToHelperModel;
import com.example.task14.R;
import com.example.task14.UserAdapter.DropDwonListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ToHelperAdapter extends RecyclerView.Adapter<ToHelperAdapter.ViewHolder> {
    Context context;
    ArrayList<ToHelperModel> list ;
    ArrayList<String> id;
    public Activity c;
    public Model model;
    public ToHelperAdapter(Context context, ArrayList<ToHelperModel> list , ArrayList<String> id , Activity p) {
        this.context = context;
        this.list = list;
        this.id = id;
        this.c = p;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_helper_layout, parent, false);
        return new ToHelperAdapter.ViewHolder(inflate);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(list.get(position).getName()+" Needs Help!");
        holder.distance.setText(list.get(position).getDistance());


        holder._true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference users = FirebaseDatabase.getInstance().getReference("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()){
                            Log.e("Task","Successfully");
                            model = new Model(snapshot.child("name").getValue(String.class)
                                    ,snapshot.child("tExperience").getValue(String.class)
                                    ,snapshot.child("lExperience").getValue(String.class)
                                    ,snapshot.child("phoneNumber").getValue(String.class)
                                    ,snapshot.child("userId").getValue(String.class));

                            Log.println(Log.ASSERT,"toString",model.toString());

                            DatabaseReference toUserRef = FirebaseDatabase.getInstance().getReference("toUser")
                                    .child(id.get(position)).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                            DatabaseReference ordersRef =  FirebaseDatabase.getInstance().getReference("orders")
                                    .child(id.get(position)).child(FirebaseAuth.getInstance().getCurrentUser().getUid());


                            toUserRef.setValue(new Model(model.getName(),model.gettExperience()
                                    ,model.getlExperience(),model.getPhoneNumber(),model.getUserId()))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Log.println(Log.ASSERT,"Inside adapter  ","onComplete");
                                                HelperDialog helperDialog = new HelperDialog(c,id,list,position);
                                                helperDialog.show();
                                            }

                                        }
                                    });

                            ordersRef.setValue(new Model(model.getName(),model.gettExperience()
                                    ,model.getlExperience(),model.getPhoneNumber(),model.getUserId()))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                HelperDialog helperDialog = new HelperDialog(c,id,list,position);
                                                helperDialog.show();
                                            }

                                        }
                                    });


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        holder._false.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "FalseImage Click", Toast.LENGTH_SHORT).show();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("toHelper")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(id.get(position));

                databaseReference.setValue(null);

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name , distance ;
        ImageView _true , _false;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.onHelperUserName);
            distance = itemView.findViewById(R.id.distanceText);

            _false = itemView.findViewById(R.id.cancle);
            _true = itemView.findViewById(R.id.ok);

        }
    }
}
