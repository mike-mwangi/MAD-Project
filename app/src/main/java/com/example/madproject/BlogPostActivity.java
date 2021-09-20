package com.example.madproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.madproject.Models.Article;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.model.Document;

public class BlogPostActivity extends AppCompatActivity {
    ImageView blogImage;
    MaterialTextView blogTitle;
    WebView storyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_post);
        Intent intent = getIntent();
        String articleID = intent.getStringExtra("articleID");
        blogImage=findViewById(R.id.blog_image);
        blogTitle=findViewById(R.id.blog_title);
        storyView=findViewById(R.id.story_web_view);

        setContents(articleID);
    }


    private void setContents(String articleID) {
        DocumentReference article= FirebaseFirestore.getInstance().collection("blogPost").document(articleID);
        article.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Article article1 = value.toObject(Article.class);
                storyView.getSettings().setJavaScriptEnabled(true);
                Glide.with(BlogPostActivity.this.getApplicationContext())
                        .load(article1.getBlogImage())
                .into(blogImage);
               storyView.loadData(article1.getBlogStory(), "text/html; charset=utf-8", "UTF-8");
               blogTitle.setText(article1.getBlogTitle());
            }
        });
    }


}