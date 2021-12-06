package com.example.task14.UserAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task14.Classes.UserModel;
import com.example.task14.R;
import com.example.task14.Users.Fragment.GoogleMaps;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DropDwonListAdapter extends RecyclerView.Adapter<DropDwonListAdapter.ViewHolder>{
    Context context;
    Activity pActivity;
    private static ClickListener clickListener;

    ArrayList<String> usersDropDwonLists  = new ArrayList<>();

   public static ArrayList<String> getLocationName  = new ArrayList<>();


    public DropDwonListAdapter(Context context, ArrayList<String> usersDropDwonLists,Activity pActivity) {
        this.context = context;
        this.usersDropDwonLists = usersDropDwonLists;
        this.pActivity = pActivity;
        this.getLocationName = usersDropDwonLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.drop_dwon_list, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(usersDropDwonLists.get(position));
        if (position==0){
            holder.relativeLayout.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return usersDropDwonLists.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView textView;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.dropDownText);
            relativeLayout = itemView.findViewById(R.id.parentLayout);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);

        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }

/*

    public void distance () {

        Places.initialize(context, "AIzaSyBnnlGOa7qzRFIa-rYm3vssnOdJx2vASnI");
                List<Place.Field>fields =
                        Arrays.asList(Place.Field.ADDRESS
                        , Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY,fields
        ).build(context);
        // Start Activity result
        pActivity.startActivityForResult(intent,100);
    }
    public void destination(){
        sType = "destination";

        List<Place.Field>fields =
                Arrays.asList(Place.Field.ADDRESS
                        , Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY,fields
        ).build(context);
        // Start Activity result
        pActivity.startActivityForResult(intent,100);
    }
*/

    public void setOnItemClickListener(ClickListener clickListener) {
        DropDwonListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}
