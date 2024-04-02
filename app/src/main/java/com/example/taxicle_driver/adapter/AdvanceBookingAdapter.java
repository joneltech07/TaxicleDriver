package com.example.taxicle_driver.adapter;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
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

public class AdvanceBookingAdapter extends FirebaseRecyclerAdapter<AdvanceBooking, AdvanceBookingAdapter.myViewHolder> {

    FirebaseAuth auth;
    FirebaseUser user;

    // Define a constant for notification channel ID
    private static final String CHANNEL_ID = "advance_booking_channel";
    // Method to create notification channel (for Android Oreo and above)
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "This is a Channel Name"; //context.getString(R.string.channel_name);
            String description = "This is a Channel Description"; //context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Method to display notification
    private void showNotification(Context context, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create notification channel
        createNotificationChannel(context);

        // Build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Advance Booking Notification")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Show notification
        notificationManager.notify(0, builder.build());
    }


    public AdvanceBookingAdapter(@NonNull FirebaseRecyclerOptions<AdvanceBooking> options) {
        super(options);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull AdvanceBooking model) {
        holder.tvPickUp.setText("Pick-up: " + model.getPickUplocationName());
        holder.tvDropOff.setText("Drop-off: " + model.getDropOffLocationName());
        holder.tvTime.setText(model.getTime());


        String positionKey = getRef(position).getKey();

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

        AdvanceBooking booking = new AdvanceBooking();
        booking.setAccepted(true);
        booking.setId(model.getId());
        booking.setTime(model.getTime());

        booking.setPickUpLatitude(model.pickUpLatitude);
        booking.setPickUpLongitude(model.pickUpLongitude);
        booking.setPickUplocationName(model.pickUplocationName);

        booking.setDropOffLatitude(model.dropOffLatitude);
        booking.setDropOffLongitude(model.dropOffLongitude);
        booking.setDropOffLocationName(model.dropOffLocationName);


        holder.btnCancel.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.tvPickUp.getContext());
            builder.setTitle("Are you Sure you want to accept?");

            builder.setPositiveButton("yes", (dialogInterface, i) -> {

                assert positionKey != null;
                FirebaseDatabase.getInstance()
                        .getReference("AcceptedAdvanceBooking")
                        .child(user.getUid())
                        .child(positionKey)
                        .setValue(booking);

                FirebaseDatabase.getInstance()
                        .getReference("AdvanceBooking")
                        .child(positionKey)
                        .removeValue();

                FirebaseDatabase.getInstance()
                        .getReference("PassengerAdvanceBooking")
                                .child(model.getId())
                                        .child(positionKey)
                                                .child("accepted")
                                                        .setValue(true);
                FirebaseDatabase.getInstance()
                        .getReference("PassengerAdvanceBooking")
                        .child(model.getId())
                        .child(positionKey)
                        .child("driverId")
                        .setValue(user.getUid());

                // Show notification
                showNotification(holder.itemView.getContext(), "Booking accepted for time: " + model.getTime());
                Toast.makeText(holder.tvPickUp.getContext(), "Accepted.", Toast.LENGTH_SHORT).show();
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_for_later_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView tvPassenger, tvPickUp, tvDropOff, tvTime;

        Button btnCancel;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPassenger = itemView.findViewById(R.id.tv_passenger);
            tvPickUp = (TextView) itemView.findViewById(R.id.tv_pickup);
            tvDropOff = (TextView) itemView.findViewById(R.id.tv_dropoff);
            tvTime = itemView.findViewById(R.id.tv_datetime);
            btnCancel = itemView.findViewById(R.id.btn_cancel);
        }
    }
}
