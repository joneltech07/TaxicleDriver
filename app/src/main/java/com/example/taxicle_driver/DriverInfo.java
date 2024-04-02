package com.example.taxicle_driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.taxicle_driver.Model.Driver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverInfo extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    TextView tvName, tvEmail, tvPhone, tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_info);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        tvPhone = findViewById(R.id.tv_phone);
        tvAddress = findViewById(R.id.tv_address);

        FirebaseDatabase.getInstance().getReference(Driver.class.getSimpleName())
                .child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Driver driver = snapshot.getValue(Driver.class);
                            assert driver != null;

                            tvName.setText(driver.getName());
                            tvEmail.setText(driver.getEmail());
                            tvPhone.setText(driver.getPhone());
                            tvAddress.setText(driver.getAddress());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}