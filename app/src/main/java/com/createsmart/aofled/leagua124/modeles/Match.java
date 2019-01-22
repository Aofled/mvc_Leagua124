package com.createsmart.aofled.leagua124.modeles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Match extends RealmObject {

    @SerializedName("id_matches")
    @Expose
    private int id_matches;
    @SerializedName("id_team1")
    @Expose
    private int idTeam1;
    @SerializedName("team_name1")
    @Expose
    private String teamName1;
    @SerializedName("team_img1")
    @Expose
    private String teamImg1;
    @SerializedName("id_team2")
    @Expose
    private int idTeam2;
    @SerializedName("team_name2")
    @Expose
    private String teamName2;
    @SerializedName("team_img2")
    @Expose
    private String teamImg2;
    @SerializedName("score_t1")
    @Expose
    private int scoreT1;
    @SerializedName("score_t2")
    @Expose
    private int scoreT2;
    @SerializedName("goals_scored_players1")
    @Expose
    private String goalsScPl1;
    @SerializedName("goals_scored_players2")
    @Expose
    private String goalsScPl2;
    @SerializedName("time_game")
    @Expose
    private String timeGame;
    @SerializedName("match_over")
    @Expose
    private int matchOver;

    public int getIdMatches() {
        return id_matches;
    }
    public void setIdMatches(int id_matches) {
        this.id_matches = id_matches;
    }

    public int getIdTeam1() {
        return idTeam1;
    }
    public void setIdTeam1(int idTeam1) {
        this.idTeam1 = idTeam1;
    }

    public String getTeamName1() {
        return teamName1;
    }
    public void setTeamName1(String teamName1) {
        this.teamName1 = teamName1;
    }

    public String getTeamImg1() {
        return teamImg1;
    }
    public void setTeamImg1(String teamImg1) {
        this.teamImg1 = teamImg1;
    }

    public int getIdTeam2() {
        return idTeam2;
    }
    public void setIdTeam2(int idTeam2) {
        this.idTeam2 = idTeam2;
    }

    public String getTeamName2() {
        return teamName2;
    }
    public void setTeamName2(String teamName2) {
        this.teamName2 = teamName2;
    }

    public String getTeamImg2() {
        return teamImg2;
    }
    public void setTeamImg2(String teamImg2) {
        this.teamImg2 = teamImg2;
    }

    public int getScoreT1() {
        return scoreT1;
    }
    public void setScoreT1(int scoreT1) {
        this.scoreT1 = scoreT1;
    }

    public int getScoreT2() {
        return scoreT2;
    }
    public void setScoreT2(int scoreT2) {
        this.scoreT2 = scoreT2;
    }

    public String getGoalsScPl1() {
        return goalsScPl1;
    }
    public void setGoalsScPl1(String goalsScPl1) {
        this.goalsScPl1 = goalsScPl1;
    }

    public String getGoalsScPl2() {
        return goalsScPl2;
    }
    public void setGoalsScPl2(String goalsScPl2) {
        this.goalsScPl2 = goalsScPl2;
    }

    public String getTimeGame() {
        return timeGame;
    }
    public void setTimeGame(String timeGame) {
        this.timeGame = timeGame;
    }

    public int getMatchOver() {
        return matchOver;
    }
    public void setMatchOver(int matchOver) {
        this.matchOver = matchOver;
    }

}
