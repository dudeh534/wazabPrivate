package com.ourincheon.wazap;

import android.widget.ImageView;

/**
 * Created by sue on 2016-03-19.
 */

public class Recycler_contestItem
{

    private String title;
    private String text;
    private String dday;
    private String date;
    private ImageView imageView;


    String getTitle() {
        return this.title;
    }

    String getText() {
        return this.text;
    }

    public String getDday() {
        return dday;
    }

    public String getDate() {
        return date;
    }

    public ImageView getImageView() {
        return imageView;
    }

    Recycler_contestItem(String title, String text, String dday, String date, ImageView imageView) //, int id, String writer)
    {
        this.title = title;
        this.text = text;
        this.dday = dday;
        this.date = date;
        this.imageView = imageView;
        //    this.id = id;
        //    this.writer = writer;
    }

}
