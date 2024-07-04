package com.example.mypois;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;


public class SearchPOI extends AppCompatActivity {
    SQLiteDatabase db;
    SearchView searchView;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_poi);
        searchView= findViewById(R.id.searchBar);
        searchView.clearFocus();
        TextView tv = findViewById(R.id.results);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s){
                tv.setText("");
                db = openOrCreateDatabase("MyPOIS.db",MODE_PRIVATE,null);
                if (!s.equals("")) {
                    Cursor c = db.rawQuery(("Select * from POIS WHERE Title LIKE ? "), new String[]{'%' + s + '%'});
                    ArrayList<POI> pois = new ArrayList<POI>();
                    if (c.moveToFirst()) {
                        do {
                            POI poi = new POI(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4));
                            if (!pois.contains(poi)) {
                                pois.add(poi);
                            }
                        } while (c.moveToNext());
                    }
                    c.close();
                    db.close();
                    for (POI item : pois) {
                        tv.setText(tv.getText() + "\n" + "POI Title: " + item.getTitle() + "\n"  + "POI Description: "+ item.getDescription() + "\n" + "POI Category: " + item.getCategory() + "\n" + "POI Timestamp: "+ item.getTimestamp() + "\n"  + "POI Location: "+ item.getLocation()+"\n");
                    }
                }
                return true;
            }

        });
    }
    public void viewAll(View view){
        db = openOrCreateDatabase("MyPOIS.db",MODE_PRIVATE,null);
        Cursor c = db.rawQuery(("Select * from POIS"), null);
        ArrayList<POI> pois = new ArrayList<POI>();
        if (c.moveToFirst()) {
            do {
                POI poi = new POI(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4));
                if (!pois.contains(poi)) {
                    pois.add(poi);
                }
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        tv = findViewById(R.id.results);
        tv.setText("");
        for (POI item : pois) {
            tv.setText(tv.getText() + "\n" + "POI Title: " + item.getTitle() + "\n"  + "POI Description: "+ item.getDescription() + "\n" + "POI Category: " + item.getCategory() + "\n" + "POI Timestamp: "+ item.getTimestamp() + "\n"  + "POI Location: "+ item.getLocation()+"\n");
        }
    }

}