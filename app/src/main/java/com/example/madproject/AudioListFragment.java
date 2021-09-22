package com.example.madproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.madproject.Adapters.AudioListAdapter;
import com.example.madproject.Models.Audio;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class AudioListFragment extends Fragment {


    private String mParam1;
    private String mParam2;
    FirebaseFirestore db;
    ArrayList<Audio> audioArrayList;
    RecyclerView audioRecyclerView;
    AudioListAdapter audioListAdapter;
    private Audio audio;

    public AudioListFragment() {
        // Required empty public constructor
    }

    public static AudioListFragment newInstance() {
        AudioListFragment fragment = new AudioListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        audioArrayList = new ArrayList<>();

        audioRecyclerView = view.findViewById(R.id.audio_list);
        audioRecyclerView.setLayoutManager(new LinearLayoutManager(AudioListFragment.this.getActivity()));

        audioListAdapter = new AudioListAdapter(this.getContext(),audioArrayList);
        audioRecyclerView.setAdapter(audioListAdapter);
        populateAudio();

    }
    private void populateAudio() {

        audioRecyclerView.setAdapter(audioListAdapter);
        db.collection("audioPost").orderBy("uploadDate", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            Log.e("Firestore Error",error.getMessage());
                            return;
                        }

                        for(DocumentChange dc:value.getDocumentChanges()){
                            if(dc.getType()==DocumentChange.Type.ADDED){
                                audio = dc.getDocument().toObject(Audio.class);
                                audio.setAudioID(dc.getDocument().getId());
                                audioArrayList.add(audio);

                            }
                            audioListAdapter.notifyDataSetChanged();

                        }
                    }
                });



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio_list, container, false);
    }
}