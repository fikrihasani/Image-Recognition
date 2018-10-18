package com.example.mfikrihasani.imagerecognition;

import android.graphics.Bitmap;
import android.graphics.Color;

public class RGBArr {
    public int[] rArr, gArr, bArr, greyArr, RCDF, GCDF, BCDF, greyCDF, RMap, GMap, BMap, greyMap, RNewCount, GNewCount, BNewCount, greyNewCount, specHis, specHisCDF, specNewCount;
    public double[] specHisMap,greyMapNew, RMapNew, BMapNew, GMapNew;
    public int height, width;
    public RGBArr(){

        specHis = new int[256];
        specHisCDF = new int[256];
        specNewCount = new int[256];
        specHisMap = new double[256];

        rArr = new int[256];
        gArr = new int[256];
        bArr = new int[256];
        greyArr= new int[256];

        RCDF = new int[256];
        GCDF = new int[256];
        BCDF= new int[256];
        greyCDF = new int[256];

        RMap = new int[256];
        GMap = new int[256];
        BMap = new int[256];
        greyMap = new int[256];

        greyMapNew = new double[256];
        RMapNew = new double[256];
        BMapNew = new double[256];
        GMapNew = new double[256];

        RNewCount = new int[256];
        GNewCount = new int[256];
        BNewCount = new int[256];
        greyNewCount = new int[256];
    }

    public void getRGBs(Bitmap bitmap){
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        for (int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int pixel = bitmap.getPixel(x, y);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);
//                int grey = (int) Math.round((0.3*red)+(0.3*green)+(0.3*blue));
                int grey = (red + green + blue)/3;
                rArr[red] += 1;
                bArr[green] += 1;
                gArr[blue] += 1;
                greyArr[grey] += 1;
            }
        }
//        return arr;
    }

    public double countCDF(int w){
        double weight = (double) w * 0.01;
//        double weight = 0.5;
//        double ww = (double) w * 0.01;
//        RGBArr cdfArr = new RGBArr();
//        RGBArr cdf = new RGBArr();
//        System.out.println("Nilai arr: "+arr.rArr[2]);
//
        RCDF[0] = rArr[0];
        BCDF[0] = bArr[0];
        GCDF[0] = gArr[0];
        greyCDF[0] = greyArr[0];
        specHisCDF[0] = specHis[0];

        for(int i = 1; i< 256; i++){
            RCDF[i] = (int) Math.round((weight*rArr[i]) + RCDF[i-1]);
            BCDF[i] = (int) Math.round((weight*bArr[i]) + BCDF[i-1]);
            GCDF[i] = (int) Math.round((weight*gArr[i]) + GCDF[i-1]);
            greyCDF[i] = (int) Math.round((weight*greyArr[i]) + greyCDF[i-1]);
            specHisCDF[i] = (int) Math.round((weight*specHis[i]) + specHisCDF[i-1]);

        }
        return weight;
    }
    //
    public void mapCDF(){
//        RGBArr map = new RGBArr();
        for(int i = 0; i<256; i++){
            RMap[i] = (int) Math.round(((double) RCDF[i]*255)/((double) height * (double) width));
            GMap[i] = (int) Math.round(((double)BCDF[i]*255)/((double) height * (double) width));
            BMap[i] = (int) Math.round(((double)BCDF[i]*255)/((double) height * (double)width));
            greyMap[i] = (int) Math.round(((double)greyCDF[i]*255)/((double) height * (double)width));
//            specHisMap[i] = (int) Math.round(((double)specHisCDF[i]*255)/((double) height * (double)width));

        }
    }

    public void mapSpecCDF(){
//        RGBArr map = new RGBArr();
        for(int i = 0; i<256; i++){
            RMapNew[i] = (double) RCDF[i]/RCDF[255];
            GMapNew[i] = (double)BCDF[i]/GCDF[255];
            BMapNew[i] = (double)BCDF[i]/BCDF[255];
            greyMapNew[i] = (double) greyCDF[i]/greyCDF[255];
            specHisMap[i] = (double) specHisCDF[i]/specHisCDF[255];

        }
    }

    public void mapToSpec(){
        for (int i = 0; i < 256; i++) {

        }
    }

    public Bitmap hisEqual(Bitmap bitmap){
//        Bitmap bitmapBW= bitmap.copy(Bitmap.Config.ARGB_8888, true);
//        Bitmap bitmapAfterBW= bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap bitmapAfter= bitmap.copy(Bitmap.Config.ARGB_8888, true);
        for(int i=0; i<height; i++)
        {
            for(int j=0; j<width; j++)
            {
                int pix = 0;
                int alpha = 0xff & (bitmap.getPixel(j, i)>>24);
                int pixel = bitmap.getPixel(j,i);
                int redValue = Color.red(pixel);
                int blueValue = Color.blue(pixel);
                int greenValue = Color.green(pixel);
                /* int redValue = (pixel & 0x00FF0000) >> 16;
                int greenValue = (pixel & 0x0000FF00) >> 8;
                int blueValue = (pixel & 0x000000FF); */
                int bwValue = (redValue+blueValue+greenValue)/3;
//                int bwColour = 0xFF000000 | (bwValue<<16 | bwValue<<8 | bwValue);


//                bitmapBW.setPixel(j,i,bwColour);

//                int currentColor = (alpha << 24) | (RMap[redValue]<<16) | (GMap[greenValue]<<8)|(BMap[blueValue]);
                //int currentColor = 0xFF000000 | (redValue<<16 + greenValue<<8 + blueValue);
//                int currentColor = 0xFF000000 | RMap[redValue]*256*256 + GMap[greenValue]*256 + BMap[blueValue];

//                int currentBWColor = 0xFF000000 | (greyMap[bwValue]<<16 | greyMap[bwValue]<<8 | greyMap[bwValue]);
                //int currentBWColor = 0xFF000000 | bwMap[bwValue]*256*256 + bwMap[bwValue]*256 + bwMap[bwValue];

                bitmapAfter.setPixel(j,i,Color.rgb(RMap[redValue], GMap[greenValue], BMap[blueValue]));
//                bitmapAfterBW.setPixel(j,i,pix);

                //bitmapAfter.setPixel(j,i,0xFFFFFF00);//ini ngawur

                RNewCount[RMap[redValue]] += 1;
                GNewCount[GMap[greenValue]] += 1;
                BNewCount[BMap[blueValue]] += 1;
                greyNewCount[greyMap[bwValue]] += 1;
            }
        }
        return bitmapAfter;
//        imageView.setImageBitmap(bitmapAfterBW);
    }

}
