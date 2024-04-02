package com.example.taxicle_driver.adapter;

import static com.mapbox.turf.TurfConstants.UNIT_METERS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxicle_driver.BookingInfo;
import com.example.taxicle_driver.R;
import com.example.taxicle_driver.Model.AcceptedBooking;
import com.example.taxicle_driver.Model.AvailableDriver;
import com.example.taxicle_driver.Model.Booking;
import com.example.taxicle_driver.Model.BookingPassenger;
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
import com.mapbox.geojson.Point;
import com.mapbox.turf.TurfConstants;
import com.mapbox.turf.TurfMeasurement;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class BookingPassengerAdapter extends FirebaseRecyclerAdapter<BookingPassenger, BookingPassengerAdapter.myViewHolder> {
    FirebaseAuth auth;
    FirebaseUser user;
    Point point;
    public BookingPassengerAdapter(@NonNull FirebaseRecyclerOptions<BookingPassenger> options, Point point) {
        super(options);

        this.auth = FirebaseAuth.getInstance();
        this.user = this.auth.getCurrentUser();
        this.point = point;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull BookingPassenger model) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference passengerRef, bookingRef;

//      Get Passenger Name
        passengerRef = db.getReference(Passenger.class.getSimpleName());
        passengerRef.child(model.getId()).addValueEventListener(new ValueEventListener() {
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

//      Get Passenger Pick-Up and Drop-off Location
        bookingRef = db.getReference(Booking.class.getSimpleName());
        bookingRef.child(model.getId()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Point pointPickUp;
                if (snapshot.exists()) {
                    Booking booking = snapshot.getValue(Booking.class);
                    assert booking != null;

                    pointPickUp = Point.fromLngLat(booking.getPickUpLongitude(), booking.getPickUpLatitude());

//                  Display Pick-Up Location
                    holder.pickupLocation.setText("from: "+booking.getPickUplocationName());

//                  Display Drop-Off Location
                    holder.dropoffLocation.setText("to: "+booking.getDropOffLocationName());

//                  Display Distance from driver to Pick-Up Location

                    double distance = TurfMeasurement.distance(
                            pointPickUp,
                            BookingPassengerAdapter.this.point,
                            UNIT_METERS
                    );

                    if (distance >= 1000) {
                        distance = TurfMeasurement.distance(
                                pointPickUp,
                                BookingPassengerAdapter.this.point,
                                TurfConstants.UNIT_KILOMETERS
                        );
                        holder.distance.setText(String.format("%skm", NumberFormat.getNumberInstance(Locale.US).format(distance)));
                    } else {
                        holder.distance.setText(String.format("%sm", NumberFormat.getNumberInstance(Locale.US).format(distance)));
                    }



                    DatabaseReference driverRef = db.getReference(AvailableDriver.class.getSimpleName()).child(user.getUid());

                    //        Accept Booking Function
                    holder.accept.setOnClickListener(v -> {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isAccepted", true);
                        hashMap.put("accepted", true);
                        hashMap.put("driver", user.getUid());

                        FirebaseDatabase.getInstance().getReference(Booking.class.getSimpleName())
                                .child(model.getId()).updateChildren(hashMap);

                        holder.accept.setText("Booking Accepted");

                        Intent intent = new Intent(holder.passengerName.getContext(), BookingInfo.class);
                        driverRef.removeValue();
                        v.getContext().startActivity(intent);

//                        set book accepted
                        AcceptedBooking acceptedBooking = new AcceptedBooking();
                        acceptedBooking.setPassengerId(booking.getId());
                        acceptedBooking.setDriverId(user.getUid());

                        FirebaseDatabase.getInstance().getReference(AcceptedBooking.class.getSimpleName())
                                .child(user.getUid()).setValue(acceptedBooking);
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.passenger_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView passengerName, pickupLocation, dropoffLocation, distance;
        Button accept;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            passengerName = itemView.findViewById(R.id.tv_passenger_name);
            pickupLocation = itemView.findViewById(R.id.pick_up_location);
            dropoffLocation = itemView.findViewById(R.id.drop_off_location);
            distance = itemView.findViewById(R.id.distance);

            accept = itemView.findViewById(R.id.btnSelect);

        }
    }






    private Address getGeoCode(Point point, Context context) {
        Geocoder geocoder = new Geocoder(context);
        Address address = null;
        try {
            List<Address> addresses = geocoder.getFromLocation(point.latitude(), point.longitude(), 1);
            if (addresses != null && addresses.size() > 0) {
                address = addresses.get(0);
                String locationName = address.getAddressLine(0); // Full address

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

}
