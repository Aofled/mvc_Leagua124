package com.createsmart.aofled.leagua124.modeles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeamDetail {

    public static final int type_team=0;
    public static final int type_players=1;


    public int data;


    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id_teams")
    @Expose
    private int id_teams;
    @SerializedName("team_name")
    @Expose
    private String team_name;
    @SerializedName("team_img")
    @Expose
    private String team_img;
    @SerializedName("date_of_creation")
    @Expose
    private String date_of_creation;
    @SerializedName("tк_win")
    @Expose
    private int tк_win;
    @SerializedName("goals_scored_team")
    @Expose
    private int goals_scored_team;
    @SerializedName("mid_age")
    @Expose
    private double mid_age;
    @SerializedName("goals_missing")
    @Expose
    private int goals_missing;
    @SerializedName("id_players")
    @Expose
    private int id_players;
    @SerializedName("number_in_team")
    @Expose
    private String numberInTeam;
    @SerializedName("pl_name")
    @Expose
    private String pl_name;
    @SerializedName("position")
    @Expose
    private String position;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("goals_scored")
    @Expose
    private String goals_scored;

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

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

    public String getTeamDateCreation() {
        return date_of_creation;
    }
    public void setTeamDateCreation(String date_of_creation) {
        this.date_of_creation = date_of_creation;
    }

    public int getTkWin() {
        return tк_win;
    }
    public void setTkWin(int tк_win) {
        this.tк_win = tк_win;
    }

    public int getGoalsScoredTeam() {
        return goals_scored_team;
    }
    public void setGoalsScoredTeam(int goals_scored_team) {
        this.goals_scored_team = goals_scored_team;
    }

    public int getGoalsMissing() {
        return goals_missing;
    }
    public void setGoalsMissing(int goals_missing) {
        this.goals_missing = goals_missing;
    }

    public double getMidAge() {
        return mid_age;
    }
    public void setMidAge(double mid_age) {
        this.mid_age = mid_age;
    }

    public int getIdPlayers() {
        return id_players;
    }
    public void setIdPlayers(int id_players) {
        this.id_players = id_players;
    }

    public String getPlName() {
        return pl_name;
    }
    public void setPlName(String pl_name) {
        this.pl_name = pl_name;
    }

    public String getNumberInTeam() {
        return numberInTeam;
    }
    public void setNumberInTeam(String numberInTeam) {
        this.numberInTeam = numberInTeam;
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
        return goals_scored;
    }
    public void setGoalsScored(String goals_scored) {
        this.goals_scored = goals_scored;
    }




}
