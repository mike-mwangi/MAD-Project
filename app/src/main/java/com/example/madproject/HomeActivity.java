package com.example.madproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;
import com.example.madproject.Adapters.MainArticleAdapter;
import com.example.madproject.Models.Article;
import java.util.ArrayList;
import com.google.android.material.navigation.NavigationView;


public class HomeActivity extends AppCompatActivity {

    public static final String[] articleImages = {"https://cdn-images-1.medium.com/max/800/0*uxd3eEv1EyIUdvNi.png","https://cdn-images-1.medium.com/fit/t/800/240/0*d4dwuqe3UgDnmW06","https://cdn-images-1.medium.com/max/1200/1*Px8Aru4UCSh-JZTSbONViw.png", "https://cdn-images-1.medium.com/focal/1600/480/52/53/1*bpuuMHXCzmyyFcRrSliuOg.jpeg"};
    public static final String[] authorImages = {"https://www.freecodecamp.org/news/content/images/size/w100/2019/06/avatar.jpg", "https://cdn-images-1.medium.com/fit/c/50/50/2*G9ReroQ6OmXRWJ9JMJ1Kxg.jpeg","https://cdn-images-1.medium.com/fit/c/50/50/2*0_Gqlwqdkk6L5BoTFzye3Q.jpeg", "https://cdn-images-1.medium.com/fit/c/100/100/1*zDKGzyimQ2BBMt_IACFjOg.jpeg"};


    private RecyclerView rvMainFeed;
    private ArrayList<Article> mainFeedArticles;
    private MainArticleAdapter mainArticleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Initialize views and data structures
        initialize();


        //Populate main feed and set adapter
        populateMainFeedArticles();
        mainArticleAdapter.notifyDataSetChanged();


    }


    private void initialize() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Home");


        mainFeedArticles = new ArrayList<>();

        rvMainFeed = findViewById(R.id.rvMainFeed);
        rvMainFeed.setLayoutManager(new LinearLayoutManager(HomeActivity.this));

        mainArticleAdapter = new MainArticleAdapter(HomeActivity.this, mainFeedArticles);
        rvMainFeed.setAdapter(mainArticleAdapter);

    }


    private void populateMainFeedArticles() {

        mainFeedArticles.add(new Article(articleImages[0], authorImages[0], "Florin Pop", "How to create a Countdown component using React & MomentJS", "4 min read", true, "Based on your reading history", "3/20/2018"));
        mainFeedArticles.add(new Article(articleImages[3], authorImages[3], "Robert Roy Britt", "Coffee, Even a Lot, Linked to Longer Life", "6 min read", true,"Life", "6 days ago"));
        mainFeedArticles.add(new Article(articleImages[2], authorImages[2], "Paolo Rotolo", "Exploring new Coroutines and Lifecycle Architectural Components integration on Android", "7 min read", false, "Programming", "6 days ago"));
        mainFeedArticles.add(new Article(articleImages[1], authorImages[1], "Austin Tindle", "The Plight of a Junior Software Developer", "5 min read", true,"From your network", "5 days ago"));
        mainFeedArticles.add(new Article(articleImages[0], authorImages[0], "Florin Pop", "How to create a Countdown component using React & MomentJS", "4 min read", true, "Based on your reading history", "3/20/2018"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_screen_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

}








