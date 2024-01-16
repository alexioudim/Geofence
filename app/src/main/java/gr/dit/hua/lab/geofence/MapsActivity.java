package gr.dit.hua.lab.geofence;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import gr.dit.hua.lab.geofence.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LocationManager manager;

    private List<Circle> circles = new ArrayList<>();

    private LocationListener listener;

    private static final int LOCATION_PERMISSIONS_REQUEST_CODE = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = location -> {
            if (mMap != null) {
                mMap.clear();
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(currentLocation));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
            }
        };
        getLocation();


    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10, listener);
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSIONS_REQUEST_CODE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSIONS_REQUEST_CODE) {
            int count = 0;
            for (String permission : permissions) {
                if (permission == Manifest.permission.ACCESS_FINE_LOCATION) {
                    if (grantResults[count] == PackageManager.PERMISSION_GRANTED) {
                        getLocation();
                    }
                }
                count++;
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(latLng -> {

            Circle existingCircle = getExistingCircle(latLng);

            if (existingCircle != null) {
                // If a circle exists, remove it
                existingCircle.remove();
                circles.remove(existingCircle);
            } else {
                Circle circle = mMap.addCircle(new CircleOptions()
                        .center(latLng)
                        .radius(100)
                        .strokeWidth(2)
                        .strokeColor(Color.RED)
                        .fillColor(Color.BLUE)
                );


                circles.add(circle);
            }


        });

        mMap.setOnCircleClickListener(selectedCircle -> {
            if (circles.contains(selectedCircle)) {
                selectedCircle.remove();
                circles.remove(selectedCircle);
            }
        });
    }

    private Circle getExistingCircle(LatLng latLng) {
        for (Circle circle : circles) {
            float[] distance = new float[1];
            Location.distanceBetween(
                    latLng.latitude, latLng.longitude,
                    circle.getCenter().latitude, circle.getCenter().longitude,
                    distance
            );
            if (distance[0] < circle.getRadius()) {
                return circle;
            }
        }
        return null;
    }
}