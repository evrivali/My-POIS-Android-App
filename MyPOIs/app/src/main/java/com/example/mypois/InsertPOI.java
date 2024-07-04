package com.example.mypois;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.time.LocalDate;


public class InsertPOI extends AppCompatActivity implements LocationListener {
SQLiteDatabase db;
LocationManager locationManager;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_poi);
        Spinner spinnerLocations=findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.POI_Categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerLocations.setAdapter(adapter);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
            return;
        }
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        TextView date =  findViewById(R.id.Date);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date.setText(LocalDate.now().toString());
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
    String currentLocation;

    public void saveUserData(View view) {
        db = openOrCreateDatabase("MyPOIS.db",MODE_PRIVATE,null);
        TextView tvtitle = findViewById(R.id.POItitle);
        String title = tvtitle.getText().toString();
        TextView tvdescription = findViewById(R.id.POIdescription);
        String description = tvdescription.getText().toString();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Spinner mySpinner = findViewById(R.id.spinner);
        String category = mySpinner.getSelectedItem().toString();
        if (currentLocation == null) {
            toastMessage("Current location couldn't be found. Try moving");
        } else if(title.equals("Title") || description.equals("Description")) {
            toastMessage("Insert title and description");
        }else {
            try {
                db.execSQL("Insert into POIS (Title,Description,Category,Location,TImestamp) Values(?,?,?,?,?)", new String[]{title.trim(), description.trim(), category.trim(), currentLocation, timestamp.toString()});
                Cursor c = db.rawQuery("Select * from POIS ", null);
                toastMessage("POI added succesfully");
                c.close();
                db.close();
            }catch (SQLiteConstraintException e){
                toastMessage("Title already exists");
            }
        }
    }

    public void onLocationChanged(@NonNull Location location) {
        String latitude = String.valueOf(location.getLatitude());
        String longitude= String.valueOf(location.getLongitude());
        currentLocation= latitude.substring(0,8) +","+ longitude.substring(0,8);;
        TextView txtLocation = findViewById(R.id.Coordinates);
        txtLocation.setText(currentLocation);

    }
    @Override
    public void onPause()
    {
        super.onPause();
        if(isFinishing())
        {
            locationManager.removeUpdates(this);
        }
    }
}