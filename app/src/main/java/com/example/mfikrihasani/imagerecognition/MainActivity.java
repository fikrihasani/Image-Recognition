package com.example.mfikrihasani.imagerecognition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import javax.crypto.Mac;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button arialImg = findViewById(R.id.arialRecognitor);
        Button thinning = findViewById(R.id.thiningAlgorithm);
        Button hisEq = findViewById(R.id.hisEq);
        Button hisSpec = findViewById(R.id.hisSpec);
        Button imgSmooth = findViewById(R.id.imgSmooth);
        Button asciRec = findViewById(R.id.asciRec);
        Button hisImg = findViewById(R.id.imgHis);
        Button faceDetect = findViewById(R.id.faceDetect);
        Button edgeDetect = findViewById(R.id.edgeDetect);
        Button fourierTransform = findViewById(R.id.fourierTransform);

        //launch activity for histogram gambar
        hisImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Histogram.class);
                startActivity(intent);
            }
        });
        //launch activity for historam specification
        hisSpec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistogramSpecification.class);
                startActivity(intent);
            }
        });

        //launch activity for histogram equalization
        hisEq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistogramEqualization.class);
                startActivity(intent);
            }
        });

        //launch activity for image smoothing
        imgSmooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageSmoothing.class);
                startActivity(intent);
            }
        });

        //launch activity for arial img recognition
        arialImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ArialNumberRecog.class);
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

        //launch activity for asci image recognitor
        asciRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AsciRecognitor.class);
                startActivity(intent);
            }
        });

        //launch activity for face detection
        faceDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FaceDetection.class);
                startActivity(intent);
            }
        });

        //launch activity for edge deection
    }
}