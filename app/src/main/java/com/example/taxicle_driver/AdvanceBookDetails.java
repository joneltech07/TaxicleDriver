package com.example.taxicle_driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taxicle_driver.Model.AcceptedBooking;
import com.example.taxicle_driver.Model.AdvanceBooking;
import com.example.taxicle_driver.Model.Booking;
import com.example.taxicle_driver.Model.DriverHistory;
import com.example.taxicle_driver.Model.Notification;
import com.example.taxicle_driver.Model.Passenger;
import com.example.taxicle_driver.Model.PassengerHistory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class AdvanceBookDetails extends AppCompatActivity {
    TextView locationPick, locationDrop;
    TextView passName, passPhone, passNote;
    ImageButton navigatePick, navigateDrop;

    Button btnDone;

    Double longPick, latPick, longDrop, latDrop;
    String passId, pickupLocation, dropoffLocation;
    double totalFare;

    FirebaseAuth auth;
    FirebaseUser user;

    private static final int REQUEST_PHONE_CALL = 1;
    private String phoneNumber = "*143#";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_book_details);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        passName = findViewById(R.id.tv_name);
        passPhone = findViewById(R.id.tv_phone);
        passNote = findViewById(R.id.tv_note);

        locationPick = findViewById(R.id.location_pick);
        locationDrop = findViewById(R.id.location_drop);


        navigatePick = findViewById(R.id.navigate_pick);
        navigateDrop = findViewById(R.id.navigate_drop);

        btnDone = findViewById(R.id.btn_done);

        try {
            FirebaseDatabase.getInstance()
                    .getReference("AcceptedAdvanceBooking")
                    .child(user.getUid())
                    .child(getIntent().getStringExtra("key"))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()) {
                                AdvanceBooking booking = snapshot.getValue(AdvanceBooking.class);
                                assert booking != null;

                                passId = booking.getId();

                                totalFare = booking.getTotalFare();

                                ((TextView)findViewById(R.id.tv_total_fare)).setText(Double.toString(totalFare));

                                locationPick.setText(booking.getPickUplocationName());
                                locationDrop.setText(booking.getDropOffLocationName());

                                longPick = booking.getPickUpLongitude();
                                latPick = booking.getPickUpLatitude();

                                longDrop = booking.getDropOffLongitude();
                                latDrop = booking.getDropOffLatitude();

                                pickupLocation = booking.getPickUplocationName();
                                dropoffLocation = booking.getDropOffLocationName();

                                passNote.setText(booking.getNotes());


//                                get Passenger Info

                                FirebaseDatabase.getInstance().getReference(Passenger.class.getSimpleName())
                                        .child(passId)
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    Passenger passenger = snapshot.getValue(Passenger.class);
                                                    passName.setText(passenger.getName());
                                                    passPhone.setText(passenger.getPhone());
                                                    phoneNumber = passenger.getPhone();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            // Get the current date and time
            LocalDateTime currentDateTime = LocalDateTime.now();

            // Define the format for the string representation
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Format the LocalDateTime object to a string
            String formattedDateTime = currentDateTime.format(formatter);

            navigatePick.setOnClickListener(v -> {
                if (passId != null) {

                    FirebaseDatabase.getInstance().getReference(Notification.class.getSimpleName())
                            .child(passId).child("advance").child("startPick").setValue(true);

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("passId", passId);
                    intent.putExtra("long", longPick);
                    intent.putExtra("lat", latPick);
                    startActivity(intent);

                    navigateDrop.setVisibility(View.VISIBLE);
                }
            });

            navigateDrop.setOnClickListener(v -> {
                if (passId != null) {

                    btnDone.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("passId", passId);
                    intent.putExtra("long", longDrop);
                    intent.putExtra("lat", latDrop);
                    startActivity(intent);
                }
            });

            btnDone.setOnClickListener(v -> {
                DriverHistory driverHistory = new DriverHistory(
                        passId,
                        pickupLocation,
                        dropoffLocation,
                        formattedDateTime,
                        totalFare
                );

                PassengerHistory passengerHistory = new PassengerHistory(
                        user.getUid(),
                        pickupLocation,
                        dropoffLocation,
                        formattedDateTime,
                        totalFare
                );

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Do you want to proceed?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    FirebaseDatabase.getInstance().getReference(DriverHistory.class.getSimpleName())
                            .child(user.getUid()).push().setValue(driverHistory);
                    FirebaseDatabase.getInstance().getReference("AcceptedAdvanceBooking")
                            .child(user.getUid()).child(getIntent().getStringExtra("key")).removeValue();

                    FirebaseDatabase.getInstance().getReference(PassengerHistory.class.getSimpleName())
                            .child(passId).push().setValue(passengerHistory);
                    FirebaseDatabase.getInstance().getReference("PassengerAdvanceBooking")
                            .child(passId).child(getIntent().getStringExtra("key")).removeValue();

                    FirebaseDatabase.getInstance().getReference(Notification.class.getSimpleName())
                            .child(passId).child("advance").child("dropped").setValue(true);

                    Toast.makeText(AdvanceBookDetails.this, "Transport has successfully done", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                });
                builder.setNegativeButton("No", (dialog, which) -> Toast.makeText(AdvanceBookDetails.this, "You clicked No", Toast.LENGTH_SHORT).show());
                AlertDialog dialog = builder.create();
                dialog.show();
            });

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        Button buttonCall = findViewById(R.id.button_call);
        buttonCall.setOnClickListener(v -> makePhoneCall());
    }
    private void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(AdvanceBookDetails.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AdvanceBookDetails.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        } else {
            String dial = "tel:" + phoneNumber;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PHONE_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}