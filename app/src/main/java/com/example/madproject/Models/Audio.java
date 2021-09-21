package com.example.madproject.Models;

public class Audio {
    private String UserId, audioImage,audioTitle,audio,uploadDate,uploadTIme,audioID;

    public String getAudioID() {
        return audioID;
    }

    public void setAudioID(String audioID) {
        this.audioID = audioID;
    }

    public Audio() {
    }

    public Audio(String userId, String audioImage, String audioTitle, String audio, String uploadDate, String uploadTIme) {
        UserId = userId;
        this.audioImage = audioImage;
        this.audioTitle = audioTitle;
        this.audio = audio;
        this.uploadDate = uploadDate;
        this.uploadTIme = uploadTIme;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getAudioImage() {
        return audioImage;
    }

    public void setAudioImage(String audioImage) {
        this.audioImage = audioImage;
    }

    public String getAudioTitle() {
        return audioTitle;
    }

    public void setAudioTitle(String audioTitle) {
        this.audioTitle = audioTitle;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getUploadTIme() {
        return uploadTIme;
    }

    public void setUploadTIme(String uploadTIme) {
        this.uploadTIme = uploadTIme;
    }
}
