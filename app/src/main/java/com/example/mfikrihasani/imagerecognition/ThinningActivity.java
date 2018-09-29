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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

public class ThinningActivity extends AppCompatActivity {
    final static int PICK_IMAGE = 100;
    Bitmap bitmap;
    Bitmap scaledBitmap;
    Bitmap thinnedBitmap;
    Uri imageURI;
    ImageView ori;
    ImageView thin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thinning);
        Button load = findViewById(R.id.buttonLoad);
        Button zhangThin = findViewById(R.id.zhangThin);
        Button otherThin = findViewById(R.id.otherThin);
        ori = findViewById(R.id.imgOri);
        thin = findViewById(R.id.imgThinned);

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        if (hasImage(ori)){
            zhangThin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    zhangSuenThinning();
                }
            });
        }
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

    //buka gallery
    private void openGallery(){
        Intent gallery =  new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        startActivityForResult(gallery, PICK_IMAGE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE)
        {
            // Load Image File
            imageURI = data.getData();
            bitmap = null;
            scaledBitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageURI);
                if (bitmap.getHeight() == bitmap.getWidth()) {
                    scaledBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), 380, 380);
                } else if (bitmap.getHeight() > bitmap.getWidth()) {
                    scaledBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), 380, 260);
                } else {
                    scaledBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), 260, 380);
                }
                thinnedBitmap = scaledBitmap.copy(Bitmap.Config.ALPHA_8, true);
                toBW();
                ori.setImageBitmap(thinnedBitmap);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //change bitmap to bw
    private void toBW(){
        int height = thinnedBitmap.getHeight();
        int width = thinnedBitmap.getWidth();
        int pixel, red, green, blue, bw, bwColour;
        for(int y=0; y<height; y++){
            for (int x=0; x<width; x++){
                pixel = thinnedBitmap.getPixel(x,y);
                red = Color.red(pixel);
                green = Color.green(pixel);
                blue = Color.blue(pixel);
                bw = (red+blue+green)/3;
                if(bw >= 128){
                    bw = 255;
                }else{
                    bw = 0;
                }
                thinnedBitmap.setPixel(x,y,Color.rgb(bw,bw,bw));
            }
        }
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

    //zhang suen thinning
    private void zhangSuenThinning(){
        for (int x = 1; x < thinnedBitmap.getWidth(); x++){
            for (int y = 1; y < thinnedBitmap.getHeight(); y++){
                Boolean cond0, cond1, cond2, cond3, cond4;
                cond0 = cond1 = cond2 = cond3 = cond4 = false;
                int cond3Val, cond4Val;
                cond3Val = cond4Val = 0;
                int BP = 0;
                int AP = 0;
                if (Color.red(thinnedBitmap.getPixel(x,y)) == 0){
                    cond0 = true;
                    //p2
                    if (Color.red(thinnedBitmap.getPixel(x,y-1)) == 0){
                        BP += 1;
                    }else{
                        cond3Val += 1;
                    }
                    //p3
                    if (Color.red(thinnedBitmap.getPixel(x+1,y-1)) == 0){
                        BP += 1;
                    }
                    //p4
                    if(Color.red(thinnedBitmap.getPixel(x+1,y)) == 0){
                        BP += 1;
                    }else{
                        cond3Val +=1;
                        cond4Val +=1;
                    }
                    //p5
                    if (Color.red(thinnedBitmap.getPixel(x+1,y+1)) == 0){
                        BP +=1;
                    }
                    //p6
                    if (Color.red(thinnedBitmap.getPixel(x,y+1)) == 0){
                        BP += 1;
                    }else{
                        cond3Val += 1;
                        cond4Val += 1;
                    }
                    //p7
                    if (Color.red(thinnedBitmap.getPixel(x-1,y+1)) == 0){
                        BP += 1;
                    }
                    //p8
                    if (Color.red(thinnedBitmap.getPixel(x-1,y)) == 0){
                        BP += 1;
                    }else {
                        cond4Val += 1;
                    }
                    //p9
                    if (Color.red(thinnedBitmap.getPixel(x-1,y-1)) == 0){
                        BP += 1;
                    }
                    int totalMovement = Color.red(thinnedBitmap.getPixel(x,y-1)) + Color.red(thinnedBitmap.getPixel(x+1,y-1)) + Color.red(thinnedBitmap.getPixel(x+1,y)) + Color.red(thinnedBitmap.getPixel(x+1,y+1)) + Color.red(thinnedBitmap.getPixel(x,y+1)) + Color.red(thinnedBitmap.getPixel(x-1,y+1)) + Color.red(thinnedBitmap.getPixel(x-1,y)) + Color.red(thinnedBitmap.getPixel(x-1,y-1));
                    AP = totalMovement/(7 * 255);
                    if (AP == 1){
                        cond2 = true;
                    }
                    if (2 <= BP && BP <= 6){
                        cond1 = true;
                    }
                    if(cond3Val > 0){
                        cond3 = true;
                    }
                    if (cond4Val > 0){
                        cond4 = true;
                    }
                }
                if (cond0 && cond1 && cond2 && cond3 && cond4){
                    thinnedBitmap.setPixel(x,y,Color.rgb(255,255,255));
                }
            }
        }
    }

}
