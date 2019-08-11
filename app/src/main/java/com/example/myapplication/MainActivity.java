package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private EditText input;
    private ImageView imageView;
    private TextView status;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = findViewById(R.id.txt_input);
        status = findViewById(R.id.txt_status);
        imageView = findViewById(R.id.img_downloaded_image);

        findViewById(R.id.btn_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String imageUrl = input.getText().toString();
                status.setText("Downloading...");

                new Thread((new Runnable() {

                    @Override
                    public void run() {
                        InputStream inputStream = null;
                        try {
                            Request request = new Request.Builder()
                                    .url(imageUrl)
                                    .build();

                            Response response = client.newCall(request).execute();

                            if (response.body() != null) {

                                inputStream = response.body().byteStream();
                                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        status.setText("Downloaded!");
                                        imageView.setImageBitmap(bitmap);
                                    }
                                });
                            } else {
                                Log.e("Perry", "run: failed!");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (inputStream == null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    status.setText("Error!");
                                }
                            });
                        }
                    }
                })).start();
            }
        });
    }
}
