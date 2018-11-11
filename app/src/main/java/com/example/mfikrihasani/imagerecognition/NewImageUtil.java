package com.example.mfikrihasani.imagerecognition;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.TextView;

import java.util.Scanner;

public class NewImageUtil {

    static String[][] featureKapital = new String[26][2];
    static String[][] featureKecil = new String[26][2];
    static String[][] featureAngka = new String[10][2];





    public static void initFeature(){
        //kapital
        featureKapital[0][0] = "A";featureKapital[0][1] = "2001000100";
        featureKapital[1][0] = "B";featureKapital[1][1] = "0111100101";
        featureKapital[2][0] = "C";featureKapital[2][1] = "2000000000";
        featureKapital[3][0] = "D";featureKapital[3][1] = "0101100010";
        featureKapital[4][0] = "E";featureKapital[4][1] = "3111100000";
        featureKapital[5][0] = "F";featureKapital[5][1] = "3110100000";
        featureKapital[6][0] = "G";featureKapital[6][1] = "2000000000";
        featureKapital[7][0] = "H";featureKapital[7][1] = "4010101000";
        featureKapital[8][0] = "I";featureKapital[8][1] = "2000001000";
        featureKapital[9][0] = "J";featureKapital[9][1] = "2001001000";
        featureKapital[10][0] = "K";featureKapital[10][1] = "4000100000";
        featureKapital[11][0] = "L";featureKapital[11][1] = "2001100000";
        featureKapital[12][0] = "M";featureKapital[12][1] = "4000101000";
        featureKapital[13][0] = "N";featureKapital[13][1] = "2000101000";
        featureKapital[14][0] = "O";featureKapital[14][1] = "0000000010";
        featureKapital[15][0] = "P";featureKapital[15][1] = "1010100100";
        featureKapital[16][0] = "Q";featureKapital[16][1] = "2000000010";
        featureKapital[17][0] = "R";featureKapital[17][1] = "2010100100";
        featureKapital[18][0] = "S";featureKapital[18][1] = "2000000000";
        featureKapital[19][0] = "T";featureKapital[19][1] = "3100010000";
        featureKapital[20][0] = "U";featureKapital[20][1] = "2000101000";
        featureKapital[21][0] = "V";featureKapital[21][1] = "2000000000";
        featureKapital[22][0] = "W";featureKapital[22][1] = "2000000000";
        featureKapital[23][0] = "X";featureKapital[23][1] = "4000000000";
        featureKapital[24][0] = "Y";featureKapital[24][1] = "3000010000";
        featureKapital[25][0] = "Z";featureKapital[25][1] = "2101000000";

        //huruf kecil
        featureKecil[0][0] = "a";featureKecil[0][1] = "2000001001";
        featureKecil[1][0] = "b";featureKecil[1][1] = "2000100001";
        featureKecil[2][0] = "c";featureKecil[2][1] = "2000000000";
        featureKecil[3][0] = "d";featureKecil[3][1] = "2000001001";
        featureKecil[4][0] = "e";featureKecil[4][1] = "1010000100";
        featureKecil[5][0] = "f";featureKecil[5][1] = "4100100000";
        featureKecil[6][0] = "g";featureKecil[6][1] = "1000001100";
        featureKecil[7][0] = "h";featureKecil[7][1] = "3000101000";
        featureKecil[8][0] = "i";featureKecil[8][1] = "2000001000";
        featureKecil[9][0] = "j";featureKecil[9][1] = "2000001000";
        featureKecil[10][0] = "k";featureKecil[10][1] = "4000100000";
        featureKecil[11][0] = "l";featureKecil[11][1] = "2000001000";
        featureKecil[12][0] = "m";featureKecil[12][1] = "4000111000";
        featureKecil[13][0] = "n";featureKecil[13][1] = "3000101000";
        featureKecil[14][0] = "o";featureKecil[14][1] = "0000000010";
        featureKecil[15][0] = "p";featureKecil[15][1] = "2000100100";
        featureKecil[16][0] = "q";featureKecil[16][1] = "2000001100";
        featureKecil[17][0] = "r";featureKecil[17][1] = "3000100000";
        featureKecil[18][0] = "s";featureKecil[18][1] = "2000000000";
        featureKecil[19][0] = "t";featureKecil[19][1] = "4100010000";
        featureKecil[20][0] = "u";featureKecil[20][1] = "3000101000";
        featureKecil[21][0] = "v";featureKecil[21][1] = "3000000000";
        featureKecil[22][0] = "w";featureKecil[22][1] = "5000000000";
        featureKecil[23][0] = "x";featureKecil[23][1] = "4000000000";
        featureKecil[24][0] = "y";featureKecil[24][1] = "3000000000";
        featureKecil[25][0] = "z";featureKecil[25][1] = "3101000000";

        //angka
        featureAngka[0][0] = "0";featureAngka[0][1] = "0000000010";
        featureAngka[1][0] = "1";featureAngka[1][1] = "3000001000";
        featureAngka[2][0] = "2";featureAngka[2][1] = "3001000000";
        featureAngka[3][0] = "3";featureAngka[3][1] = "3000000000";
        featureAngka[4][0] = "4";featureAngka[4][1] = "3001001010";
        featureAngka[5][0] = "5";featureAngka[5][1] = "3110000000";
        featureAngka[6][0] = "6";featureAngka[6][1] = "1000000001";
        featureAngka[7][0] = "7";featureAngka[7][1] = "2100000000";
        featureAngka[8][0] = "8";featureAngka[8][1] = "0010000101";
        featureAngka[9][0] = "9";featureAngka[9][1] = "1000000100";


    }


    // -----------------------------
    // Image Enhancement Algorithm
    // -----------------------------

    public static Bitmap getBinaryImage(Bitmap bitmap, int threshold) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int size = width * height;
        int[] pixels = new int[size];

        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < size; i++) {
            int pixel = pixels[i];
            int grayscale = (((pixel & 0x00ff0000) >> 16) + ((pixel & 0x0000ff00) >> 8) + (pixel & 0x000000ff)) / 3;

            if (grayscale < threshold) {
                pixels[i] = pixel & 0xff000000;
            } else {
                pixels[i] = pixel | 0x00ffffff;
            }
        }

        return Bitmap.createBitmap(pixels, bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
    }

    // -----------------------------
    // Feature Extraction Algorithm
    // -----------------------------

    public static Bitmap[] getSkeleton(Bitmap bitmap) {
        int count;
        int[] border;

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int size = height * width;
        int[] pixels = new int[size];
        int[] pixelsa = new int[size];
        int[] pixelsb = new int[size];

        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        bitmap.getPixels(pixelsa, 0, width, 0, 0, width, height);
        bitmap.getPixels(pixelsb, 0, width, 0, 0, width, height);

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                if ((pixels[i + j * width] & 0x000000ff) != 255) {
                    border = SubImageUtil.floodFill(pixels, i, j, width);

                    do {
                        count = SubImageUtil.zhangSuenStep(pixelsa, border[0], border[1], border[2], border[3], width);
                    }
                    while (count != 0);

                    SubImageUtil.customStep(pixelsb, border[0], border[1], border[2], border[3], i, j, width);
                }
            }
        }

        return new Bitmap[]{
                Bitmap.createBitmap(pixelsa, width, height, bitmap.getConfig()),
                Bitmap.createBitmap(pixelsb, width, height, bitmap.getConfig())
        };
    }

    public static void getSkeletonFeature(Bitmap bitmap, TextView textView) {
        int count;
        int[] border, border2;

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int size = height * width;
        int[] pixels = new int[size];
        int[] pixelsa = new int[size];
        StringBuffer stringBuffer = new StringBuffer();

        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        bitmap.getPixels(pixelsa, 0, width, 0, 0, width, height);

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                if ((pixels[i + j * width] & 0x000000ff) != 255) {
                    border = SubImageUtil.floodFill(pixels, i, j, width);

                    do {
                        count = SubImageUtil.zhangSuenStep(pixelsa, border[0], border[1], border[2], border[3], width);
                    }
                    while (count != 0);

                    border2 = SubImageUtil.getNewBorder(pixelsa, border[0], border[1], border[2], border[3], width);
                    SkeletonFeature sf = SubImageUtil.extractFeature(pixelsa, border2[0], border2[1], border2[2], border2[3], width);

                    stringBuffer.append(String.format("Fitur: \r\n posisi endpoints: %d%d%d%d%d%d%d%d%d\r\n Jumlah Endpoint: %d\r\nGaris horizontal atas: %d\r\nGaris Horizontal tengah: %d\r\nGaris Horizontal Bawah: %d\r\nGaris Vertikal kiri: %d\r\nGaris Vertikal tengah: %d\r\nGaris vertikal kanan: %d\r\nLobang atas: %d\r\nLobang tengah: %d\r\nLobang Bawah: %d\r\n",
                            sf.ep[0],sf.ep[1],sf.ep[2],sf.ep[3],sf.ep[4],sf.ep[5],sf.ep[6],sf.ep[7],sf.ep[8],
                            sf.endpoints.size(),
                            sf.hTop, sf.hMid, sf.hBottom,
                            sf.vLeft, sf.vMid, sf.vRight,
                            sf.lTop, sf.lMid, sf.lBottom));
                }
            }
        }

        textView.setText(stringBuffer);
//        String string = stringBuffer.toString();
//        Scanner scanner = new Scanner(string);
//        String cek="";
//        String set="";
//        while(scanner.hasNextLine()){
//            cek = scanner.nextLine();
//            set = set+matchFeature(cek)+",";
//        }
//        scanner.close();
//        textView.setText("hasil pengenalan adalah: "+set);
//        char hasil = matchFeature(stringBuffer);
//        textView.setText("Hasil Pengenalan: "+hasil);

    }

    public static char matchFeature(String stringBuffer){
        initFeature();
        int maxKapital = -10000000;
        int idKapital = 0;
        int maxKecil = -10000000;
        int idKecil= 0;
        int maxAngka = -10000000;
        int idAngka= 0;


        //cek minimum feature Kapital
        for (int i = 0; i < featureKapital.length; i++){
            String cek = featureKapital[i][1];
            int sumError = 0;

            for (int a = 0; a < stringBuffer.length(); a++){
                if (stringBuffer.charAt(a) == cek.charAt(a)){
                    sumError++;
                }
//                sumError += Math.pow(Character.getNumericValue(stringBuffer.charAt(a)) - Character.getNumericValue(cek.charAt(a)),2);
            }

            if (maxKapital < sumError){
                maxKapital = sumError;
                idKapital = i;
            }
        }

        //cek minimum feature huruf kecil
        for (int i = 0; i < featureKecil.length; i++){
            String cek = featureKecil[i][1];
            int sumError = 0;
            for (int a = 0; a < stringBuffer.length(); a++){
                if (stringBuffer.charAt(a) == cek.charAt(a)){
                    sumError++;
                }
//                sumError += Math.pow(Character.getNumericValue(stringBuffer.charAt(a)) - Character.getNumericValue(cek.charAt(a)),2);
            }
            if (maxKecil < sumError){
                maxKecil= sumError;
                idKecil= i;
            }
        }

        //cek minimum feature angka
        for (int i = 0; i < featureAngka.length; i++){
            String cek = featureAngka[i][1];
            int sumError = 0;
            for (int a = 0; a < stringBuffer.length(); a++){
                if (stringBuffer.charAt(a) == cek.charAt(a)){
                    sumError++;
                }
//                sumError += Math.pow(Character.getNumericValue(stringBuffer.charAt(a)) - Character.getNumericValue(cek.charAt(a)),2);
            }
            if (maxAngka <  sumError){
                maxAngka = sumError;
                idAngka= i;
            }
        }
        if (maxAngka < maxKapital){
            if (maxKapital < maxKecil){
                return featureKecil[idKecil][0].charAt(0);
            }else{
                return featureKapital[idKapital][0].charAt(0);
            }
        }else{
            if (maxAngka < maxKecil){
                return featureKecil[idKecil][0].charAt(0);
            }else{
                return featureAngka[idAngka][0].charAt(0);
            }
        }
    }
}
