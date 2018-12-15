package com.example.mfikrihasani.imagerecognition.Control;

import android.graphics.Bitmap;
import android.util.Log;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.log;
import static java.lang.Math.sin;

public class FFTRosettaCode {
    PublicUsage publicUsage = new PublicUsage();
    public int bitReverse(int n, int bits) {
        int reversedN = n;
        int count = bits - 1;

        n >>= 1;
        while (n > 0) {
            reversedN = (reversedN << 1) | (n & 1);
            count--;
            n >>= 1;
        }

        return ((reversedN << count) & ((1 << bits) - 1));
    }

    public void fft(RosettaComplex[] buffer) {

        int bits = (int) (log(buffer.length) / log(2));
        for (int j = 1; j < buffer.length / 2; j++) {

            int swapPos = bitReverse(j, bits);
            RosettaComplex temp = buffer[j];
            buffer[j] = buffer[swapPos];
            buffer[swapPos] = temp;
        }

        for (int N = 2; N <= buffer.length; N <<= 1) {
            for (int i = 0; i < buffer.length; i += N) {
                for (int k = 0; k < N / 2; k++) {

                    int evenIndex = i + k;
                    int oddIndex = i + k + (N / 2);
                    RosettaComplex even = buffer[evenIndex];
                    RosettaComplex odd = buffer[oddIndex];

                    double term = (-2 * PI * k) / (double) N;
                    RosettaComplex exp = (new RosettaComplex(cos(term), sin(term)).mult(odd));

                    buffer[evenIndex] = even.add(exp);
                    buffer[oddIndex] = even.sub(exp);
                }
            }
        }
    }

    public void startFFT(Bitmap bitmap) {
//        System.out.println("aaaa");
        Log.d("Cek", "startFFT: "+Math.sqrt(-1));
        double[] input = {1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0};
//        Bitmap grayBit = publicUsage.getGrayBit(bitmap);
//        Bitmap padImg = getRadix2Bit(grayBit);
        RosettaComplex[] cinput = new RosettaComplex[input.length];
        for (int i = 0; i < input.length; i++){
            cinput[i] = new RosettaComplex(input[i], 0.0);
//            System.out.println("cinput: "+cinput[i]);
        }

        fft(cinput);
        System.out.println("Results:");
        for (RosettaComplex c : cinput) {
            System.out.println(c);
        }
    }

    public Bitmap getRadix2Bit(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = 0;
        int newHeight = 0;
        for(int i= 2; i<width;i<<=1){
            newWidth = 2*i;
        }
        for (int j = 2; j<height; j<<=1){
            newHeight = 2*j;
        }
        int padHeight = newHeight-height;
        int padWidth = newWidth-width;
        Bitmap paddedBit = publicUsage.padding(bitmap,padWidth, padHeight);
        return paddedBit;
    }
}
