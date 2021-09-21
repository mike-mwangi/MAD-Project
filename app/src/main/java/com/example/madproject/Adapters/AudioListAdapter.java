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
import com.example.madproject.Models.Audio;
import com.example.madproject.R;
import com.example.madproject.SingleAudioActivity;
import com.example.madproject.UserHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.AudioListViewHolder>{

	private Context context;
	private ArrayList<Audio> audioArrayList;

	public AudioListAdapter(Context context, ArrayList<Audio> audios) {
		this.context = context;
		this.audioArrayList = audios;
	}
	
	@NonNull
	@Override
	public AudioListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		return new AudioListViewHolder(LayoutInflater.from(context)
		.inflate(R.layout.course_item, viewGroup, false));
	}
	
	@Override
	public void onBindViewHolder(@NonNull AudioListViewHolder mainArticleViewHolder, int i) {
		
		Audio audio= audioArrayList.get(i);
		mainArticleViewHolder.blogTitle.setText(audio.getAudioTitle());
		mainArticleViewHolder.blogDate.setText("uploaded on :"+audio.getUploadDate());
		FirebaseDatabase.getInstance().getReference("users").child(audio.getUserId()).addValueEventListener(new ValueEventListener() {
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
			.load(audio.getAudioImage())
			.into(mainArticleViewHolder.imgArticlePic);
		
		
	}
	
	@Override
	public int getItemCount() {
		if (audioArrayList != null) {
			return audioArrayList.size();
		}
		return 0;
	}
	
	class AudioListViewHolder extends RecyclerView.ViewHolder {
		TextView blogTitle;
		TextView blogAuthorName;
		TextView blogDate;
		
		ImageView imgArticlePic;
		ImageView imgAuthorPic;
		ImageView typeOfContent;
		
		
		AudioListViewHolder(@NonNull View itemView) {
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
					Intent intent=new Intent(view.getContext(), SingleAudioActivity.class);
					intent.putExtra("audioID",audioArrayList.get(getAdapterPosition()).getAudioID());
					view.getContext().startActivity(intent);
				}
			});
			
		}
	}
}
