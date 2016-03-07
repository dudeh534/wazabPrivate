package com.ourincheon.wazap;

/**
 * Created by Youngdo on 2016-01-19.
 */
public class Recycler_item {

    private String title;
    private String text;
    private String name;
    private int recruit;
    private int member;
    private int clip;


    String getTitle(){
        return this.title;
    }

    String getText(){
        return this.text;
    }
    String getName(){
        return this.name;
    }

    public int getRecruit() {
        return recruit;
    }

    public int getMember() {
        return member;
    }

    public int getClip() {
        return clip;
    }

    Recycler_item(String title, String text, String name, int recruit,int member,int clip){
        this.title=title;
        this.text=text;
        this.name=name;
        this.recruit = recruit;
        this.member = member;
        this.clip = clip;
    }
}
