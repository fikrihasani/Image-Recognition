package com.example.mfikrihasani.imagerecognition.Control;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.incrementExact;
import static java.lang.Math.log;

public class FaceDetectionSupport {
    public static int totalSkinPixel;
    public static int xMinSkinPixel;
    public static int xMaxSkinPixel;
    public static int yMinSkinPixel;
    public static int yMaxSkinPixel;
    public static ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();


    public static Bitmap detectFace(Bitmap bitmap, boolean[][] skinPixels){
        boolean[][] isVisit = new boolean[skinPixels.length][skinPixels[0].length];
        Bitmap bitmapCopy = bitmap.copy(bitmap.getConfig(),true);
        Bitmap bitmapCek = bitmap.copy(Bitmap.Config.ARGB_8888,true);
//        Bitmap[] bitmapArr = new Bitmap[2];
//        bitmapArr[0] = bitmap;

        for(int i = 0; i < skinPixels.length; i++){
            for(int j = 0; j < skinPixels[0].length; j++){
                isVisit[i][j] = false;
            }
        }
        Bitmap cek = null;
        for(int i = 1; i < skinPixels.length-1; i++){
            for(int j = 1; j < skinPixels[0].length-1; j++){
                if(!isVisit[i][j] && skinPixels[i][j]){
                    xMinSkinPixel = i;
                    xMaxSkinPixel = i;
                    yMaxSkinPixel = j;
                    yMinSkinPixel = j;
                    totalSkinPixel = 0;
                    floodFill(skinPixels, isVisit, i, j);
//                    Log.e("total", totalSkinPixel +"");
//                    Log.e("xmin", xMinSkinPixel +"");
//                    Log.e("xmax", xMaxSkinPixel +"");
//                    Log.e("ymin", yMinSkinPixel +"");
//                    Log.e("ymax", yMaxSkinPixel +"");
                    if(totalSkinPixel > 100) {
                        int[] boundary = getBoundary(skinPixels, xMinSkinPixel, xMaxSkinPixel, yMinSkinPixel, yMaxSkinPixel);
                        int xminb = boundary[0];
                        int xmaxb = boundary[1];
                        int yminb = boundary[2];
                        int ymaxb = boundary[3];
                        int totalSkin = boundary[4];
                        int newWidth = xmaxb - xminb;
                        int newHeight = ymaxb - yminb;
                        float goldenRatio = (float) (((1 + Math.sqrt(5)) / 2) - 0.65);
                        float goldenRatio2 = (float) (((1 + Math.sqrt(5)) / 2) + 0.65);
                        float rasio = (float) newHeight / (float) newWidth;
                        int skinPercentage = (totalSkin * 100) / (newWidth * newHeight);
                        if(skinPercentage > 55 && rasio <= goldenRatio2 && rasio >= goldenRatio){
                            if (ymaxb-yminb > 1.36* (xmaxb - xminb)){
                                double panjang = (double) (xmaxb-xminb);
                                ymaxb = yminb + (int) (1.36 * panjang);
                            }
                            createBoundaryBox(bitmapCopy, xminb, xmaxb, yminb, ymaxb);
//                            Bitmap bitCek = Bitmap.createBitmap(xMax-xMin, ymax-yMin, Bitmap.Config.ARGB_8888);
//                            Log.d("cek bitcek - ", "Lebar: "+bitCek.getWidth()+" Tinggi: "+bitCek.getHeight());

                            Bitmap bitCek = cropBitmap(xminb, yminb,xmaxb-xminb, ymaxb-yminb,bitmap);
                            bitmaps.add(bitCek);
                            Log.d("hasil: ", "");
//                            cek = bitCek;
                        }
                    }
                }
            }
        }
//        bitmaps.add(bitmap);
//        bitmapArr[1] = bitmapCopy;
        return bitmapCopy;
    }

    public static Bitmap getCroppedBitmap(){
        Bitmap bitmap;
        if (bitmaps.size() > 0){
            bitmap = bitmaps.get(bitmaps.size()-1);
        }else {
            Log.d(TAG, "getCroppedBitmap: bitmap null");
            bitmap = null;
        }
        return bitmap;
    }
    private static Bitmap cropBitmap(int xMin, int yMin, int width, int height, Bitmap bitmapSource){
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Log.d("Panjang dan lebar", " panjang: "+width+" lebar: "+height);
        int red,green,blue,pixel;
        for(int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                pixel = bitmapSource.getPixel(xMin + x,yMin+y);
                red = Color.red(pixel);
                green = Color.green(pixel);
                blue = Color.blue(pixel);
                result.setPixel(x,y,Color.rgb(red,green,blue));
            }
        }
        return result;
    }

    private static void floodFill(boolean[][] skinPixels, boolean[][] isVisit, int x, int y) {
        if (x - 1 >= 0 && x + 1 < skinPixels.length && y - 1 >= 0 && y + 1 < skinPixels[0].length){
            if(skinPixels[x][y] && !isVisit[x][y]){
                isVisit[x][y] = true;
                totalSkinPixel++;
                if(x < xMinSkinPixel){
                    xMinSkinPixel = x;
                }
                if(x > xMaxSkinPixel){
                    xMaxSkinPixel = x;
                }
                if(y < yMinSkinPixel){
                    yMinSkinPixel = y;
                }
                if(y > yMaxSkinPixel){
                    yMaxSkinPixel = y;
                }
                //floodFill(skinPixels,isVisit, x - 1, y - 1);
                floodFill(skinPixels,isVisit, x - 1, y);
                //floodFill(skinPixels,isVisit, x - 1, y + 1);
                floodFill(skinPixels,isVisit, x, y - 1);
                floodFill(skinPixels,isVisit, x, y + 1);
                //floodFill(skinPixels,isVisit, x + 1, y - 1);
                floodFill(skinPixels,isVisit, x + 1, y);
                //floodFill(skinPixels,isVisit, x + 1, y + 1);
            }
        }
    }

    public static Bitmap getSkinPixels(Bitmap bitmap, boolean[][] skinPixels){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[][] arrBitmap = getPixelsArray(bitmap);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = arrBitmap[i][j];
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
//                float[] hsv = new float[3];
//                Color.colorToHSV(pixel, hsv);
//                float h = hsv[0];
//                float s = hsv[1];
//                float v = hsv[2];
                float[] hsv = new float[3];
                boolean eqA = (red > 50) && (green > 40) && (blue > 20) && (Math.max(Math.max(red, green), blue) - Math.min(Math.min(red, green), blue)) > 10 && (red - green) >= 10 && (red > green) && (green > blue);
                boolean eqB = (red > 200) && (green > 210) && (blue > 170) && (Math.abs(red - green) <= 15) && (red > blue) && (green > blue);
                boolean ruleA = eqA || eqB;
                float y = (float) ((0.257*red) + (0.504*green) + (0.098*blue) + 16);
                float cb = (float) (-(0.148*red) - (0.291*green)+ (0.439*blue) + 128);
                float cr = (float) ((0.439*red) - (0.368*green) - (0.071*blue) + 128);
                boolean ruleB = cb > 105 && cb < 135 && cr > 140 && cr < 165;
                Color.RGBToHSV(red, green, blue, hsv);
                boolean ruleC = (hsv[0] >= 0 && hsv[0] <= 50) && (0.1 <= hsv[1] && hsv[1] <= 0.9);
                if(ruleA && ruleB && ruleC){
                    skinPixels[i][j] = true;
                } else{
                    skinPixels[i][j] = false;
                }
            }
        }
        Bitmap cek = getSkinBitmap(arrBitmap, skinPixels);
        return cek;
    }

    public static Bitmap getSkinBitmap(int[][] arrBitmap, boolean[][] skinPixels){
        Bitmap bitmap = Bitmap.createBitmap(arrBitmap.length, arrBitmap[0].length, Bitmap.Config.ARGB_8888);
        for(int i = 0 ; i < arrBitmap.length ; i++){
            for(int j = 0 ; j < arrBitmap[0].length ; j++){
                if(!skinPixels[i][j]){
                    bitmap.setPixel(i, j, Color.rgb(0, 0, 0));
                } else{
//                    int pixel = arrBitmap[i][j];
//                    int red = (pixel >> 16) & 0xff;
//                    int green = (pixel >> 8) & 0xff;
//                    int blue = (pixel) & 0xff;
                    bitmap.setPixel(i, j, Color.rgb(255, 255, 255));
                }
            }
        }
        return bitmap;
    }

    public static int[][] getPixelsArray(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[][] arr = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = bitmap.getPixel(i,j);
                arr[i][j] = pixel;
            }
        }
        return arr;
    }

    private static int[] getBoundary(boolean[][] skinPixels, int xmin, int xmax, int ymin, int ymax){
        int countX = 0;
        int countY = 0;
        int tempX = 0;
        int tempY = 0;
        int totalSkin = 0;
        for(int a = xmin; a <= xmax; a++){
            for(int b = ymin; b <= ymax; b++){
                if(skinPixels[a][b]){
                    countX++;
                    countY++;
                    tempX += a;
                    tempY += b;
                }
            }
        }
        int centerX = tempX / countX;
        int centerY = tempY / countY;
//        Log.e("centerX", centerX +"");
//        Log.e("centerY", centerY +"");
        countY = 0;
        tempY = 0;
        for(int a = xmin; a <= xmax; a++){
            for(int b = ymin; b < centerY; b++){
                if(skinPixels[a][b]){
                    countY++;
                    tempY += centerY - b;
                    totalSkin++;
                }
            }
        }
        int yplus = tempY / countY;
        countY = 0;
        tempY = 0;
        for(int a = xmin; a <= xmax; a++){
            for(int b = centerY + 1; b <= ymax; b++){
                if(skinPixels[a][b]){
                    countY++;
                    tempY += b - centerY;
                    totalSkin++;
                }
            }
        }
        int yminus = tempY / countY;

        countX = 0;
        tempX = 0;
        for(int a = xmin; a < centerX; a++){
            for(int b = ymin; b <= ymax; b++){
                if(skinPixels[a][b]){
                    countX++;
                    tempX += centerX - a;
                }
            }
        }
        int xplus = tempX / countX;
        countX = 0;
        tempX = 0;
        for(int a = centerX + 1; a <= xmax; a++){
            for(int b = ymin; b <= ymax; b++){
                if(skinPixels[a][b]){
                    countX++;
                    tempX += a - centerX;
                }
            }
        }
        int xminus = tempX / countX;

        int xmin2 = centerX - (xminus * 2);
        int xmax2 = centerX + (xplus * 2);
        int ymin2 = centerY - (yminus * 2);
        int ymax2 = centerY + (yplus * 2);
//        Log.e("xmin2", xmin2 +"");
//        Log.e("xmax2", xmax2 +"");
//        Log.e("ymin2", ymin2 +"");
//        Log.e("ymax2", ymax2 +"");
        return new int[]{xmin2, xmax2, ymin2, ymax2, totalSkin};
    }

    public static void createBoundaryBox(Bitmap bitmap, int xmin, int xmax, int ymin, int ymax){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if(xmin<0)xmin = 0;
        if(xmax>width)xmax = width;
        if(ymin<0)ymin = 0;
        if(ymax>height)ymax = height;
        for(int i = xmin; i < xmax; i++){
            bitmap.setPixel(i, ymin, Color.YELLOW);
            bitmap.setPixel(i, ymax, Color.YELLOW);
        }
        for(int j = ymin; j < ymax; j++){
            bitmap.setPixel(xmin, j, Color.YELLOW);
            bitmap.setPixel(xmax, j, Color.YELLOW);
        }
    }

    public static Bitmap[] sobelOperator(int xMin, int xMax, int yMin, int yMax, Bitmap bitmap){
//        ImageView imgP1bw = findViewById(R.id.imgP1bw);
        Bitmap grayBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        Bitmap edgeBitmap = grayBitmap.copy(Bitmap.Config.ARGB_8888, true);
        int height = grayBitmap.getHeight();
        int width = grayBitmap.getWidth();
        int[][] grayMap = new int[height][width];
//change to grayscale
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int color = bitmap.getPixel(x, y);
                int redVal = Color.red(color);
                int greenVal = Color.green(color);
                int blueVal = Color.blue(color);
                int bwVal = (redVal + greenVal + blueVal) / 3;
                int bwColor = 0xFF000000 | (bwVal << 16 | bwVal << 8 | bwVal);
                int opacity = color >> 24;
                Log.d(TAG, "sobelOperator: "+Color.red(bwColor)+" - "+Color.green(bwColor));
                grayBitmap.setPixel(x, y, bwColor);
                grayMap[y][x] = Color.red(bwColor);
            }
        }
        int pixel = grayBitmap.getPixel(50,50);
        Log.d(TAG, "pixelValue: "+pixel+", rVal = "+Color.red(pixel));
//        return grayBitmap;
//        int width = xMax-xMin;
//        int height = yMax-yMin;
//
//        int gM[][] = new int[height][width];
//
//        for(int i=0; i<height; i++){
//            for(int j=0; j<width; j++){
//                int color = grayBitmap.getPixel(j,i);
//                gM[i][j] = 0x000000FF & color;
//            }
//        }
        Log.d(TAG, "grayMap: "+grayMap.length+" - "+grayMap[0].length);
        Log.d(TAG, "grayScaled: "+grayBitmap.getHeight()+" - "+grayBitmap.getWidth());
//        int pixelGray = Color.red(grayBitmap.getPixel/(112,0));
//        int pixelMap = grayMap[0][112];
        for (int y = 1; y < grayMap.length-2;y++){
            for (int x = 1; x<grayMap[0].length-2;x++){
                int newVal;
                double valRes, valVer, valHor;
//                int Val = grayMap[y][x] * sobel[1][1] + grayMap[y-1][x] * sobel[0][1] + grayMap[y+1][x] * sobel[2][1];
                valVer = grayMap[y][x] * 0 + grayMap[y-1][x] * 0 + grayMap[y-1][x+1] * 1 + grayMap[y][x+1] * 2 + grayMap[y+1][x+1] * 1 + grayMap[y+1][x] * 0 + grayMap[y+1][x-1] * -1 + grayMap[y][x-1] * -2 + grayMap[y-1][x-1] * -1;
//                newVal = grayMap[y][x] * sobel[1][1] + grayMap[y-1][x] * sobel[0][1] + grayMap[y-1][x+1] * sobel[0][2] + grayMap[y][x+1] * sobel[1][2] + grayMap[y+1][x+1] * sobel[2][2] + grayMap[y+1][x] * sobel[2][1] + grayMap[y+1][x-1] * sobel[2][0] + grayMap[y][x-1] * sobel[1][0] + grayMap[y-1][x-1] * sobel[0][0];
                valHor = grayMap[y][x] * 0 + grayMap[y-1][x] * -2 + grayMap[y-1][x+1] * -1 + grayMap[y][x+1] * 0 + grayMap[y+1][x+1] * 1 + grayMap[y+1][x] * 2 + grayMap[y+1][x-1] * 1 + grayMap[y][x-1] * 0 + grayMap[y-1][x-1] * -1;
//                    newVal = grayMap[y][x];
                valRes = Math.sqrt(valVer*valVer + valHor*valHor);

                int Val = (int) valRes;
//                int Val = newVal;
                Val = Math.abs(Val);
//                if (Val<= 0){
//                    Val = 0;
//                }
                Log.d(TAG, "val: "+Val);
                edgeBitmap.setPixel(x,y,Color.rgb(Val,Val,Val));
            }
        }
//        System.exit(0);

//
//        for(int i=2; i<height-2; i++){
//            for(int j=2; j<width-2; j++){
//                int currentVal = gM[i-2][j-2] + 2*gM[i-2][j-1] - 2*gM[i-2][j+1] - gM[i-2][j+2]
//                        + 2*gM[i-1][j-2] + 4*gM[i-1][j-1] - 4*gM[i-1][j+1] - 2*gM[i-1][j+2]
//                        - 2*gM[i+1][j-2] - 4*gM[i+1][j-1] + 4*gM[i+1][j+1] + 2*gM[i+1][j+2]
//                        - gM[i+2][j-2] - 2*gM[i+2][j-1] + 2*gM[i+2][j+1] + gM[i+2][j+2];
//
//                int currentNorm = abs(currentVal)/9;
//
//                int shortVal = 4*gM[i-1][j-1] - 4*gM[i-1][j+1] - 4*gM[i+1][j-1] + 4*gM[i+1][j+1];
//
//                int edgeColor = 0;
//                if(currentVal > 0){
//                    edgeColor = 0xFF;
//                }
//                int currentColor = 0xFF000000 | (currentNorm<<16 | currentNorm<<8 | currentNorm);
//                //int currentColor = 0xFF000000 | edgeColor << 16 | edgeColor << 8 | edgeColor;
//                //int currentColor = 0xFF000000 | shortVal << 16 | shortVal << 8 | shortVal;
//                edgeBitmap.setPixel(j,i,currentColor);
//            }
//        }
//

        Bitmap[] result = new Bitmap[2];
        edgeBitmap = cropBitmap(1,1,edgeBitmap.getWidth()-3,edgeBitmap.getHeight()-3,edgeBitmap);
        Bitmap rgb = cropBitmap(1,1,bitmap.getWidth()-3,bitmap.getHeight()-3,bitmap);
        Bitmap cropabit = maxThreshold(edgeBitmap);
        result[0] = cropabit;
        result[1] = rgb;
//        result[1] = maxThreshold(result[1]);
        return result;
    }
    public static Bitmap maxThreshold(Bitmap bitmap){
        Bitmap newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        for (int y=0; y<bitmap.getHeight(); y++){
            for (int x=0;x<bitmap.getWidth(); x++){
                int pixel = bitmap.getPixel(x,y);
                if (Color.red(pixel) > 230){
                    newBitmap.setPixel(x,y,Color.rgb(255,255,255));
                }else{
                    newBitmap.setPixel(x,y,Color.rgb(0,0,0));
                }
            }
        }
        return newBitmap;
    }

    public static Bitmap detectObject(Bitmap bitmapBiner, Bitmap colorBitmap){
        Bitmap newBitmap = colorBitmap.copy(Bitmap.Config.ARGB_8888,true);
        for (int x = 0;x<bitmapBiner.getWidth(); x++){
            for (int y = 0; y<bitmapBiner.getHeight(); y++){
                int pixel = bitmapBiner.getPixel(x,y);
//                mata
                if (Color.red(pixel) == 255 && y < bitmapBiner.getHeight()/2 && y > bitmapBiner.getHeight()*3/10){
                    newBitmap.setPixel(x,y,Color.rgb(255,0,0));
                }
                //hidung
                if (Color.red(pixel) == 255 && y > bitmapBiner.getHeight()/2){
                    if ( y < bitmapBiner.getHeight()*4/6){
                        newBitmap.setPixel(x,y,Color.rgb(0,255,0));
                    }else if(y < bitmapBiner.getHeight()*8/10){
                        newBitmap.setPixel(x,y,Color.rgb(0,0,255));
                    }
                }
            }
        }
        return newBitmap;
    }
}
