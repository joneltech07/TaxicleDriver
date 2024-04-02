package com.example.taxicle_driver.adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxicle_driver.R;
import com.example.taxicle_driver.Model.DriverHistory;
import com.example.taxicle_driver.Model.Passenger;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HistoryAdapter extends FirebaseRecyclerAdapter<DriverHistory, HistoryAdapter.myViewHolder> {
    FirebaseAuth auth;
    FirebaseUser user;
    public HistoryAdapter(@NonNull FirebaseRecyclerOptions<DriverHistory> options) {
        super(options);

        this.auth = FirebaseAuth.getInstance();
        this.user = this.auth.getCurrentUser();
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull DriverHistory model) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference passengerRef;

//      Get Passenger Name
        passengerRef = db.getReference(Passenger.class.getSimpleName());
        passengerRef.child(model.getPassengerId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Passenger passenger = snapshot.getValue(Passenger.class);
                    assert passenger != null;
                    holder.passengerName.setText(passenger.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.pickupLocation.setText(model.getPickupLocation());
        holder.dropoffLocation.setText(model.getDropoffLocation());

        holder.deleteBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.pickupLocation.getContext());
            builder.setTitle("Are you Sure?");
            builder.setMessage("Deleted data can't be Undo.");

            builder.setPositiveButton("Delete", (dialogInterface, i) -> {
                FirebaseDatabase.getInstance().getReference(DriverHistory.class.getSimpleName())
                        .child(user.getUid())
                        .child(getRef(position).getKey()).removeValue();
            });
            builder.setNegativeButton("Cancel", ((dialogInterface, i) -> {
                Toast.makeText(holder.pickupLocation.getContext(), "Cancelled.", Toast.LENGTH_SHORT).show();
            }));
            builder.show();
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView passengerName, pickupLocation, dropoffLocation;
        Button deleteBtn;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            passengerName = itemView.findViewById(R.id.tv_passenger_name);
            pickupLocation = itemView.findViewById(R.id.tv_pickup_location);
            dropoffLocation = itemView.findViewById(R.id.tv_dropoff_location);

            deleteBtn = itemView.findViewById(R.id.btnDelete);

        }
    }

}
