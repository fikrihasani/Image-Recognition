package com.example.mfikrihasani.imagerecognition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button arialImg = findViewById(R.id.arialRecognitor);
        Button thinning = findViewById(R.id.thiningAlgorithm);
        //launch activity for arial img recognition
        arialImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ArialNumberRecogActivity.class);
                startActivity(intent);
            }
        });
        //launch activity for thinning
        thinning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThinningActivity.class);
                startActivity(intent);
            }
        });
    }
}
