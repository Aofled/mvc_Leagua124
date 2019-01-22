package com.createsmart.aofled.leagua124.modeles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Afactor on 04.04.2017.
 */

public class News extends RealmObject {


    @SerializedName("id_news")
    @Expose
    private int id_news;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("image_size")
    @Expose
    private double image_size;
    @SerializedName("time_create")
    @Expose
    private String timeCreate;

    public int getId_news() {
        return id_news;
    }
    public void setId_news(int id_news) {
        this.id_news = id_news;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public double getImageSize() {
        return image_size;
    }
    public void setImageSize(double image_size) {
        this.image_size = image_size;
    }

    public String getTimeCreate() {
        return timeCreate;
    }
    public void setTimeCreate(String timeCreate) {
        this.timeCreate = timeCreate;
    }
}
