package com.example.mypois;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = openOrCreateDatabase("MyPOIS.db",MODE_PRIVATE,null);
        db.execSQL("Create table if not exists POIS("+
                "Title TEXT PRIMARY KEY UNIQUE,"+"Description TEXT, "+"Category TEXT,"+"Location TEXT,"+"Timestamp TEXT)");
        db.close();

    }
    public void InsertPOI(View view){
       Intent intent= new Intent(this, InsertPOI.class);
        startActivity(intent);
    }
    public void SearchPOI(View view){
        Intent intent= new Intent(this, SearchPOI.class);
        startActivity(intent);
    }
    public void EditPOI(View view){

        Intent intent= new Intent(this, EditPOI.class);
        startActivity(intent);
    }

}