package com.example.mypois;
import androidx.annotation.NonNull;
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
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Timestamp;


public class EditPOI extends AppCompatActivity implements LocationListener {
    SQLiteDatabase db;
    SearchView searchViewEdit;

    String forDeletion;
    String currentLocation;
    LocationManager locationManager;
    Button updatebutton;
    Button deletebutton;
    Button changeLocation;
    Button changeTimestamp;
    TextView title;
    TextView description;
    TextView location;
    TextView timestamp;
    Spinner spinnerLocations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deletebutton = findViewById(R.id.delete);
        updatebutton = findViewById(R.id.update);
        title = findViewById(R.id.titleEdit);
        description = findViewById(R.id.DescriptionEdit);
        location = findViewById(R.id.LocationEdit);
        timestamp = findViewById(R.id.TimestampEdit);
        setContentView(R.layout.activity_edit_poi);
        changeLocation = findViewById(R.id.changeLocation);
        changeTimestamp = findViewById(R.id.changeTimestamp);
        spinnerLocations = findViewById(R.id.catgoryEdit);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.POI_Categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerLocations.setAdapter(adapter);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
            return;
        }
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        db = openOrCreateDatabase("MyPOIS.db", MODE_PRIVATE, null);
        searchViewEdit = findViewById(R.id.searchBarEdit);
        searchViewEdit.clearFocus();
        searchViewEdit.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                POI poi = null;
                db = openOrCreateDatabase("MyPOIS.db", MODE_PRIVATE, null);
                if (!s.equals("")) {
                    Cursor c = db.rawQuery(("Select * from POIS WHERE Title LIKE ? "), new String[]{'%' + s.trim() + '%'});
                    if (c.moveToFirst()) {
                        do {
                            poi = new POI(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4));
                            forDeletion = c.getString(0);
                        } while (c.moveToNext());
                    }
                    c.close();
                }
                    if (poi != null) {
                        deletebutton = findViewById(R.id.delete);
                        updatebutton = findViewById(R.id.update);
                        changeLocation = findViewById(R.id.changeLocation);
                        changeTimestamp = findViewById(R.id.changeTimestamp);
                        updatebutton.setEnabled(true);
                        deletebutton.setEnabled(true);
                        changeLocation.setEnabled(true);
                        changeTimestamp.setEnabled(true);
                        title = findViewById(R.id.titleEdit);
                        description = findViewById(R.id.DescriptionEdit);
                        location = findViewById(R.id.LocationEdit);
                        timestamp = findViewById(R.id.TimestampEdit);
                        spinnerLocations = findViewById(R.id.catgoryEdit);
                        title.setText(poi.getTitle());
                        description.setText(poi.getDescription());
                        location.setText(poi.getLocation());
                        timestamp.setText(poi.getTimestamp());
                        setSpinner(poi.getCategory(), spinnerLocations);

                }
            else

            {
                toastMessage("POI not found");
            }
                db.close();
                return true;
        }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        db.close();

    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void setSpinner(String cv, Spinner mySpinner) {
        String compareValue = cv;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.POI_Categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);
        if (compareValue != null) {
            int spinnerPosition = adapter.getPosition(compareValue);
            mySpinner.setSelection(spinnerPosition);
        }
    }

    public void update(View view) {
        db = openOrCreateDatabase("MyPOIS.db", MODE_PRIVATE, null);
        try {
            db.execSQL("UPDATE POIS SET Title = ?, Description = ? , Category = ? , Location=?, Timestamp=?  WHERE Title = ?;", new String[]{title.getText().toString().trim(), description.getText().toString().trim(), spinnerLocations.getSelectedItem().toString(), location.getText().toString(), timestamp.getText().toString(), searchViewEdit.getQuery().toString().trim()});
            toastMessage("updated Successfully");
            title.setText("");
            description.setText("");
            location.setText("");
            timestamp.setText("");
            updatebutton.setEnabled(false);
            deletebutton.setEnabled(false);
            changeTimestamp.setEnabled(false);
            changeLocation.setEnabled(false);
        }catch(SQLiteConstraintException e){
            toastMessage("Title already exists");
        }
        db.close();
    }

    public void delete(View view) {
        db = openOrCreateDatabase("MyPOIS.db", MODE_PRIVATE, null);
        db.execSQL("DELETE FROM POIS WHERE Title = ?", new String[]{forDeletion});
        toastMessage("Deleted Successfully");
        title.setText("");
        description.setText("");
        location.setText("");
        timestamp.setText("");
        updatebutton.setEnabled(false);
        deletebutton.setEnabled(false);
        changeTimestamp.setEnabled(false);
        changeLocation.setEnabled(false);
        db.close();
    }

    public void changeLocation(View view) {
        TextView location = findViewById(R.id.LocationEdit);
        if (currentLocation!=null) {
            location.setText(currentLocation);
        }else{
            toastMessage("Couldn't track you try moving");
        }
    }

    public void changeTimestamp(View view) {
        TextView ts = findViewById(R.id.TimestampEdit);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        ts.setText(timestamp.toString());
    }

    public void onLocationChanged(@NonNull Location location) {
        String latitude = String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());
        currentLocation = latitude.substring(0, 8) + "," + longitude.substring(0, 8);
    }
}
