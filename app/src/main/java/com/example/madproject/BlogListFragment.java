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
import com.example.madproject.Adapters.MainArticleAdapter;
import com.example.madproject.Models.Article;
import com.example.madproject.Models.Audio;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BlogListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    ArrayList<Article> blogArrayList;
    RecyclerView blogRecyclerView;
    MainArticleAdapter blogListAdapter;
    private FirebaseFirestore db;

    public BlogListFragment() {
        // Required empty public constructor
    }

    public static BlogListFragment newInstance() {
        BlogListFragment fragment = new BlogListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        blogArrayList = new ArrayList<>();

        blogRecyclerView = view.findViewById(R.id.blog_list);
        blogRecyclerView.setLayoutManager(new LinearLayoutManager(BlogListFragment.this.getActivity()));

        blogListAdapter = new MainArticleAdapter(this.getContext(), blogArrayList);
        populateBlogs();

    }
    private void populateBlogs() {

        blogRecyclerView.setAdapter(blogListAdapter);
        db.collection("blogPost").orderBy("uploadDate", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            Log.e("Firestore Error",error.getMessage());
                            return;
                        }

                        for(DocumentChange dc:value.getDocumentChanges()){
                            if(dc.getType()==DocumentChange.Type.ADDED){
                               Article article= dc.getDocument().toObject(Article.class);
                                article.setBlogID(dc.getDocument().getId());
                                blogArrayList.add(article);

                            }
                            blogListAdapter.notifyDataSetChanged();

                        }
                    }
                });



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blog_list, container, false);
    }
}