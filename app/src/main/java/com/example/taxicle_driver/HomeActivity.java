package com.example.taxicle_driver;

import static com.mapbox.maps.plugin.gestures.GesturesUtils.getGestures;
import static com.mapbox.maps.plugin.locationcomponent.LocationComponentUtils.getLocationComponent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taxicle_driver.adapter.BookingPassengerAdapter;
import com.example.taxicle_driver.constructor.AcceptedBooking;
import com.example.taxicle_driver.constructor.AvailableDriver;
import com.example.taxicle_driver.constructor.Booking;
import com.example.taxicle_driver.constructor.BookingPassenger;
import com.example.taxicle_driver.constructor.Driver;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.extension.style.layers.properties.generated.TextAnchor;
import com.mapbox.maps.plugin.LocationPuck2D;
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener;

import java.util.HashMap;
import java.util.function.Consumer;

public class HomeActivity extends AppCompatActivity {

// Drawer
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;






    //    Firebase
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }









    Point point;
    DatabaseReference reference = null;
    AvailableDriver availableDriver;








    SwitchMaterial switchMaterial;










    private ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (result) {
            Toast.makeText(HomeActivity.this, "Permission granted!", Toast.LENGTH_SHORT).show();
        }
    });
    private final OnIndicatorBearingChangedListener onIndicatorBearingChangedListener = new OnIndicatorBearingChangedListener() {
        @Override
        public void onIndicatorBearingChanged(double v) {
            try {
                mapView.getMapboxMap().setCamera(new CameraOptions.Builder().bearing(v).build());
            } catch (Exception e) {
                Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    };
    private final OnIndicatorPositionChangedListener onIndicatorPositionChangedListener = new OnIndicatorPositionChangedListener() {
        @Override
        public void onIndicatorPositionChanged(@NonNull Point point) {
            try {
                mapView.getMapboxMap().setCamera(new CameraOptions.Builder().center(point).zoom(17.0).build());
                getGestures(mapView).setFocalPoint(mapView.getMapboxMap().pixelForCoordinate(point));
                HomeActivity.this.point = point;
                if (switchMaterial.isChecked())setToAvailable();

            } catch (Exception e) {
                Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    };
    private final OnMoveListener onMoveListener = new OnMoveListener() {
        @Override
        public void onMoveBegin(@NonNull MoveGestureDetector moveGestureDetector) {
            try {
                getLocationComponent(mapView).removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
                getLocationComponent(mapView).removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
                getGestures(mapView).removeOnMoveListener(onMoveListener);
                floatingActionButton.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public boolean onMove(@NonNull MoveGestureDetector moveGestureDetector) {
            return false;
        }

        @Override
        public void onMoveEnd(@NonNull MoveGestureDetector moveGestureDetector) {

        }
    };








//    Map Vview
    private MapView mapView;
    ImageButton floatingActionButton, showPassenger;








    BookingPassengerAdapter adapter;










    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);





        switchMaterial = findViewById(R.id.status);






        if (ActivityCompat.checkSelfPermission(HomeActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }








        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();









        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.account) {
                Toast.makeText(this, "Account", Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.booking) {
                Intent intent = new Intent(this, BookingInfo.class);
                startActivity(intent);
            }
            return false;
        });

        ImageButton showDrawer = findViewById(R.id.show_drawer);
        showDrawer.setOnClickListener(v -> drawerLayout.open());

        View headerView = navigationView.getHeaderView(0);
        TextView tvUserName = headerView.findViewById(R.id.user_name);
        TextView tvEmail = headerView.findViewById(R.id.email);







//      Display User info
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Driver.class.getSimpleName()).child(user.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Driver driver = snapshot.getValue(Driver.class);
                if (snapshot.exists()) {
                    tvUserName.setText(driver.getName());
                    tvEmail.setText(user.getEmail());
                } else {
                    Toast.makeText(HomeActivity.this, "No data exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        mapView = findViewById(R.id.mapview);
        floatingActionButton = findViewById(R.id.focusLocation);
        showPassenger = findViewById(R.id.show_passengers);







        mapView.getMapboxMap().loadStyleUri("mapbox://styles/jltolentino/clpxx8g5j00jr01re4o8x833g", style -> {

            mapView.getMapboxMap().setCamera(new CameraOptions.Builder().zoom(20.0).build());
            LocationComponentPlugin locationComponentPlugin = getLocationComponent(mapView);
            locationComponentPlugin.setEnabled(true);
            LocationPuck2D locationPuck2D = new LocationPuck2D();
            locationPuck2D.setBearingImage(AppCompatResources.getDrawable(HomeActivity.this,R.drawable.baseline_person_pin_24));
            locationComponentPlugin.setLocationPuck(locationPuck2D);

            locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
            locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
            getGestures(mapView).addOnMoveListener(onMoveListener);

            floatingActionButton.setOnClickListener(view1 -> {
                locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
                locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
                getGestures(mapView).addOnMoveListener(onMoveListener);
                floatingActionButton.setVisibility(View.GONE);
            });





            showPassenger.setOnClickListener(v -> showBookingPassengers());








//      Switch Button
            switchMaterial.setOnClickListener(v -> {
                if (switchMaterial.isChecked()) {
                    setToAvailable();
                    showBookingPassengers();
                    Toast.makeText(HomeActivity.this, "Set Status to Available", Toast.LENGTH_SHORT).show();
                } else {
                    setToUnAvailable();
                    Toast.makeText(HomeActivity.this, "Set Status to UnAvailable", Toast.LENGTH_SHORT).show();
                }
            });

        });







        switchMaterialState();



    }





    private void switchMaterialState() {
        FirebaseDatabase.getInstance().getReference(AvailableDriver.class.getSimpleName())
                .child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        switchMaterial.setChecked(snapshot.exists());
                       if (snapshot.exists()) {
                           switchMaterial.setText("Available");

                       } else {
                           switchMaterial.setText("Not Available");

                       }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }









    private void showBookingPassengers() {
        try {
            // Inflate the bottom dialog layout
            View dialogView = LayoutInflater.from(this).inflate(R.layout.passenger_booking, null);

            // Create a dialog without a title
            Dialog bottomDialog = new Dialog(this);
            bottomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            bottomDialog.setCancelable(false);
            bottomDialog.setContentView(dialogView);

            // Set the dialog to appear at the bottom of the screen
            Window window = bottomDialog.getWindow();
            if (window != null) {
                window.setGravity(android.view.Gravity.BOTTOM);
                window.setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            }


            RecyclerView recyclerView = bottomDialog.findViewById(R.id.rv);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));


            FirebaseRecyclerOptions<BookingPassenger> options =
                    new FirebaseRecyclerOptions.Builder<BookingPassenger>()
                            .setQuery(
                                    FirebaseDatabase.getInstance().getReference(AvailableDriver.class.getSimpleName()).child(user.getUid()).child("passengers"), BookingPassenger.class
                            )
                            .build();
            adapter = new BookingPassengerAdapter(options, HomeActivity.this.point);
            recyclerView.setAdapter(adapter);
            adapter.startListening();


            bottomDialog.findViewById(R.id.ib_close).setOnClickListener(v -> {
                bottomDialog.dismiss();
            });


//            Check if Passenger Booking exists
            FirebaseDatabase.getInstance().getReference(AvailableDriver.class.getSimpleName())
                    .child(user.getUid()).child("passengers").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                showPassenger.setVisibility(View.VISIBLE);
                                bottomDialog.show();
                            } else {
                                showPassenger.setVisibility(View.GONE);
                                bottomDialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        } catch (Exception e) {
            TextView tvErr = findViewById(R.id.tv_error);
            tvErr.setVisibility(View.VISIBLE);
            tvErr.setText(e.getMessage());
        }




    }







    private void setToAvailable() {
        try {
            reference = FirebaseDatabase.getInstance().getReference(AvailableDriver.class.getSimpleName());

            HashMap hashMap = new HashMap<String, Object>();
            hashMap.put("id", user.getUid());
            hashMap.put("latitude", point.latitude());
            hashMap.put("longitude", point.longitude());

            reference.child(user.getUid()).updateChildren(hashMap);
        } catch (Exception e) {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }







    private void setToUnAvailable() {
        reference.child(user.getUid()).removeValue();
        reference = null;
    }
}
