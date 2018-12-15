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
import android.widget.ImageView;

import com.example.mfikrihasani.imagerecognition.Control.FFTRosettaCode;
import com.example.mfikrihasani.imagerecognition.Control.PublicUsage;

import java.io.IOException;

import static com.example.mfikrihasani.imagerecognition.ThinningActivity.PICK_IMAGE;

public class FourierTransform extends AppCompatActivity {
    Bitmap bitmap, scaledBitmap, processedImg;
    Uri imageURI;
    ImageView loadedImage,transformedImage, inversedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourier_transform);
        loadedImage = findViewById(R.id.fftImage);

    }

    public void openGallery(View view) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        startActivityForResult(gallery, PICK_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PublicUsage publicUsage = new PublicUsage();
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

                //search face

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void fft(View view){
        Log.d("cekfft", "fft: ceeek");
////            FastFourierTransform fastFourierTransform = new FastFourierTransform();
        FFTRosettaCode fftRosettaCode = new FFTRosettaCode();
        int[] img1D = new int[100];
        fftRosettaCode.startFFT(scaledBitmap);
    }

}
