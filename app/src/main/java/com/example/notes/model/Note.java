package com.example.notes.model;

public class Note {
    private String Title;
    private String content;

    public Note() {

    }

    public Note(String title, String content) {
        Title = title;
        this.content = content;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
