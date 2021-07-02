package com.example.apps;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Tab1Fragment extends Fragment {
    ListView listView;
    ArrayList<String> arrayList;
    ArrayAdapter arrayAdapter;
    View view;
    private Context context;

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
        arrayList = new ArrayList<>(); // 빈 배열 목록.
        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_expandable_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selected_item = (String)adapterView.getItemAtPosition(position);
                Toast.makeText(getActivity(), selected_item, Toast.LENGTH_SHORT).show();
            }
        });
        Button button = (Button) view.findViewById(R.id.tab1_button);
        button.setOnClickListener(v -> clickEvent());
        return view;
    }
    public void clickEvent(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // 사용자에게 권한을 요청합니다.
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 100);
        } else {
            // 앱에 이미 권한이있는 경우이 블록이 실행됩니다.
            readContacts();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    // 사용자가 대화 상자에서 ALLOW를 클릭하면이 메서드가 호출됩니다.
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult
                (requestCode, permissions, grantResults);
        readContacts();
    }

    //콘텐츠 리졸버를 사용하여 연락처를 읽는 함수
    private void readContacts() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        String contact;
        if (cursor.moveToFirst()) {
            do {
                contact = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))+ "\n";
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Integer hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (hasPhone > 0) {
                    Cursor cp = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    if (cp != null && cp.moveToFirst()) {
                        contact += cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        cp.close();
                    }
                }else{
                    contact += "No Phone Number";
                }
                arrayList.add(contact);

            } while (cursor.moveToNext());
            arrayAdapter.notifyDataSetChanged();
        }
    }
}
