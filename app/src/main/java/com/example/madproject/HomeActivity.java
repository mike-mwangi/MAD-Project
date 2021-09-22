package com.example.madproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.madproject.Adapters.MainArticleAdapter;
import com.example.madproject.Models.Article;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {

    public static final String[] articleImages = {"https://cdn-images-1.medium.com/max/800/0*uxd3eEv1EyIUdvNi.png","https://cdn-images-1.medium.com/fit/t/800/240/0*d4dwuqe3UgDnmW06","https://cdn-images-1.medium.com/max/1200/1*Px8Aru4UCSh-JZTSbONViw.png", "https://cdn-images-1.medium.com/focal/1600/480/52/53/1*bpuuMHXCzmyyFcRrSliuOg.jpeg"};
    public static final String[] authorImages = {"https://www.freecodecamp.org/news/content/images/size/w100/2019/06/avatar.jpg", "https://cdn-images-1.medium.com/fit/c/50/50/2*G9ReroQ6OmXRWJ9JMJ1Kxg.jpeg","https://cdn-images-1.medium.com/fit/c/50/50/2*0_Gqlwqdkk6L5BoTFzye3Q.jpeg", "https://cdn-images-1.medium.com/fit/c/100/100/1*zDKGzyimQ2BBMt_IACFjOg.jpeg"};


    private RecyclerView rvMainFeed;
    private ArrayList<Article> mainFeedArticles;
    private MainArticleAdapter mainArticleAdapter;
    private FirebaseFirestore db;
    private FloatingActionButton fab;
    ProgressDialog progressDialog;
    private FragmentTransaction ft;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar=findViewById(R.id.home_toolbar);

        initialize();
        ft = getSupportFragmentManager().beginTransaction();
        BlogListFragment blogListFragment=BlogListFragment.newInstance();
        ft.replace(R.id.fragment_placeholder, blogListFragment);
        ft.commit();
        bottomNavigationView=findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        ft = getSupportFragmentManager().beginTransaction();
                        BlogListFragment blogListFragment=BlogListFragment.newInstance();
                        ft.replace(R.id.fragment_placeholder, blogListFragment);
                        ft.commit();
                        break;
                    case R.id.audio:
                        ft = getSupportFragmentManager().beginTransaction();
                        AudioListFragment audioListFragment=AudioListFragment.newInstance();
                        ft.replace(R.id.fragment_placeholder, audioListFragment);
                        ft.commit();
                        break;

                }
                return true;
    }
});


        //Populate main feed and set adapter

        //Open the new blog/audio content page
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toPost= new Intent(HomeActivity.this,PostingActivity.class);
                startActivity(toPost);

            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.account)
                {
                    startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
                }
                return true;
            }
        });



    }


    private void initialize() {
        fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(HomeActivity.this,PostingActivity.class));
           }
       });
        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserHelper value = snapshot.getValue(UserHelper.class);
                Glide.with(HomeActivity.this)
                        .load(value.getUserImage())
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                toolbar.getMenu().getItem(0).setIcon(resource);
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

   private void populateMainFeedArticles() {
       /* mainFeedArticles.add(new Article(articleImages[0], authorImages[0], "Florin Pop", "How to create a Countdown component using React & MomentJS", "4 min read", true, "Based on your reading history", "3/20/2018"));
        mainFeedArticles.add(new Article(articleImages[3], authorImages[3], "Robert Roy Britt", "Coffee, Even a Lot, Linked to Longer Life", "6 min read", true,"Life", "6 days ago"));
        mainFeedArticles.add(new Article(articleImages[2], authorImages[2], "Paolo Rotolo", "Exploring new Coroutines and Lifecycle Architectural Components integration on Android", "7 min read", false, "Programming", "6 days ago"));
        mainFeedArticles.add(new Article(articleImages[1], authorImages[1], "Austin Tindle", "The Plight of a Junior Software Developer", "5 min read", true,"From your network", "5 days ago"));
        mainFeedArticles.add(new Article(articleImages[0], authorImages[0], "Florin Pop", "How to create a Countdown component using React & MomentJS", "4 min read", true, "Based on your reading history", "3/20/2018")); */

        rvMainFeed.setAdapter(mainArticleAdapter);
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
                                Article e = dc.getDocument().toObject(Article.class);
                                e.setBlogID(dc.getDocument().getId());
                                mainFeedArticles.add(e);

                            }
                            mainArticleAdapter.notifyDataSetChanged();
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                        }
                    }
                });



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_screen_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

}








