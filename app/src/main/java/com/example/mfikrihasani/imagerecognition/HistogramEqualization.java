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
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.io.IOException;

public class HistogramEqualization extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    ImageView imageView;
    ImageView imageViewResult;
    Uri imageURI;
    Bitmap scaledBitmap;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histogram_equalization);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageViewResult = (ImageView) findViewById(R.id.imageViewResult);

        Button load = (Button) findViewById(R.id.loadImgHis);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        final TextView textView = (TextView) findViewById(R.id.Teks);

        final SeekBar seekBar =  (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                if(hasImage(imageViewResult)){
//                    countEqualization(prog);
////                    textView.setText("Progress");
//                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int prog = seekBar.getProgress();
                if(hasImage(imageViewResult)){
//                    prog = (double) i * 0.01;
                    countEqualization(prog);
//                        textView.setText("Progress: "+prog);
                }
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
                if(bitmap.getHeight() == bitmap.getWidth()){
                    scaledBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), 360, 360);
                }else if(bitmap.getHeight() > bitmap.getWidth()){
                    scaledBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), 360, 240);
                }else{
                    scaledBitmap = getResizedBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), 240, 360);
                }
                //set bitmap
                countEqualization(1);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void countEqualization(int i){
        imageView.setImageBitmap(scaledBitmap);
        // get RGB
        RGBArr arr = new RGBArr();
        // Get RGBs value per-pixel
        arr.getRGBs(scaledBitmap);
        // count CDF
        Double w = arr.countCDF(i);
        Toast toast =  Toast.makeText(getApplicationContext(),"Progress: "+w,Toast.LENGTH_LONG);
        toast.show();
        //count map of CDF for equalization
        arr.mapCDF();
        //make a new bitmap
        arr.hisEqual(scaledBitmap);

        Bitmap newbitmap = arr.hisEqual(scaledBitmap);

        //draw image
        imageViewResult.setImageBitmap(newbitmap);

        drawHis(arr);
//            arr.hisEqual(bitmap,imageViewResult);

    }

    public void drawHis(RGBArr arr){
        // plotting to Graph :
        GraphView graph;

        BarGraphSeries<DataPoint> series;       //an Object of the PointsGraphSeries for plotting scatter graphs

        graph = (GraphView) findViewById(R.id.rGraph); //RED
        graph.removeAllSeries();
        series= new BarGraphSeries<>(rgbToPlot(arr.rArr));   //initializing/defining series
        series.setColor(Color.RED);
        graph.addSeries(series);                   //adding the series to the GraphView

        graph = (GraphView) findViewById(R.id.gGraph); //GREEN
        graph.removeAllSeries();
        series= new BarGraphSeries<>(rgbToPlot(arr.gArr));   //initializing/defining series
        series.setColor(Color.GREEN);
        graph.addSeries(series);                   //adding the series to the GraphView

        graph = (GraphView) findViewById(R.id.bGraph); //BLUE
        graph.removeAllSeries();
        series= new BarGraphSeries<>(rgbToPlot(arr.bArr));   //initializing/defining series
        series.setColor(Color.BLUE);
        graph.addSeries(series);                   //adding the series to the GraphView

        //get grayscale graph
        graph = (GraphView) findViewById(R.id.grayGraph);
        graph.removeAllSeries();
        series = new BarGraphSeries<>(rgbToPlot(arr.greyArr));
        series.setColor(Color.GRAY);
        graph.addSeries(series);

        //histogram hasil equalisasi.

        graph = (GraphView) findViewById(R.id.rNewGraph);
        graph.removeAllSeries();
        series = new BarGraphSeries<>(rgbToPlot(arr.RNewCount));
        series.setColor(Color.RED);
        graph.addSeries(series);

        graph = (GraphView) findViewById(R.id.gNewGraph);
        graph.removeAllSeries();
        series = new BarGraphSeries<>(rgbToPlot(arr.GNewCount));
        series.setColor(Color.GREEN);
        graph.addSeries(series);

        graph = (GraphView) findViewById(R.id.bNewGraph);
        graph.removeAllSeries();
        series = new BarGraphSeries<>(rgbToPlot(arr.BNewCount));
        series.setColor(Color.BLUE);
        graph.addSeries(series);

        graph = (GraphView) findViewById(R.id.grayNewGraph);
        graph.removeAllSeries();
        series = new BarGraphSeries<>(rgbToPlot(arr.greyNewCount));
        series.setColor(Color.GRAY);
        graph.addSeries(series);
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
