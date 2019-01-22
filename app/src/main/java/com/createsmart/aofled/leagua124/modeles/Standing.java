package com.createsmart.aofled.leagua124.modeles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Standing extends RealmObject {

    @SerializedName("id_standings")
    @Expose
    private int id_standings;
    @SerializedName("namber")
    @Expose
    private String namber;
    @SerializedName("winning_place")
    @Expose
    private int winningPlace;
    @SerializedName("up_down")
    @Expose
    private int upDown;
    @SerializedName("id_teams")
    @Expose
    private int id_team;
    @SerializedName("team_name")
    @Expose
    private String nameTeam;
    @SerializedName("games")
    @Expose
    private String games;
    @SerializedName("winning")
    @Expose
    private String winning;
    @SerializedName("draws")
    @Expose
    private String draws;
    @SerializedName("defeats")
    @Expose
    private String defeats;
    @SerializedName("goals")
    @Expose
    private String goals;
    @SerializedName("points")
    @Expose
    private String points;

    public int getIdStandings() {
        return id_standings;
    }
    public void setIdStandings(int id_standings) {
        this.id_standings = id_standings;
    }

    public String getNamber() {
        return namber;
    }
    public void setNamber(String namber) {
        this.namber = namber;
    }

    public int getWinningPlace() {
        return winningPlace;
    }
    public void setWinningPlace(int winningPlace) {
        this.winningPlace = winningPlace;
    }

    public int getUpDown() {
        return upDown;
    }
    public void setUpDown(int upDown) {
        this.upDown = upDown;
    }

    public int getIdTeam() {
        return id_team;
    }
    public void setIdTeam(int id_team) {
        this.id_team = id_team;
    }

    public String getNameTeam() {
        return nameTeam;
    }
    public void setNameTeam(String nameTeam) {
        this.nameTeam = nameTeam;
    }

    public String getGames() {
        return games;
    }
    public void setGames(String games) {
        this.games = games;
    }

    public String getWinning() {
        return winning;
    }
    public void setWinning(String winning) {
        this.winning = winning;
    }

    public String getDraws() {
        return draws;
    }
    public void setDraws(String draws) {
        this.draws = draws;
    }

    public String getDefeats() {
        return defeats;
    }
    public void setDefeats(String defeats) {
        this.defeats = defeats;
    }

    public String getGoals() {
        return goals;
    }
    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getPoints() {
        return points;
    }
    public void setPoints(String points) {
        this.points = points;
    }

}


