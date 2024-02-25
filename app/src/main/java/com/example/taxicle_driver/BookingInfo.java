package com.example.taxicle_driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

import com.example.taxicle_driver.constructor.AcceptedBooking;
import com.example.taxicle_driver.constructor.Booking;
import com.example.taxicle_driver.constructor.DriverHistory;
import com.example.taxicle_driver.constructor.Passenger;
import com.example.taxicle_driver.constructor.PassengerHistory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import android.Manifest;

public class BookingInfo extends AppCompatActivity {

    TextView locationPick, locationDrop;
    TextView passName, passPhone, passNote;
    ImageButton navigatePick, navigateDrop;

    Button btnDone;

    Double longPick, latPick, longDrop, latDrop;
    String passId, pickupLocation, dropoffLocation;

    FirebaseAuth auth;
    FirebaseUser user;

    private static final int REQUEST_PHONE_CALL = 1;
    private String phoneNumber = "*143#";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_info);

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
            FirebaseDatabase.getInstance().getReference(AcceptedBooking.class.getSimpleName())
                    .child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()) {
                                AcceptedBooking booking = snapshot.getValue(AcceptedBooking.class);
                                assert booking != null;
                                passId = booking.getPassengerId();

                                FirebaseDatabase.getInstance().getReference(Booking.class.getSimpleName())
                                        .child(booking.getPassengerId()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    Booking booking1 = snapshot.getValue(Booking.class);
                                                    assert booking1 != null;

                                                    longPick = booking1.getPickUpLongitude();
                                                    latPick = booking1.getPickUpLatitude();

                                                    longDrop = booking1.getDropOffLongitude();
                                                    latDrop = booking1.getDropOffLatitude();

                                                    pickupLocation = booking1.getPickUplocationName();
                                                    dropoffLocation = booking1.getDropOffLocationName();

                                                    locationPick.setText(pickupLocation);
                                                    locationDrop.setText(dropoffLocation);
                                                } else {
                                                    FirebaseDatabase.getInstance().getReference(AcceptedBooking.class.getSimpleName())
                                                            .child(user.getUid()).removeValue();
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                FirebaseDatabase.getInstance().getReference(Passenger.class.getSimpleName())
                                        .child(passId).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    Passenger passenger = snapshot.getValue(Passenger.class);
                                                    assert passenger != null;
                                                    passName.setText(passenger.getName());
                                                    passPhone.setText(passenger.getPhone());
                                                    phoneNumber = passenger.getPhone();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                FirebaseDatabase.getInstance().getReference(Booking.class.getSimpleName())
                                        .child(passId).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    Booking booking = snapshot.getValue(Booking.class);
                                                    assert booking != null;
                                                    passNote.setText(booking.getNotes());
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

            navigatePick.setOnClickListener(v -> {
                if (passId != null) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("passId", passId);
                    intent.putExtra("long", longPick);
                    intent.putExtra("lat", latPick);
                    startActivity(intent);
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
                        LocalDateTime.now()
                );

                PassengerHistory passengerHistory = new PassengerHistory(
                        user.getUid(),
                        pickupLocation,
                        dropoffLocation,
                        LocalDateTime.now()
                );

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Do you want to proceed?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    FirebaseDatabase.getInstance().getReference(DriverHistory.class.getSimpleName())
                            .child(user.getUid()).push().setValue(driverHistory);
                    FirebaseDatabase.getInstance().getReference(AcceptedBooking.class.getSimpleName())
                            .child(user.getUid()).removeValue();

                    FirebaseDatabase.getInstance().getReference(PassengerHistory.class.getSimpleName())
                            .child(passId).push().setValue(passengerHistory);
                    FirebaseDatabase.getInstance().getReference(Booking.class.getSimpleName())
                            .child(passId).removeValue();

                    Toast.makeText(BookingInfo.this, "Transport has successfully done", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(BookingInfo.this, "You clicked No", Toast.LENGTH_SHORT).show();
                    }
                });
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
        if (ContextCompat.checkSelfPermission(BookingInfo.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BookingInfo.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
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