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
    private String img;
 //   private ImageView imageView;


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

    public String getImg()
    {
        return img;
    }

    Recycler_contestItem(String title, String text, String dday, String date, String img)//, ImageView imageView)
    {
        this.title = title;
        this.text = text;
        this.dday = dday;
        this.date = date;
        this.img = img;
      //  this.imageView = imageView;
    }

}
