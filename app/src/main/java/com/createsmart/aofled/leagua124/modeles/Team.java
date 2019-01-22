package com.createsmart.aofled.leagua124.modeles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Team extends RealmObject {

    @SerializedName("id_teams")
    @Expose
    private int id_teams;
    @SerializedName("team_name")
    @Expose
    private String team_name;
    @SerializedName("team_img")
    @Expose
    private String team_img;

    public int getIdTeams() {
        return id_teams;
    }
    public void setIdTeams(int id_teams) {
        this.id_teams = id_teams;
    }

    public String getTeamName() {
        return team_name;
    }
    public void setTeamName(String team_name) {
        this.team_name = team_name;
    }

    public String getTeamImg() {
        return team_img;
    }
    public void setTeamImg(String team_img) {
        this.team_img = team_img;
    }


}
