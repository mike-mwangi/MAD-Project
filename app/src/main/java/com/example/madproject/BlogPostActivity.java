package com.example.madproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.madproject.Adapters.CommentAdapter;
import com.example.madproject.Models.Article;
import com.example.madproject.Models.Comment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.model.Document;

import java.util.ArrayList;

public class BlogPostActivity extends AppCompatActivity {
    ImageView blogImage;
    MaterialTextView blogTitle;
    WebView storyView;
    RecyclerView commentRecyclerView;
    TextInputEditText commentInput;
    MaterialButton postComment;
    ShapeableImageView authorImage;
    MaterialTextView authorName;
    private Article article;
    private CommentAdapter commentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_post);
        Intent intent = getIntent();
        String articleID = intent.getStringExtra("articleID");
        blogImage=findViewById(R.id.blog_image);
        blogTitle=findViewById(R.id.blog_title);
        storyView=findViewById(R.id.story_web_view);
        commentRecyclerView=findViewById(R.id.comments);
        commentInput= (TextInputEditText) ((TextInputLayout)findViewById(R.id.comment_input)).getEditText();
        postComment=findViewById(R.id.post_comment);
        authorImage=findViewById(R.id.author_image);
        authorName=findViewById(R.id.author_name);

        commentAdapter = new CommentAdapter(this);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentRecyclerView.setAdapter(commentAdapter);




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

                commentAdapter.setComments(article1.getComments());
                storyView.loadData(article1.getBlogStory(), "text/html; charset=utf-8", "UTF-8");
                blogTitle.setText(article1.getBlogTitle());

                FirebaseDatabase.getInstance().getReference("users").child(article1.getUserId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserHelper value1 = snapshot.getValue(UserHelper.class);
                        Glide.with(BlogPostActivity.this)
                                .load(value1.getUserImage())
                                .into(authorImage);
                        authorName.setText(value1.getFname());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
    }
  /*  private void setUpRecyclerView() {
        Query query = .orderBy("jobID", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Job> options = new FirestoreRecyclerOptions.Builder<Job>()
                .setQuery(query, Job.class)
                .build();
        adapter = new JobsRecyclerViewAdapter(options);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(this);

    }

   */


}