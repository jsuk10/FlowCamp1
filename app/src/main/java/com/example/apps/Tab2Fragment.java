package com.example.apps;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.File;
import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class Tab2Fragment extends Fragment {

    private Context context;
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    public String basePath = null;
    public GridView mGridView;
    public ArrayList<String> uriArr;
    public Tab2GalleryAdapter mCustomImageAdapter;

    private static final int MY_READ_PERMISSION_CODE = 101;

    public Tab2Fragment() {

    }

    public static Tab2Fragment newInstance() {
        Tab2Fragment tab2_fragment = new Tab2Fragment();
        return tab2_fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");

        if (!mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
            }
        }

        basePath = mediaStorageDir.getPath();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = container.getContext();
        view = inflater.inflate(R.layout.fragment_tab2, container, false);
        mGridView = view.findViewById(R.id.gridview); // .xml의 GridView와 연결
        uriArr = new ArrayList<String>();
        swipeRefreshLayout = view.findViewById(R.id.tab2_swiperefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /* swipe 시 진행할 동작 */
                loadImages();
                /* 업데이트가 끝났음을 알림 */
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_READ_PERMISSION_CODE);
        } else {
            loadImages();
        }

        return view;
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        loadImages();
    }

    private void loadImages() {
        uriArr.clear();
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        if(cursor!=null){
            while(cursor.moveToNext()){
                // 사진 경로 Uri 가져오기
                String uri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                uriArr.add(uri);
            }
            cursor.close();
        }

        mCustomImageAdapter = new Tab2GalleryAdapter(getActivity(), uriArr); // 앞에서 정의한 Custom Image Adapter와 연결
        mGridView.setAdapter(mCustomImageAdapter); // GridView가 Custom Image Adapter에서 받은 값을 뿌릴 수 있도록 연결
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                Bitmap imageBitmap = BitmapFactory.decodeFile(uriArr.get(position));
                ImageView showImage = new ImageView(context);
                showImage.setImageBitmap(resizeBitmapImage(imageBitmap, 1000, 1500));

                builder.setView(showImage);

                builder.setNegativeButton("close", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                    }
                });
                builder.show();
            }
        });

        mCustomImageAdapter.notifyDataSetChanged();
    }

    public Bitmap resizeBitmapImage(Bitmap source, int maxWidthResolution, int maxHeightResolution)
    {
        int width = source.getWidth();
        int height = source.getHeight();
        int newWidth = width;
        int newHeight = height;
        float rate = 0.0f;

        if(width > height) {
                rate = maxWidthResolution / (float) width;
                newHeight = (int) (height * rate);
                newWidth = maxWidthResolution;
        }
        else if (width < height) {
                rate = maxHeightResolution / (float) height;
                newWidth = (int) (width * rate);
                newHeight = maxHeightResolution;
        } else {
            newWidth = maxWidthResolution;
            newHeight = maxWidthResolution;
        }

        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }

}