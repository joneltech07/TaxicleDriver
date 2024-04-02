package com.example.taxicle_driver.adapter;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxicle_driver.AdvanceBookDetails;
import com.example.taxicle_driver.Model.AdvanceBooking;
import com.example.taxicle_driver.Model.Passenger;
import com.example.taxicle_driver.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AcceptedAdvanceBookingAdapter extends FirebaseRecyclerAdapter<AdvanceBooking, AcceptedAdvanceBookingAdapter.myViewHolder> {

    FirebaseAuth auth;
    FirebaseUser user;

    public AcceptedAdvanceBookingAdapter(@NonNull FirebaseRecyclerOptions<AdvanceBooking> options) {
        super(options);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull AdvanceBooking model) {
        holder.tvPickUp.setText("Pick-up: " + model.getPickUplocationName());
        holder.tvDropOff.setText("Drop-off: " + model.getDropOffLocationName());
        holder.tvTime.setText(model.getTime());

        FirebaseDatabase.getInstance().getReference(Passenger.class.getSimpleName())
                .child(model.getId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Passenger passenger = snapshot.getValue(Passenger.class);
                            assert passenger != null;

                            holder.tvPassenger.setText("Name: "+passenger.getName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        String positionKey = getRef(position).getKey();


        holder.btnShow.setOnClickListener(v -> {
            Intent intent = new Intent(holder.tvPickUp.getContext(), AdvanceBookDetails.class);
            intent.putExtra("key", positionKey);
            v.getContext().startActivity(intent);
        });

        holder.btnCancel.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.tvPickUp.getContext());
            builder.setTitle("Are you Sure?");
            builder.setMessage("Cancellation can't be undone");

            builder.setPositiveButton("yes", (dialogInterface, i) -> {

                assert positionKey != null;
                FirebaseDatabase.getInstance()
                        .getReference("AcceptedAdvanceBooking")
                        .child(user.getUid())
                        .child(positionKey)
                        .removeValue();

                FirebaseDatabase.getInstance()
                        .getReference("PassengerAdvanceBooking")
                        .child(model.getId())
                        .child(positionKey)
                        .child("driverId")
                        .setValue(user.getUid());

                FirebaseDatabase.getInstance()
                        .getReference("PassengerAdvanceBooking")
                        .child(model.getId())
                        .child(positionKey)
                        .removeValue();


                Toast.makeText(holder.tvPickUp.getContext(), "Cancelled.", Toast.LENGTH_SHORT).show();
            });
            builder.setNegativeButton("no", ((dialogInterface, i) -> {
                Toast.makeText(holder.tvPickUp.getContext(), "Aborted.", Toast.LENGTH_SHORT).show();
            }));
            builder.show();
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accepted_book_for_later_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView tvPassenger, tvPickUp, tvDropOff, tvTime;

        Button btnCancel, btnShow;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPassenger = itemView.findViewById(R.id.tv_passenger);
            tvPickUp = (TextView) itemView.findViewById(R.id.tv_pickup);
            tvDropOff = (TextView) itemView.findViewById(R.id.tv_dropoff);
            tvTime = itemView.findViewById(R.id.tv_datetime);
            btnCancel = itemView.findViewById(R.id.btn_cancel);
            btnShow = itemView.findViewById(R.id.btn_show_more);
        }
    }
}
