package com.example.tpkkoja;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.tpkkoja.Content.CobaUpload;
import com.example.tpkkoja.Content.UploadPatrolSafe;
import com.example.tpkkoja.Services.PreferenceHelper;

public class MainActivity extends AppCompatActivity {

    PreferenceHelper preferenceHelper;
    CardView cv_safe;
    CardView cv_risk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceHelper = new PreferenceHelper(this);

        cv_safe = findViewById(R.id.cv_safe);
        cv_safe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UploadPatrolSafe.class));
            }
        });

        cv_risk = findViewById(R.id.cv_risk);
        cv_risk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CobaUpload.class));
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        TextView title = toolbar.findViewById(R.id.toolbar_judul);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


    }

}
