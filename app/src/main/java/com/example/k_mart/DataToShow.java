package com.example.k_mart;

import android.graphics.Bitmap;

public class DataToShow {

    private String first;
    private String second;
    private String third;
    private String imageUrl;
    private String uid;
    private String id;

    DataToShow(){

    }

    public void addFirst(String first){
        this.first = first;
    }

    public void addSecond(String second){
        this.second = second;
    }

    public void addThird(String third){
        this.third = third;
    }

    public void addImageUrl(String url){
        this.imageUrl = url;
    }

    public void addUid(String uid){
        this.uid = uid;
    }

    public void addId(String id){
        this.id = id;
    }

    public String getFirst(){
        return  this.first;
    }

    public String getSecond(){
        return this.second;
    }

    public String getThird(){
        return this.third;
    }

    public String getImageUrl(){
        return  this.imageUrl;
    }

    public String getUid(){
        return this.uid;
    }

    public String getId(){
        return this.id;
    }

}
