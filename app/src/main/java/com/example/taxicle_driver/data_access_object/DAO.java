package com.example.taxicle_driver.data_access_object;

import androidx.annotation.NonNull;

import com.example.taxicle_driver.Model.Passenger;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DAO {

    private DatabaseReference databaseReference;

    public DAO() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Passenger.class.getSimpleName());
    }

    public String getPassengerName(String id)   {
        final String[] passengerName = {null};
        databaseReference.child(id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Passenger passenger1 = snapshot.getValue(Passenger.class);
                    assert passenger1 != null;
                    passengerName[0] = passenger1.getName();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return passengerName[0];
    }

    public Task<Void> add(Passenger passenger){
        return databaseReference.child(passenger.getId()).setValue(passenger);
    }

    // Book now
    public Task<Void> shareLocation(String id, HashMap<String, Object> passenger){

        return databaseReference.child(id).child("sharedLocations").setValue(passenger);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap){
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> cancelBooked(String id) {
        return databaseReference.child(id).child("sharedLocations").removeValue();
    }

    // Advance booking
    public Task<Void> saveLocation (String id, String key, HashMap<String, Object> passenger) {
        return databaseReference.child(id).child("savedLocations").child(key).setValue(passenger);
    }

    public Task<Void> updateSavedLocation (String id, String key, HashMap<String, Object> passenger) {
        return databaseReference.child(id).child("savedLocations").child(key).updateChildren(passenger);
    }
}
