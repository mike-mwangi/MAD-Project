package com.example.madproject.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Article implements Serializable {
	
	private String blogImage;
	private String blogStory;
	private String authorProfilePic;
	private String UserId;
	private String blogTitle;
	private String uploadDate;
	private boolean premium;
	private String topic;
	private String uploadTime;
	private String blogID;
	ArrayList<Comment> Comments;


	public ArrayList<Comment> getComments() {
		return Comments;
	}

	public void setComments(ArrayList<Comment> comments) {
		this.Comments = comments;
	}

	public String getBlogStory() {
		return blogStory;
	}

	public void setBlogStory(String blogStory) {
		this.blogStory = blogStory;
	}

	public Article() {

	}
	
	public Article(String articleImage, String authorProfilePic, String author, String title, String duration, boolean premium, String topic, String timestamp) {
		this.blogImage = articleImage;
		this.authorProfilePic = authorProfilePic;
		this.UserId = author;
		this.blogTitle = title;
		this.uploadDate = duration;
		this.premium = premium;
		this.topic = topic;
		this.uploadTime = timestamp;
	}
	
	public Article(String articleImage, String authorProfilePic, String author, String title, String duration, boolean premium, String timestamp) {
		this.blogImage = articleImage;
		this.authorProfilePic = authorProfilePic;
		this.UserId = author;
		this.blogTitle = title;
		this.uploadDate = duration;
		this.premium = premium;
		this.uploadTime = timestamp;
	}
	
	public String getUserId() {
		return UserId;
	}
	
	public void setUserId(String userId) {
		this.UserId = userId;
	}
	
	public String getBlogTitle() {
		return blogTitle;
	}
	
	public void setBlogTitle(String blogTitle) {
		this.blogTitle = blogTitle;
	}
	
	public String getUploadDate() {
		return uploadDate;
	}
	
	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}
	
	public String getBlogImage() {
		return blogImage;
	}
	
	public void setBlogImage(String blogImage) {
		this.blogImage = blogImage;
	}
	
	public String getAuthorProfilePic() {
		return authorProfilePic;
	}
	
	public void setAuthorProfilePic(String authorProfilePic) {
		this.authorProfilePic = authorProfilePic;
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public String getTopic() {
		return topic;
	}
	
	public boolean isPremium() {
		return premium;
	}
	
	public void setPremium(boolean premium) {
		this.premium = premium;
	}
	
	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}
	
	public String getUploadTime() {
		return uploadTime;
	}

	public String getBlogID() {
		return blogID;
	}
	public void setBlogID(String blogID){
		this.blogID=blogID;

	}
}
