package com.example.apps;

import android.Manifest;
import android.R.layout;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Tab1Fragment extends Fragment {
    ListView listView;
    ArrayList<String> arrayList;
    ArrayAdapter arrayAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listView = getView().findViewById(R.id.tab1_listView); // xml의 listview
        arrayList = new ArrayList<>(); // 빈 배열 목록.
        arrayAdapter = new ArrayAdapter(getActivity(), layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
        listView.setAdapter(arrayAdapter);

        // 연락처 읽기 권한이 부여되었는지 확인합니다.
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // 사용자에게 권한을 요청합니다.
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 100);
        } else {
            // 앱에 이미 권한이있는 경우이 블록이 실행됩니다.
            readContacts();
        }
    }

    // 사용자가 대화 상자에서 ALLOW를 클릭하면이 메서드가 호출됩니다.
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult
                (requestCode, permissions, grantResults);
        readContacts();
    }

    // 콘텐츠 리졸버를 사용하여 연락처를 읽는 함수
    private void readContacts() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
            } while (cursor.moveToNext());
            arrayAdapter.notifyDataSetChanged();
        }
    }
}
