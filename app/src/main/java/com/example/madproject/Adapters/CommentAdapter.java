package com.example.madproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.madproject.Models.Comment;
import com.example.madproject.R;
import com.example.madproject.UserHelper;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
   ArrayList<Comment> comments=new ArrayList<>();
   Context context;

   public CommentAdapter(Context context) {
      this.context = context;
   }

   @NonNull
   @Override
   public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      return new CommentAdapter.CommentViewHolder(LayoutInflater.from(context)
              .inflate(R.layout.comment_item, parent, false));
   }

   @Override
   public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
      Comment comment = comments.get(position);
 holder.getCommentText().setText(comment.getCommentText());
      FirebaseDatabase.getInstance().getReference("users").child(comment.getUserID()).addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {
            UserHelper value = snapshot.getValue(UserHelper.class);
            holder.userName.setText(value.getFname()+" • "+ comment.getDate());
            Glide.with(holder.itemView.getContext())
                    .load(value.getUserImage())
                    .into(holder.userImage);

         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {

         }
      });




   }

   public ArrayList<Comment> getComments() {
      return comments;
   }

   public void setComments(ArrayList<Comment> comments) {
      this.comments = comments;
      notifyDataSetChanged();
   }

   @Override
   public int getItemCount() {
      return (comments==null )? 0: comments.size() ;
   }

   public class CommentViewHolder extends RecyclerView.ViewHolder {

      public ShapeableImageView userImage;
      public MaterialTextView userName;
      public MaterialTextView commentText;

      public MaterialTextView getCommentText() {
         return commentText;
      }

      public void setCommentText() {
         this.commentText = itemView.findViewById(R.id.comment_text);
      }

      public CommentViewHolder(@NonNull View itemView) {
         super(itemView);
         userImage=itemView.findViewById(R.id.user_image);
         userName=itemView.findViewById(R.id.user_name);
         commentText=itemView.findViewById(R.id.comment_text);
      }
   }

}
