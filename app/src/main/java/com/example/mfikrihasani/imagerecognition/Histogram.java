package com.example.mfikrihasani.imagerecognition;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.io.IOException;

public class Histogram extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    ImageView imageView;
    ImageView imageViewResult;
    Uri imageURI;
    Bitmap scaledBitmap;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histogram);
        imageView = (ImageView) findViewById(R.id.imageView);
        Button load = (Button) findViewById(R.id.loadImgHis);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    private void openGallery(){
        Intent gallery =  new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        startActivityForResult(gallery, PICK_IMAGE);
    }

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
                imageView.setImageBitmap(scaledBitmap);
                drawGraph();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void drawGraph(){
        RGBArr arr = new RGBArr();
        arr.getRGBs(scaledBitmap);
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
