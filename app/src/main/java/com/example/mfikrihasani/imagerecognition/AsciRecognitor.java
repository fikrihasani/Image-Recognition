package com.example.mfikrihasani.imagerecognition;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStreamWriter;

public class AsciRecognitor extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    Button process;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asci_recognitor);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        process = findViewById(R.id.buttonProcess);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.IntentCode.LOAD_IMAGE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK && data != null) {
                if (requestCode == Constant.IntentCode.LOAD_IMAGE && data.getData() != null) {
                    Cursor cursor = getContentResolver().query(data.getData(), new String[]{MediaStore.Images.Media.DATA}, null, null, null);

                    if (cursor == null)
                        return;

                    cursor.moveToFirst();
                    String imageString = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    cursor.close();

                    Bitmap image = BitmapFactory.decodeFile(imageString);

                    imageView.setImageBitmap(NewImageUtil.getBinaryImage(image, 128));
                } else if (requestCode == Constant.IntentCode.OPEN_CAMERA && data.getExtras().get("data") != null) {
                    Bitmap image = (Bitmap) data.getExtras().get("data");

                    imageView.setImageBitmap(NewImageUtil.getBinaryImage(image, 128));
                } else if (requestCode == 3) {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("configmenori.txt", MODE_PRIVATE));
                    outputStreamWriter.write("test");
                    outputStreamWriter.close();
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, String.format("Error : %s", e.getMessage()), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void loadImage(View view) {
        process.setEnabled(true);
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Constant.IntentCode.LOAD_IMAGE);
    }

    public void openCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Constant.IntentCode.OPEN_CAMERA);
    }

    public void process(View view) {
        Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        Bitmap bm = NewImageUtil.getSkeleton(image)[0];
        imageView.setImageBitmap(bm);
        NewImageUtil.getSkeletonFeature(image, textView);
        process.setEnabled(false);
    }
}
