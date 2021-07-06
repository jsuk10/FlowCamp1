//package com.example.apps;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import static com.example.apps.MainActivity.musicFile;
//
//public class SongFragment extends Fragment {
//    private View view;
//    RecyclerView recyclerView;
//    MusicAdapter musicAdapter;
//    public SongFragment() {
//        // Required empty public constructor
//    }
//
//    public static SongFragment newInstance() {
//        SongFragment songFragment = new SongFragment();
//        return songFragment;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_songs, container, false);
//        recyclerView = view.findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
//        if(!(musicFile.size()<1)) {
//            musicAdapter = new MusicAdapter(getContext(), musicFile);
//            recyclerView.setAdapter(musicAdapter);
//            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
//        }
//         return view;
//        }
//
//
//}
