package com.example.taxicle_driver.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxicle_driver.MainActivity;
import com.example.taxicle_driver.R;
import com.example.taxicle_driver.Model.Booking;
import com.example.taxicle_driver.Model.Passenger;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<Booking, MainAdapter.myViewHolder> {

    FirebaseDatabase db;
    DatabaseReference databaseReference;
    public MainAdapter(@NonNull FirebaseRecyclerOptions<Booking> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull Booking model) {
        try {
            db = FirebaseDatabase.getInstance();
            databaseReference = db.getReference(Passenger.class.getSimpleName());
            databaseReference.child(model.getId()).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Passenger passenger = snapshot.getValue(Passenger.class);
                        assert passenger != null;
                        holder.name.setText(passenger.getName());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(holder.notes.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        holder.notes.setText(model.getNotes());
//        holder.location.setText(model.getLocationName());

        holder.btnAccept.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
//                intent.putExtra("long", model.getLongitude());
//                intent.putExtra("lat", model.getLatitude());
                view.getContext().startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(holder.name.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }


            Toast.makeText(holder.btnAccept.getContext(), "Accepted", Toast.LENGTH_SHORT).show();
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img;
        TextView name, notes, location;

        Button btnAccept, btnInfo;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (CircleImageView) itemView.findViewById(R.id.img1);
            name = (TextView) itemView.findViewById(R.id.passenger_name);
            notes = (TextView) itemView.findViewById(R.id.notes);
            location = (TextView) itemView.findViewById(R.id.tv_location_name);

            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnInfo = itemView.findViewById(R.id.btnInfo);
        }
    }
}
