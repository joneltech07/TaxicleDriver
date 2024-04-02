package com.example.taxicle_driver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.taxicle_driver.databinding.ActivityAdvanceBookingBinding;

public class AdvanceBookingActivity extends AppCompatActivity {

    ActivityAdvanceBookingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdvanceBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new AdvanceBookingFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.item_booking) {
                replaceFragment(new AdvanceBookingFragment());
            } else if (item.getItemId() == R.id.item_accepted) {
                replaceFragment(new AcceptedFragment());
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}