package com.example.sdandeka_mybookwishlist.data;

import java.io.Serializable;

//we have to implement serializable interface to pass the objects through the intent
public class Book implements Serializable{
    private String title;
    private String author;
    private String genre;
    private int year;
    private boolean isRead;

    //can set genre using enumeration in the future
//    TODO: use enum for genre
    public Book(String title, String author, String genre, int year, boolean isRead) {
        //enforce title and author length constrains
        //and that year should be 4 digit integer

        this.title = title;
        this.author = author;
        this.genre = genre;
        this.year = year;
        this.isRead = isRead;

    }

    //all getters and setters
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public String getGenre() {
        return genre;
    }
    public int getYear() {
        return year;
    }
    public boolean getIsRead() {
        return isRead;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setRead(boolean read) {
        isRead = read;
    }

}
