package com.example.apps;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Tab1Fragment extends Fragment {
    ListView listView;
    ArrayList<CustomContext> arrayLists;
    ArrayAdapter arrayAdapters;
    View view;

    public Tab1Fragment() {
        // Required empty public constructor
    }

    public static Tab1Fragment newInstance() {
        Tab1Fragment tab1_fragment = new Tab1Fragment();
        return tab1_fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab1, container, false);
        listView = view.findViewById(R.id.tab1_listView); // xml의 listview
        arrayLists = new ArrayList<>(); // 빈 배열 목록.
        arrayAdapters = new ArrayAdapter(getActivity(), android.R.layout.simple_expandable_list_item_1, arrayLists);
        listView.setAdapter(arrayAdapters);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View views, int position, long id) {
                CustomContext selected_item = (CustomContext)adapterView.getItemAtPosition(position);
                Intent call = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+selected_item.getNumber()));
                ((Activity)view.getContext()).startActivity(call);
            }
        });
        view.findViewById(R.id.tab1_button).setOnClickListener(v -> reacContacts());
        view.findViewById(R.id.tab1_button2).setOnClickListener(v -> addContacts());
        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void reacContacts(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // 사용자에게 권한을 요청합니다.
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 100);
        } else {
            // 앱에 이미 권한이있는 경우이 블록이 실행됩니다.
            readContacts();
        }
    }

    public void addContacts(){
        
        //리프레쉬 해줌
        readContacts();
    }

    // 사용자가 대화 상자에서 ALLOW를 클릭하면이 메서드가 호출됩니다.
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult
                (requestCode, permissions, grantResults);
        readContacts();
    }

    //콘텐츠 리졸버를 사용하여 연락처를 읽는 함수
    private void readContacts() {
        String name = "not";
        String number = "dont Have";
        arrayLists.clear();
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))+ "\n";
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Integer hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (hasPhone > 0) {
                    Cursor cp = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    if (cp != null && cp.moveToFirst()) {
                        number = cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        cp.close();
                    }
                }
                arrayLists.add(new CustomContext(name,number));
            } while (cursor.moveToNext());
            arrayAdapters.notifyDataSetChanged();
        }
    }
}
