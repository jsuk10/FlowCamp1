package com.example.apps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Tab2Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    Tab2GalleryAdapter galleryAdapter;
    List<String> images;
    TextView gallery_number;

    private static final int MY_READ_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab2);

        gallery_number = findViewById(R.id.tab2_gallery_number);
        recyclerView = findViewById(R.id.tab2_recyclerview_gallery_images);

        if(ContextCompat.checkSelfPermission(Tab2Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Tab2Activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_READ_PERMISSION_CODE);
        } else loadImages();
    }

    private void loadImages(){

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        images = Tab2GalleryImages.ImageList(this);
        galleryAdapter = new Tab2GalleryAdapter(this, images, new Tab2GalleryAdapter.PhotoListener() {
            @Override
            public void onPhotoClick(String path) {
                //TODO: something with photo
                Toast.makeText(Tab2Activity.this, ""+path, Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(galleryAdapter);
        gallery_number.setText("Photos (" + images.size() + ")");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_READ_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Read external storage permission granted", Toast.LENGTH_SHORT).show();
                loadImages();
            } else {
                Toast.makeText(this, "Read external storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}