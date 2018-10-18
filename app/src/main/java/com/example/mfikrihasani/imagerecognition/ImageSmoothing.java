package com.example.mfikrihasani.imagerecognition;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

public class ImageSmoothing extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    Uri imageURI;
    ImageView img;
    ImageView img2;
    Bitmap newBitmap;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_smoothing);
        img = (ImageView) findViewById(R.id.imageView);
        img2 = (ImageView) findViewById(R.id.imageView2);
        Button load = (Button) findViewById(R.id.loadImageS);
        final Button smooth = (Button) findViewById(R.id.smoothImage);
        smooth.setEnabled(false);

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage();
                if(hasImage(img)){
                    smooth.setEnabled(true);
                }
            }
        });

        smooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothImage(newBitmap);
            }
        });
    }


    private boolean hasImage(@NonNull ImageView img){
        Drawable drawable = img.getDrawable();
        boolean hasImage = (drawable != null);
        if(hasImage && (drawable instanceof BitmapDrawable)){
            hasImage = ((BitmapDrawable) drawable).getBitmap()!=null;
        }
        return hasImage;
    }

    private void loadImage(){
        Intent gallery =  new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            // Load Image File
            imageURI = data.getData();

            Bitmap viewImage = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURI);
                if (bitmap.getWidth() == bitmap.getHeight()){
                    newBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), 450, 450);
//                    viewImage = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), 600, 600);
                }else if(bitmap.getHeight() > bitmap.getWidth()){
                    newBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), 500, 450);
//                    viewImage = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), 720, 600);
                }else{
                    newBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), 450, 500);
//                    viewImage = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), 600, 720);

                }
                img.setImageBitmap(newBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private Bitmap getResizedBitmap(Bitmap bm, int height, int width, int newHeight, int newWidth){
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // crate matrix for manipulation
        Matrix matrix = new Matrix();
//        // Resize the bitmap
        matrix.postScale(scaleWidth, scaleHeight);
//        // recreate the new bitmap;
        Bitmap newBitmap = Bitmap.createBitmap(bm, 0,0,width,height,matrix, false);
//        bm.recycle();
//        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return newBitmap;
    }

    public void smoothImage(Bitmap bitmap){

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        Bitmap newImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int pixelNow, pixel1,pixel2,pixel3,pixel4,pixel5,pixel6,pixel7,pixel8;
        int redNow, red1, red2, red3,red4,red5,red6,red7,red8;
        int greenNow, green1, green2, green3,green4,green5,green6,green7,green8;
        int blueNow, blue1, blue2, blue3,blue4,blue5,blue6,blue7,blue8;

        //loop pixel bitmap
        for(int y = 1; y < height-1; y++){
            for(int x = 1; x < width-1; x++){
                pixelNow = bitmap.getPixel(x,y);
                redNow = Color.red(pixelNow);
                greenNow = Color.green(pixelNow);
                blueNow = Color.blue(pixelNow);

                //cek sisi kanan
                pixel1 = bitmap.getPixel(x+1,y);
                red1 = Color.red(pixel1);
                green1 = Color.green(pixel1);
                blue1 = Color.blue(pixel1);

                // cek serong kanan bawah
                pixel2 = bitmap.getPixel(x+1, y+1);
                red2 = Color.red(pixel2);
                green2 = Color.green(pixel2);
                blue2 = Color.blue(pixel2);

                // cek serong kanan atas
                pixel3 = bitmap.getPixel(x+1, y-1);
                red3 = Color.red(pixel3);
                green3 = Color.green(pixel3);
                blue3 = Color.blue(pixel3);

                //cek sisi kiri
                pixel4 = bitmap.getPixel(x-1,y);
                red4 = Color.red(pixel4);
                green4 = Color.green(pixel4);
                blue4 = Color.blue(pixel4);

                //cek serong kiri bawah
                pixel5 = bitmap.getPixel(x-1, y+1);
                red5 = Color.red(pixel5);
                green5 = Color.green(pixel5);
                blue5 = Color.blue(pixel5);

                //cek serong kiri atas
                pixel6 = bitmap.getPixel(x-1, y-1);
                red6 = Color.red(pixel6);
                green6 = Color.green(pixel6);
                blue6 = Color.blue(pixel6);

                //cek bawah
                pixel7 = bitmap.getPixel(x, y+1);
                red7 = Color.red(pixel7);
                green7 = Color.green(pixel7);
                blue7 = Color.blue(pixel7);

                //cek atas
                pixel8 = bitmap.getPixel(x, y-1);
                red8 = Color.red(pixel8);
                green8 = Color.green(pixel8);
                blue8 = Color.blue(pixel8);

                int red = (redNow + red1 + red2 + red3 + red4 + red5 + red6 + red7 + red8)/9;
                int green = (greenNow + green1 + green2 + green3 + green4 + green5 + green6 + green7 + green8)/9;
                int blue = (blueNow + blue1 + blue2 + blue3 + blue4 + blue5 + blue6 + blue7 + blue8)/9;
                newImage.setPixel(x,y,Color.rgb(red,green,blue));
            }
        }
//        if (bitmap.getWidth() == bitmap.getHeight()){
//            newImage = getResizedBitmap(newImage, bitmap.getHeight(), bitmap.getWidth(), 500, 500);
//        }else if(bitmap.getHeight() > bitmap.getWidth()){
//            newImage = getResizedBitmap(newImage, bitmap.getHeight(), bitmap.getWidth(), 620, 500);
//        }else{
//            newImage = getResizedBitmap(newImage, bitmap.getHeight(), bitmap.getWidth(), 500, 620);
//        }
        img2.setImageBitmap(newImage);
    }
}
