package com.example.madproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.madproject.BlogPostActivity;
import com.example.madproject.Models.Article;
import com.example.madproject.R;
import com.example.madproject.UserHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainArticleAdapter extends RecyclerView.Adapter<MainArticleAdapter.MainArticleViewHolder>{
	
	private Context context;
	private ArrayList<Article> articles;
	
	public MainArticleAdapter(Context context, ArrayList<Article> articles) {
		this.context = context;
		this.articles = articles;
	}
	
	@NonNull
	@Override
	public MainArticleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		return new MainArticleViewHolder(LayoutInflater.from(context)
		.inflate(R.layout.course_item, viewGroup, false));
	}
	
	@Override
	public void onBindViewHolder(@NonNull MainArticleViewHolder mainArticleViewHolder, int i) {
		
		Article article = articles.get(i);
		mainArticleViewHolder.blogTitle.setText(article.getBlogTitle());
		mainArticleViewHolder.blogDate.setText("uploaded on :"+article.getUploadDate());
		FirebaseDatabase.getInstance().getReference("users").child(article.getUserId()).addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				UserHelper value = snapshot.getValue(UserHelper.class);
				mainArticleViewHolder.blogAuthorName.setText("Authored by : "+ value.getFname());
				Glide.with(mainArticleViewHolder.itemView.getContext())
						.load(value.getUserImage())
						.into(mainArticleViewHolder.imgAuthorPic);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});

		
		Glide.with(context)
			.load(article.getBlogImage())
			.into(mainArticleViewHolder.imgArticlePic);
		
		
	}
	
	@Override
	public int getItemCount() {
		if (articles != null) {
			return articles.size();
		}
		return 0;
	}
	
	class MainArticleViewHolder extends RecyclerView.ViewHolder {
		TextView blogTitle;
		TextView blogAuthorName;
		TextView blogDate;
		
		ImageView imgArticlePic;
		ImageView imgAuthorPic;
		ImageView typeOfContent;
		
		
		MainArticleViewHolder(@NonNull View itemView) {
			super(itemView);
			
		blogTitle=itemView.findViewById(R.id.card_blog_title);
		blogAuthorName=itemView.findViewById(R.id.card_blog_author);
		blogDate=itemView.findViewById(R.id.card_blog_date);

			
			imgArticlePic = itemView.findViewById(R.id.imgArticleThumbnail);
			imgAuthorPic = itemView.findViewById(R.id.imgAuthorProfilePic);
			typeOfContent=itemView.findViewById(R.id.course_icon);


			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent=new Intent(view.getContext(), BlogPostActivity.class);
					intent.putExtra("articleID",articles.get(getAdapterPosition()).getBlogID());
					view.getContext().startActivity(intent);
				}
			});
			
		}
	}
}
