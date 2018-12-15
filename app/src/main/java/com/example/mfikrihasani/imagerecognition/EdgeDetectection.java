package com.example.mfikrihasani.imagerecognition;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mfikrihasani.imagerecognition.Control.FaceDetectionSupport;
import com.example.mfikrihasani.imagerecognition.Control.PublicUsage;

import java.io.IOException;
import java.text.BreakIterator;

import static com.example.mfikrihasani.imagerecognition.ThinningActivity.PICK_IMAGE;

public class EdgeDetectection extends AppCompatActivity {
    Button loadImage, process;
    Uri imageURI;
    Bitmap bitmap, scaledBitmap, processedImg, clonedBitmap;
    ImageView loadedImage, detectedFace;
    TextView textView;
    int[] rgbMax = new int[3];
    int xMax, xMin, yMax, yMin;
    PublicUsage publicUsage = new PublicUsage();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edge_detectection);
        loadImage = findViewById(R.id.loadImageE);
        process = findViewById(R.id.processSobel);
        loadedImage = findViewById(R.id.loadedImageE);
        detectedFace = findViewById(R.id.sobeledFace);
        textView = findViewById(R.id.imageMessage);
    }

    public void openGallery(View view) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        startActivityForResult(gallery, PICK_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            // Load Image File
            imageURI = data.getData();
            bitmap = null;
            scaledBitmap = null;
            processedImg = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURI);
                if (bitmap.getWidth() > 2000 || bitmap.getHeight() > 2000) {
                    scaledBitmap = publicUsage.getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), bitmap.getHeight() / 8, bitmap.getWidth() / 8);
                } else if (bitmap.getWidth() > 1000 || bitmap.getHeight() > 1000) {
                    scaledBitmap = publicUsage.getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), bitmap.getHeight() / 6, bitmap.getWidth() / 6);
                } else if (bitmap.getHeight() > 800 || bitmap.getWidth() > 800) {
                    scaledBitmap = publicUsage.getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), bitmap.getHeight() / 3, bitmap.getWidth() / 3);
                } else if (bitmap.getHeight() >= 500|| bitmap.getWidth() >= 500){
                    scaledBitmap = publicUsage.getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), bitmap.getHeight() / 2, bitmap.getWidth() / 2);

                }

                loadedImage.setImageBitmap(scaledBitmap);

                Log.d("Face Feature", ": xMin = " + xMin + ", yMin = " + yMin + ", xMax = " + xMax + ", yMax = " + yMax);
                //search face

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //process
    public void startSobel(View view) {
        boolean [][] skinPixels = new boolean[scaledBitmap.getWidth()][scaledBitmap.getHeight()];
        if (publicUsage.hasImage(loadedImage)) {
            rgbMax[0] = rgbMax[1] = rgbMax[2] = 0;
            processedImg = null;
            Bitmap cropped = null;
            FaceDetectionSupport.getSkinPixels(scaledBitmap,skinPixels);
            processedImg = FaceDetectionSupport.detectFace(scaledBitmap,skinPixels);
//            detectedFace.setImageBitmap(processedImg);

            cropped = FaceDetectionSupport.getCroppedBitmap();
            if(cropped!= null){
                Bitmap[] hasil = new Bitmap[2];
                hasil = FaceDetectionSupport.sobelOperator(0,cropped.getWidth(),0,cropped.getHeight(),cropped);
                cropped = hasil[0];
//                Bitmap detected = hasil[1];
                Bitmap rgb = hasil[1];
                Bitmap detected = FaceDetectionSupport.detectObject(cropped,rgb);

                detectedFace.setImageBitmap(detected);
                textView.setText("ukuran scaled image: "+scaledBitmap.getWidth()+","+scaledBitmap.getHeight());
            }else{
                textView.setText("No image processed");
            }
        } else {
            textView.setText("No Image to Process");
        }
    }
}
