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
import android.widget.SeekBar;

import com.example.mfikrihasani.imagerecognition.Control.RGBArr;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.io.IOException;

public class HistogramSpecification extends AppCompatActivity {

    Bitmap bitmap;
    Bitmap scaledBitmap;
    Uri imageURI;
    int PICK_IMAGE = 100;
    ImageView imageView;
    ImageView imageViewResult;
    int ay;
    int bx;
    int by;
    int cy;
    double slope, slopeDec;
    RGBArr arr = new RGBArr();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histogram_specification);
        Button button = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.imgAwal);
        imageViewResult = (ImageView) findViewById(R.id.imgAkhir);

        final SeekBar seekBarA = (SeekBar) findViewById(R.id.seekBarA);
        final SeekBar seekBarB = (SeekBar) findViewById(R.id.seekBarB);
        final SeekBar seekBarC = (SeekBar) findViewById(R.id.seekBarC);

        seekBarA.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(hasImage(imageViewResult)){
                    int progressA = seekBarA.getProgress();
                    int progressB = seekBarB.getProgress();
                    int progressC = seekBarC.getProgress();
                    init(arr, progressA, progressB, progressC);
                    countEqualization();
                }
            }
        });

        seekBarB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(hasImage(imageViewResult)){
                    int progressA = seekBarA.getProgress();
                    int progressB = seekBarB.getProgress();
                    int progressC = seekBarC.getProgress();
                    init(arr, progressA, progressB, progressC);
                    countEqualization();
                }
            }
        });

        seekBarC.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(hasImage(imageViewResult)){
                    int progressA = seekBarA.getProgress();
                    int progressB = seekBarB.getProgress();
                    int progressC = seekBarC.getProgress();
                    init(arr, progressA, progressB, progressC);
                    countEqualization();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    private void init(RGBArr specArr, int a, int b, int c){
//        RGBArr specArr = new RGBArr();
        ay = (a*by)/100;
        bx = (b*255)/100;
        by = 3500;
        cy = (c*by)/100;

        slope = (double) (by-ay)/bx;
        slopeDec = (double) (cy-by)/(255-bx);

        //init slope inc
        specArr.specHis[0] = ay;
        specArr.specHis[bx] = by;

        for(int i = 1; i<bx; i++){
            specArr.specHis[i] = (int) Math.round(slope + specArr.specHis[i-1]);
        }
        for (int i = bx+1; i<256; i++) {
            specArr.specHis[i] = (int) Math.round(slopeDec + specArr.specHis[i-1]);
        }

        GraphView graph;

        BarGraphSeries<DataPoint> series;

        graph = (GraphView) findViewById(R.id.graySpecGraph);
        graph.removeAllSeries();
        series = new BarGraphSeries<>(rgbToPlot(specArr.specHis));
        series.setColor(Color.GRAY);
        graph.addSeries(series);
    }


    private boolean hasImage(@NonNull ImageView img){
        Drawable drawable = img.getDrawable();
        boolean hasImage = (drawable != null);
        if(hasImage && (drawable instanceof BitmapDrawable)){
            hasImage = ((BitmapDrawable) drawable).getBitmap()!=null;
        }
        return hasImage;
    }

    private void openGallery(){
        Intent gallery =  new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK && requestCode == PICK_IMAGE){
            // Load Image File
            imageURI = data.getData();
            bitmap = null;
            scaledBitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURI);
//                imageView.setImageBitmap(bitmap);
                if(bitmap.getHeight() == bitmap.getWidth()){
                    scaledBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), 360, 360);
                }else if(bitmap.getHeight() > bitmap.getWidth()){
                    scaledBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), 360, 240);
                }else{
                    scaledBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), 240, 360);
                }
                //set bitmap
                countEqualization();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void countEqualization(){
        imageView.setImageBitmap(scaledBitmap);
        // get RGB
//        RGBArr arr = new RGBArr();
        // Get RGBs value per-pixel
        arr.getRGBs(scaledBitmap);
        //init spec histogram
//        init(arr);
        // count CDF
        arr.countCDF(100);
//        Toast toast =  Toast.makeText(getApplicationContext(),"Progress: "+w,Toast.LENGTH_LONG);
//        toast.show();

        //count map of CDF for specification
        arr.mapCDF();
        arr.mapSpecCDF();

        histMatchGray(arr);
        Bitmap result = specTransform(arr,scaledBitmap);
//
//        //make a new bitmap
////        arr.hisEqual(scaledBitmap);
////
////        Bitmap newbitmap = arr.hisEqual(scaledBitmap);
////
////        //draw image
        imageViewResult.setImageBitmap(result);
//
        drawHis(arr);
//            arr.hisEqual(bitmap,imageViewResult);

    }

    public void drawHis(RGBArr arr){
        // plotting to Graph :
        GraphView graph;

        BarGraphSeries<DataPoint> series;       //an Object of the PointsGraphSeries for plotting scatter graphs

        //get grayscale graph
        graph = (GraphView) findViewById(R.id.imGrayGraph);
        graph.removeAllSeries();
        series = new BarGraphSeries<>(rgbToPlot(arr.greyArr));
        series.setColor(Color.GRAY);
        graph.addSeries(series);

        //histogram hasil Spesifikasi.

        graph = (GraphView) findViewById(R.id.grayResultSpecGraph);
        graph.removeAllSeries();
        series = new BarGraphSeries<>(rgbToPlot(arr.specNewCount));
        series.setColor(Color.GRAY);
        graph.addSeries(series);
    }
    //transform image
    public Bitmap specTransform(RGBArr arr, Bitmap bm){
        Bitmap res = bm.copy(Bitmap.Config.ARGB_8888, true);
        int[][] lookUp = new int[3][256];
        int h = res.getHeight();
        int w = res.getWidth();
        res.setHasAlpha(true);
        // get normalized histograms
        double[][] normCfd = new double[3][256];
        normCfd[0] = arr.RMapNew;
        normCfd[1] = arr.GMapNew;
        normCfd[2] = arr.BMapNew;
        double [] mskAcc = arr.specHisMap;
        // matching to mask
        for(int i = 0; i < 256; i++){
            lookUp[0][i] = getClosestIndex(mskAcc, normCfd[0][i]); //RED
            lookUp[1][i] = getClosestIndex(mskAcc, normCfd[1][i]); //GREEN
            lookUp[2][i] = getClosestIndex(mskAcc, normCfd[2][i]); //BLUE
        }
        // map to bitmap
        for(int y = 0; y < h; y++){
            for(int x = 0; x < w; x++){
                int pixel = res.getPixel(x, y);
                int r = Color.red(pixel);
                int b = Color.blue(pixel);
                int g = Color.green(pixel);
                int greyLookUp = (lookUp[0][r] + lookUp[0][g] + lookUp[0][b])/3;
                arr.specNewCount[greyLookUp] += 1;
                res.setPixel(x, y, Color.rgb(lookUp[0][r],lookUp[1][g],lookUp[2][b]));
            }
        }
        return res;
    }
    // Histogram matching single channel only
    public void histMatchGray(RGBArr arr){
        double[] res = arr.specHisMap.clone();
        int[] hist = new int[256];
//        // get normalized histograms
//        double[] inpAcc = normalize(accHist(inp, w));
//        double [] mskAcc = normalize(accHist(mask, w));
        // matching to mask
        for(int i = 0;i < 256;i++){
            int pointer = getClosestIndex(arr.specHisMap, arr.greyMapNew[i]);
            res[i] = pointer;
            System.out.println("Grey value: "+arr.greyMapNew[i]+" -- Map Value:  "+arr.specHisMap[i]+"Pointer: "+pointer);
            arr.specNewCount[pointer] += 1;
        }

//        return new int[][] {res, hist};
    }

    public int getClosestIndex(double[] a, double x) {
        int low = 0;
        int high = a.length - 1;

        if (high < 0)
            throw new IllegalArgumentException("The array cannot be empty");
        while (low < high) {
            int mid = (low + high) / 2;
            assert(mid < high);
            double d1 = Math.abs(a[mid  ] - x);
            double d2 = Math.abs(a[mid+1] - x);
            if (d2 <= d1) {
                low = mid+1;
            }
            else {
                high = mid;
            }
        }
        return high;
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

        return newBitmap;
    }

    public DataPoint[] rgbToPlot(int[] arr){
        int n = arr.length;
        DataPoint[] values = new DataPoint[n];     //creating an object of type DataPoint[] of size 'n'
        for(int i=0;i<n;i++){
            DataPoint v = new DataPoint(i,arr[i]);
            values[i] = v;
        }
        return values;
    }
}
