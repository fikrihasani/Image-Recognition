package com.example.mfikrihasani.imagerecognition.Control;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import static android.content.ContentValues.TAG;

public class PublicUsage {
	//cek apakah image ada atau tidak
	public boolean hasImage(@NonNull ImageView img) {
		Drawable drawable = img.getDrawable();
		boolean hasImage = (drawable != null);
		if (hasImage && (drawable instanceof BitmapDrawable)) {
			hasImage = ((BitmapDrawable) drawable).getBitmap() != null;
		}
		return hasImage;
	}


	//resize bitmap biar ga kegedean
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

	public Bitmap getGrayBit(Bitmap bitmap){
		Bitmap grayBitmap = null;
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();
		int[][] grayMap = new int[height][width];
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
		return grayBitmap;
	}

	public int[][] getGrayArr(Bitmap bitmap){
		Bitmap grayBitmap = null;
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();
		int[][] grayMap = new int[height][width];
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
		return grayMap;
	}

	public int[][] getBitArr(Bitmap bitmap){
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();
		int[][] arr = new int[width][height];
		for (int i = 0; i < arr.length; i++){
			for (int j = 0; j < arr[0].length; j++){
				arr[i][j] = bitmap.getPixel(j,i);
			}
		}
		return arr;
	}

	public Bitmap padding(Bitmap src, int padx, int pady){
		Bitmap output = Bitmap.createBitmap(src.getWidth()+padx,src.getHeight()+pady, Bitmap.Config.ARGB_8888);
		Canvas can = new Canvas(output);
		can.drawARGB(255,255,255,255);
		can.drawBitmap(src, padx, pady, null);
		return output;
	}
}
