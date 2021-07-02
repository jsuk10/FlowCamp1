package com.example.apps;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class Tab1Fragment extends Fragment {
    EditText name;
    EditText phone;
    ListView listView;
    ArrayList<CustomContext> arrayLists;
    ArrayAdapter arrayAdapters;
    SwipeRefreshLayout swipeRefreshLayout;
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
        name = view.findViewById(R.id.tab1_inputName);
        phone = view.findViewById(R.id.tab1_inputNumber);
        swipeRefreshLayout = view.findViewById(R.id.tab1_swiperefresh);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View views, int position, long id) {
                CustomContext selected_item = (CustomContext)adapterView.getItemAtPosition(position);
                Intent call = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+selected_item.getNumber()));
                ((Activity)view.getContext()).startActivity(call);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /* swipe 시 진행할 동작 */
                readContacts();
                /* 업데이트가 끝났음을 알림 */
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        readContacts();
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
        View dialogView = getLayoutInflater().inflate(R.layout.addcontacts, null);

        name = (EditText)dialogView.findViewById(R.id.tab1_inputName);
        phone = (EditText)dialogView.findViewById(R.id.tab1_inputNumber);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("연락처 추가").setMessage("이름과 번호를 기록해 주세요");
        builder.setView(dialogView);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int pos)
            {
                if(!name.getText().toString().isEmpty() && !phone.getText().toString().isEmpty()){
                    //이제 파싱해서 넣는 함수임. 빠방~~
                    addContext();
                }else{
                    Toast.makeText(getActivity(), "Please fill all text", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getActivity(),name.getText().toString() + "\n" + phone.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Toast.makeText(getActivity(), "Cancel Add", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void addContext(){
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.NAME,name.getText().toString());
        intent.putExtra(ContactsContract.Intents.Insert.PHONE,phone.getText().toString());
        ((Activity)view.getContext()).startActivity(intent);
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
