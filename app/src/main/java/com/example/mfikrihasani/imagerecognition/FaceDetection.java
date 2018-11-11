package com.example.mfikrihasani.imagerecognition;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
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

import java.io.IOException;

import static com.example.mfikrihasani.imagerecognition.ThinningActivity.PICK_IMAGE;

public class FaceDetection extends AppCompatActivity {
    Button loadImage, process;
    Uri imageURI;
    Bitmap bitmap, scaledBitmap, processedImg,clonedBitmap;
    ImageView loadedImage, detectedFace;
    TextView textView;
    int[] rgbMax = new int[3];
    int[] location = new int[4];
//    int[] faceMin= {177, 113, 90};
//    double[] faceMax= {230, 183, 175,255,179,156};
    int xMax,xMin,yMax,yMin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detection);
        loadImage = findViewById(R.id.loadImageF);
        process = findViewById(R.id.process);
        loadedImage = findViewById(R.id.loadedImageF);
        detectedFace = findViewById(R.id.detectedFace);
        textView = findViewById(R.id.imageLocation);
    }

    //cek apakah image ada atau tidak
    private boolean hasImage(@NonNull ImageView img){
        Drawable drawable = img.getDrawable();
        boolean hasImage = (drawable != null);
        if(hasImage && (drawable instanceof BitmapDrawable)){
            hasImage = ((BitmapDrawable) drawable).getBitmap()!=null;
        }
        return hasImage;
    }

    public void openGallery(View view){
        Intent gallery =  new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        startActivityForResult(gallery, PICK_IMAGE);
    }

    //resize bitmap biar ga kegedean
    private Bitmap getResizedBitmap(Bitmap bm, int height, int width, int newHeight, int newWidth){
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // crate matrix for manipulation
        Matrix matrix = new Matrix();
//        // Resize the bitmap
        matrix.postScale(scaleWidth, scaleHeight);
//        // recreate the new bitmap;
        Bitmap newBitmap = Bitmap.createBitmap(bm, 0,0,width,height,matrix, false);
        bm.recycle();

        return newBitmap;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE)
        {
            // Load Image File
            imageURI = data.getData();
            bitmap = null;
            scaledBitmap = null;
            processedImg = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageURI);
                if (bitmap.getWidth() > 2000 || bitmap.getHeight() > 2000) {
                    scaledBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), bitmap.getHeight()/16, bitmap.getWidth()/16);
                }else
                    if (bitmap.getWidth() > 1000 || bitmap.getHeight() > 1000){
                    scaledBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), bitmap.getHeight()/8, bitmap.getWidth()/8);
                }else if(bitmap.getHeight() > 800 || bitmap.getWidth() > 800){
                    scaledBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), bitmap.getHeight()/4, bitmap.getWidth()/4);
                }else{
                    scaledBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), bitmap.getHeight()/2, bitmap.getWidth()/2);

                }

//                if (bitmap.getHeight() == bitmap.getWidth()) {
//                    scaledBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), 260, 260);
//                } else if (bitmap.getHeight() > bitmap.getWidth()) {
//                    scaledBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), 260, 140);
//                } else {
//                    scaledBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), 140, 260);
//                }

                loadedImage.setImageBitmap(scaledBitmap);

                Log.d("Face Feature", ": xMin = "+xMin+", yMin = "+yMin+", xMax = "+xMax+", yMax = "+yMax);
                //search face

            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //process
    public void startDetect(View view){
        if (hasImage(loadedImage)){
            rgbMax[0] = rgbMax[1] = rgbMax[2] = 0;
            processedImg = null;
            processedImg = detectSkin(scaledBitmap);
            detectedFace.setImageBitmap(processedImg);
            String location = "Face Feature: xMin = "+xMin+", yMin = "+yMin+", xMax = "+xMax+", yMax = "+yMax+"\n RGB xMax: "+rgbMax[0]+" "+rgbMax[1]+" "+rgbMax[2];
            textView.setText(location);
        }else{
            textView.setText("No Image to Process");
        }
    }
    //search face
    private Bitmap detectSkin(Bitmap bitmap){
        Bitmap cloned = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        Bitmap detected = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        int pixel, red, green, blue;
        xMax = yMax = 0;
        xMin = bitmap.getWidth();
        yMin = bitmap.getHeight();
        for (int y = 0; y < bitmap.getHeight(); y++){
            for (int x = 0; x < bitmap.getWidth(); x++){
                pixel = bitmap.getPixel(x,y);
                red = Color.red(pixel);
                green = Color.green(pixel);
                blue = Color.blue(pixel);
                float[] hsv = new float[3];
                boolean eqA = (red > 50) && (green > 40) && (blue > 20) && (Math.max(Math.max(red,green),blue) - Math.min(Math.min(red,green),blue)) > 10 && (red-green)>=10 && (red > green) && (green > blue);
                boolean eqB = (red > 200 ) && (green > 210) && (blue > 170) && (Math.abs(red-green) <= 15) && (red > blue) && (green > blue);
                boolean ruleA = eqA || eqB;
//                double Y = 0.299 * red + 0.287*green + 0.11 * blue;
//                double cr = red-Y;
//                double cb = blue-Y;
//                int y = 16 + (((red << 6) +(red << 1) + (green << 7) + green + (blue << 4) + (blue << 3) + blue) >> 8);
                int cb = 128+((-((red << 5)+(red << 2)+(red << 1)) - ((green << 6 )+ (green << 3) + (green << 1)) + (blue << 7) - (blue << 4)) >> 8);
                int cr = 128+(((red << 7) - (red << 4) - ((green << 6) + (green << 5) - (green << 1)) - ((blue << 4) + (blue << 1))) >> 8);
                boolean ruleB = (cb >= 60 && cr <=130) && (cb>=130 && cr<=165);
                Color.RGBToHSV(red,green,blue,hsv);
                boolean ruleC = (hsv[0] >= 0 && hsv[0]<=50) && (0.1 <= hsv[1] && hsv[1]<=0.9);
                Log.d("rule", ": "+ruleA+" "+ruleB+" "+ruleC);
                if (ruleA && ruleC){
                    xMax = (x >= xMax) ? x : xMax;
                    xMin = (x <= xMin) ? x : xMin;
                    yMin = (y <= yMin) ? y : yMin;
//                    yMax = (y >= yMax) ? y : yMax;
//                    cloned.setPixel(x,y,Color.rgb(255,255,255));
                }else{
                    cloned.setPixel(x,y,Color.rgb(0,0,0));
                }
            }
        }
        yMax = yMin + (xMax - xMin);
        Bitmap hasil = detectFace(xMin, xMax, yMin, yMax, detected);
        return hasil;

//        clonedBitmap = cloned;

    }
    private Bitmap detectFace(int xMin, int xMax, int yMin, int yMax, Bitmap bitmap){
        //boundary atas & bawah
        for (int i = xMin; i < xMax; i++){
            bitmap.setPixel(i,yMin,Color.rgb(0,255,0));
            bitmap.setPixel(i,yMax,Color.rgb(0,255,0));
        }
        //boundary kiri && kanan
        for (int i = yMin; i < yMax; i++){
            bitmap.setPixel(xMin,i,Color.rgb(0,255,0));
            bitmap.setPixel(xMax,i,Color.rgb(0,255,0));
        }
        return bitmap;
    }
    private Bitmap itemin(Bitmap bitmap){
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int pixel,red,green,blue;
        boolean full;
        int count;
        int val = 0;
        int subVal = 0;
        int i,j = 0;
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                pixel = bitmap.getPixel(x,y);
                count = 0;
                val = Color.red(pixel) + Color.green(pixel)+ Color.blue(pixel);
                if((val/3) == 255){
                    floodfill(clonedBitmap, x, y, count);
                }
            }
        }
        return bitmap;
    }

    private void floodfill(Bitmap bitmap, int x,int y, int count){
        int pixel = bitmap.getPixel(x,y);
        int val = Color.red(pixel) + Color.green(pixel)+ Color.blue(pixel);
        if((val/3) == 255){
            count = count+1;
            clonedBitmap.setPixel(x,y,Color.rgb(0,0,0));
            floodfill(clonedBitmap, x+1,y+1,count);
        }
    }
}
