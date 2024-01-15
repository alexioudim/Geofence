package gr.dit.hua.lab.geofence;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(view->{
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(),MapsActivity.class);
            startActivity(intent);
            finish();
        });
    }
}