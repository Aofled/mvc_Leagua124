package com.createsmart.aofled.leagua124.modeles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Player extends RealmObject {
    @SerializedName("id_player")
    @Expose
    private int id_player;
    @SerializedName("pl_name")
    @Expose
    private String plName;
    @SerializedName("position")
    @Expose
    private String position;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("goals_scored")
    @Expose
    private String goalsScored;
    @SerializedName("team_name")
    @Expose
    private String teamName;

    @SerializedName("team_img_small")
    @Expose
    private String teamImgSmall;

    public int getIdPlayer() {
        return id_player;
    }
    public void setIdPlayer(int idPlayer) {
        this.id_player = idPlayer;
    }

    public String getPlName() {
        return plName;
    }
    public void setPlName(String plName) {
        this.plName = plName;
    }

    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }

    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }

    public String getGoalsScored() {
        return goalsScored;
    }
    public void setGoalsScored(String goalsScored) {
        this.goalsScored = goalsScored;
    }

    public String getTeamName() {
        return teamName;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamImgSmall() {
        return teamImgSmall;
    }
    public void setTeamImgSmall(String teamImgSmall) {
        this.teamImgSmall = teamImgSmall;
    }

}

