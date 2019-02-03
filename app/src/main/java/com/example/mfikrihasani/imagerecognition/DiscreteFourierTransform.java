package com.example.mfikrihasani.imagerecognition;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

import static com.example.mfikrihasani.imagerecognition.ThinningActivity.PICK_IMAGE;
import static java.lang.Math.PI;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.hypot;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class DiscreteFourierTransform extends AppCompatActivity {
    ImageView img, imgMag, imgP, inversedDFT;
    Uri imageURI;
    Bitmap bitmap, scaledBitmap, processedImg;
    int width;
    int height;
    int[][] g; // original image
    double[][] GReal; // Fourier real
    double[][] GImaginer; // Fourier imaginary
    double[][] GMagnitude; // Fourier Magnitude
    double[][] GAngle; // Fourier Phase  
    double[][] gReal; // Inverse real 
    double[][] gImaginer; // Inverse imaginary
    double[][] gMagnitude; // Inverse Magnitude 
    double[][] gAngle; // Inverse Phase
    Bitmap spatial;
    Bitmap freq;
    Bitmap freqR; //Real
    Bitmap freqI; //Imaginary
    Bitmap freqA; //angle
    Bitmap hat; //magnitude
    Bitmap hatR; //Real
    Bitmap hatI; //Imaginary
    Bitmap hatA; //angle
    Button load,dft,idft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discrete_fourier_transform);
        img = findViewById(R.id.loadedImgForDFT);
        imgMag = findViewById(R.id.imgMagnitude);
        imgP = findViewById(R.id.imgPhase);
        inversedDFT = findViewById(R.id.inversedDFTImg);
        load = findViewById(R.id.loadImgDFT);
        if (img != null){
            load.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openGallery();
                }
            });
        }
        dft = findViewById(R.id.doDFT);
        dft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDFT();
            }
        });
        idft = findViewById(R.id.doIDFT);
        idft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doIDFT();
            }
        });

    }

    public void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        startActivityForResult(gallery, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            // Load Image File
            imageURI = data.getData();
            bitmap = null;
            scaledBitmap = null;
            processedImg = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURI);
                if (bitmap.getWidth() > 2000 || bitmap.getHeight() > 2000) {
                    scaledBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), bitmap.getHeight() / 8, bitmap.getWidth() / 8);
                } else if (bitmap.getWidth() > 1000 || bitmap.getHeight() > 1000) {
                    scaledBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), bitmap.getHeight() / 6, bitmap.getWidth() / 6);
                } else if (bitmap.getHeight() > 800 || bitmap.getWidth() > 800) {
                    scaledBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), bitmap.getHeight() / 3, bitmap.getWidth() / 3);
                } else if (bitmap.getHeight() >= 500|| bitmap.getWidth() >= 500){
                    scaledBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), bitmap.getHeight() / 2, bitmap.getWidth() / 2);

                }
                Bitmap grayBit = grayscale(scaledBitmap);
                img.setImageBitmap(grayBit);

                //search face

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{

        }
    }

    private Bitmap grayscale(Bitmap bitmap){
        Bitmap grayBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                int color = bitmap.getPixel(j,i);
                int redVal = Color.red(color);
                int greenVal = Color.green(color);
                int blueVal = Color.blue(color);
                int bwVal = (redVal+greenVal+blueVal)/3;
                int bwColor = 0xFF000000 | (bwVal<<16 | bwVal<<8 | bwVal);

                grayBitmap.setPixel(j,i,bwColor);

            }
        }

        return grayBitmap;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int height, int width, int newHeight, int newWidth) {
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // crate matrix for manipulation
        Matrix matrix = new Matrix();
//        // Resize the bitmap
        matrix.postScale(scaleWidth, scaleHeight);
//        // recreate the new bitmap;
        Bitmap newBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();

        return newBitmap;
    }

    public void doDFT(){
        spatial = ((BitmapDrawable) img.getDrawable()).getBitmap();
        freq = spatial.copy(Bitmap.Config.ARGB_8888, true); //magnitude
        freqR = spatial.copy(Bitmap.Config.ARGB_8888, true); //Real
        freqI = spatial.copy(Bitmap.Config.ARGB_8888, true); //Imaginary
        freqA = spatial.copy(Bitmap.Config.ARGB_8888, true); //angle
        hat = spatial.copy(Bitmap.Config.ARGB_8888, true); //magnitude
        hatR = spatial.copy(Bitmap.Config.ARGB_8888, true); //Real
        hatI = spatial.copy(Bitmap.Config.ARGB_8888, true); //Imaginary
        hatA = spatial.copy(Bitmap.Config.ARGB_8888, true); //angle
        width = spatial.getWidth();
        height = spatial.getHeight();
        g = new int[height][width]; 
        GReal = new double[height][width];
        GImaginer = new double[height][width];
        GMagnitude = new double[height][width];
        GAngle = new double[height][width];
        gReal = new double[height][width];
        gImaginer = new double[height][width];
        gMagnitude = new double[height][width];        
        gAngle = new double[height][width]; 

        int midX = width/2;
        int midY = height/2;
        double r = 150;
        for(int p=0; p<height; p++){
            for(int q=0; q<width; q++){
                int color = spatial.getPixel(q,p);
                int sVal = Color.red(color);
                g[p][q] = sVal;
            }
        }

        /** DFT */
        double cekIsInsideCirle = 0;
        for(int p=0; p<height; p++){
            for(int q=0; q<width; q++){

                for(int x=0; x<width; x++){
                    GReal[p][q] += g[p][x]*cos(2*PI*x*q/width);
                    GImaginer[p][q] += g[p][x]*sin(2*PI*x*q/width);
                }
                GReal[p][q] /= width; /** Scaling, only in Fourier not in inverse*/
                GImaginer[p][q] /= width;
                cekIsInsideCirle = sqrt(Math.pow(midX - q,2) + Math.pow(midY - p,2));
                if (cekIsInsideCirle > r){
                    GReal[p][q] = 0;
                    GImaginer[p][q] = 0;
                }

                GMagnitude[p][q] = hypot(GReal[p][q],GImaginer[p][q])*width;
                GAngle[p][q] = atan(GImaginer[p][q]/GReal[p][q]);

                int GVal = (int)GMagnitude[p][q];
                int GColor = 0xFF000000 | (GVal<<16 | GVal<<8 | GVal);
                freq.setPixel(q,p,GColor);


                int GAVal = (int)((GAngle[p][q] + PI/2)*255/PI); /** scaling for display */
                int GAColor = 0xFF000000 | (GAVal<<16 | GAVal<<8 | GAVal);
                freqA.setPixel(q,p,GAColor);
            }
        }
        imgMag.setImageBitmap(freq);
        imgP.setImageBitmap(freqA);
    }

    public void doIDFT(){
        /** Inverse DFT */
        for(int p=0; p<height; p++){
            for(int q=0; q<width; q++){

                for(int x=0; x<width; x++){
                    gReal[p][q] += GReal[p][x]*cos(2*PI*x*q/width) + GImaginer[p][x]*sin(2*PI*x*q/width);
                    gImaginer[p][q] += GImaginer[p][x]*cos(2*PI*x*q/width) - GReal[p][x]*sin(2*PI*x*q/width);
                }

                gMagnitude[p][q] = hypot(gReal[p][q],gImaginer[p][q]);
                gAngle[p][q] = atan(gImaginer[p][q]/gReal[p][q]);

                int gVal = (int)gMagnitude[p][q];
                int gColor = 0xFF000000 | (gVal<<16 | gVal<<8 | gVal);
                hat.setPixel(q,p,gColor);

            }
        }
//        ImageView imgInverseMagnitude = findViewById(R.id.imgInverseMagnitude);
        inversedDFT.setImageBitmap(hat);

    }

}
