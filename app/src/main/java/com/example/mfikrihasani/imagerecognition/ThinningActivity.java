package com.example.mfikrihasani.imagerecognition;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
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

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThinningActivity extends AppCompatActivity {
    final static int PICK_IMAGE = 100;
    private final static int[][] tetangga = {{0,-1}, {1,-1}, {1,0}, {1,1}, {0,1}, {-1,1}, {-1,0}, {-1,-1}, {0,-1}};
    Bitmap bitmap;
    Bitmap scaledBitmap;
    Bitmap thinnedBitmap;
    Uri imageURI;
    ImageView ori;
    ImageView thin;
    static final int white = 0xFFFFFFFF;
    static final int black = 0xFF000000;
    final static int[][][] nbrGroups = {{{0, 2, 4}, {2, 4, 6}}, {{0, 2, 6},
            {0, 4, 6}}};
    int obj = 0;

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
                    Bitmap thinBm;
                    thinBm = thinImage(thinnedBitmap);
                    thin.setImageBitmap(thinBm);
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

    //on acitivity result
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
                    scaledBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), 260, 260);
                } else if (bitmap.getHeight() > bitmap.getWidth()) {
                    scaledBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), 260, 140);
                } else {
                    scaledBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), 140, 260);
                }
//                thinnedBitmap = scaledBitmap.copy(Bitmap.Config.ARGB_8888, true);
                thinnedBitmap = createBlackAndWhite(scaledBitmap);
                ori.setImageBitmap(scaledBitmap);

            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //change bitmap to bw
    public static Bitmap createBlackAndWhite(Bitmap src) {
        int width = src.getWidth();
        int height = src.getHeight();

        Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        final float factor = 255f;
        final float redBri = 0.2126f;
        final float greenBri = 0.2126f;
        final float blueBri = 0.0722f;

        int length = width * height;
        int[] inpixels = new int[length];
        int[] oupixels = new int[length];

        src.getPixels(inpixels, 0, width, 0, 0, width, height);

        int point = 0;
        for(int pix: inpixels){
            int R = (pix >> 16) & 0xFF;
            int G = (pix >> 8) & 0xFF;
            int B = pix & 0xFF;

            float lum = (redBri * R / factor) + (greenBri * G / factor) + (blueBri * B / factor);

            if (lum > 0.4) {
                oupixels[point] = white;
            }else{
                oupixels[point] = black;
            }
            point++;
        }
        bmOut.setPixels(oupixels, 0, width, 0, 0, width, height);
        return bmOut;
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

    //translate
    public int[] translate(int x, int y, int pos){
        if(pos > 7) pos = 0;
        switch (pos){
            case 1 : return new int[]{x+1, y-1};
            case 0 : return new int[]{x+1, y};
            case 7 : return new int[]{x+1, y+1};
            case 6 : return new int[]{x, y+1};
            case 5 : return new int[]{x-1, y+1};
            case 4 : return new int[]{x-1, y};
            case 3 : return new int[]{x-1, y-1};
            case 2 : return new int[]{x, y-1};
            default: return null;
        }
    }
    //cek transisi putih ke hitam
//    private int[] cekTransisi(Integer x, Integer y){
//        int[] APBP= new int[2];
//        int AP,BP;
//        AP = BP = 0;
//        int loopx,loopy;
//        int loopNextX,loopNextY;
//
//        for (int i = 0; i<tetangga.length-1; i++){
//            int j=i+1;
//            loopx = x+tetangga[i][0];
//            loopy = y+tetangga[i][1];
//            loopNextX = x+tetangga[j][0];
//            loopNextY = y+tetangga[j][1];
//            Log.d("cekTransisi", "loopx: "+loopx+", loopy: "+loopy);
//            if (Color.red(thinnedBitmap.getPixel(loopx,loopy)) == 255 && Color.red(thinnedBitmap.getPixel(loopNextX,loopNextY))== 0){
//                AP += 1;
//            }
//            if(Color.red(thinnedBitmap.getPixel(loopx,loopy)) == 0) {
//                BP += 1;
//            }
//
//        }
//
//        APBP[0] = AP;
//        APBP[1] = BP;
//        return  APBP;
//    }

    //thinning algorithm
    public Bitmap thinImage(Bitmap img) {
        boolean firstStep = false;
        boolean hasChanged;
        List<Point> toWhite = new ArrayList<Point>();
        Bitmap res = img;
        do {
            hasChanged = false;
            firstStep = !firstStep;

            for (int y = 1; y < res.getHeight() - 1; y++) {
                for (int x = 1; x < res.getWidth() - 1; x++) {
                    if (res.getPixel(x, y) != black)
                        continue;
                    int nn = numNeighbors(x, y, res);
                    if (nn < 2 || nn > 6)
                        continue;
                    if (numTransitions(x, y, res) != 1)
                        continue;
                    if (!atLeastOneIsWhite(x, y, firstStep ? 0 : 1, res))
                        continue;
                    toWhite.add(new Point(x, y));
                    hasChanged = true;
                }
            }

            for (Point p : toWhite)
                res.setPixel(p.x, p.y, Color.WHITE);
            toWhite.clear();

        } while (firstStep || hasChanged);
        return res;
    }

    public int numNeighbors(int x, int y, Bitmap img) {
        int count = 0;
        for (int i = 0; i < 7; i++) {
            int[] nbrs = translate(x, y, i);
            if (img.getPixel(nbrs[0], nbrs[1]) == black)
                count++;
        }
        return count;
    }

    public int numTransitions(int x, int y, Bitmap img) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            int[] nbrs = translate(x, y, i);
            if (img.getPixel(nbrs[0], nbrs[1]) == white) {
                nbrs = translate(x, y, i+1);
                if (img.getPixel(nbrs[0], nbrs[1]) == black)
                    count++;
            }
            if (count > 1) break;
        }
        return count;
    }

    public boolean atLeastOneIsWhite(int x, int y, int step, Bitmap img) {
        int count = 0;
        int[][] group = nbrGroups[step];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < group[i].length; j++) {
                int[] nbr = translate(x, y, group[i][j]);
                if (img.getPixel(nbr[0], nbr[1]) == white) {
                    count++;
                    break;
                }
            }
        return count > 1;
    }

    //zhang suen thinning
//    private void zhangSuenThinning(){
//        int[] transisi = new int[2];
//        for (int y = 1; y < thinnedBitmap.getHeight()-1; y++){
//            for (int x = 1; x < thinnedBitmap.getWidth()-1; x++){
//                int BP = 0;
//                int AP = 0;
//                int cond3Val, cond4Val, s2Cond3Val, s2Cond4Val;
//                cond3Val = cond4Val = s2Cond3Val = s2Cond4Val = 0;
//                if (Color.red(thinnedBitmap.getPixel(x,y)) == 0){
//                    //cek kondisi 3 untuk p2, p4, p6 step 1
//                    cond3Val = Color.red(thinnedBitmap.getPixel(x,y-1)) + Color.red(thinnedBitmap.getPixel(x+1,y)) + Color.red(thinnedBitmap.getPixel(x,y+1));
//                    //cek kondisi 4 untuk p4, p6, p8 step 1
//                    cond4Val = Color.red(thinnedBitmap.getPixel(x+1,y)) + Color.red(thinnedBitmap.getPixel(x,y+1)) + Color.red(thinnedBitmap.getPixel(x-1,y));
//                    //cek kondisi 3 step 2 p2,p4,p8
//                    s2Cond3Val = Color.red(thinnedBitmap.getPixel(x,y-1)) + Color.red(thinnedBitmap.getPixel(x+1,y)) + Color.red(thinnedBitmap.getPixel(x-1,y));
//                    //cek kondisi 4 step 2 p2,p6,p8
//                    s2Cond4Val = Color.red(thinnedBitmap.getPixel(x,y-1)) + Color.red(thinnedBitmap.getPixel(x,y+1)) + Color.red(thinnedBitmap.getPixel(x-1,y));
//                    transisi = cekTransisi(x,y);
//                    AP = transisi[0];
//                    BP = transisi[1];
//                    Log.d("AP BP: ", "zhangSuenThinning: AP = "+AP+", BP = "+BP);
//                    if ((AP==1) && (2<=BP && BP<=6) && cond3Val >= 255 && cond4Val >=255 && s2Cond3Val >= 255 && s2Cond4Val >= 255){
//                        thinnedBitmap.setPixel(x,y,Color.rgb(255,255,255));
//                    }
//                }
//
//            }
//        }
}
