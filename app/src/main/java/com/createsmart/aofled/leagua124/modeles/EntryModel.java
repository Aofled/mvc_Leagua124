package com.createsmart.aofled.leagua124.modeles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Afactor on 04.04.2017.
 */

public class EntryModel {

    @SerializedName("news")
    @Expose
    private ArrayList<News> news = null;

    @SerializedName("matches")
    @Expose
    private ArrayList<Match> matches = null;

    @SerializedName("standings")
    @Expose
    private ArrayList<Standing> standings = null;

    @SerializedName("teams")
    @Expose
    private ArrayList<Team> teams = null;

    @SerializedName("teams_detals")
    @Expose
    private ArrayList<TeamDetail> teamDetails = null;

    @SerializedName("players")
    @Expose
    private ArrayList<Player> players = null;

    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("surplus")
    @Expose
    private int surplus;

    public ArrayList<News> getNews() {
        return news;
    }
    public ArrayList<Match> getMatches() {
        return matches;
    }
    public ArrayList<Standing> getStandings() {
        return standings;
    }
    public ArrayList<Team> getTeams() {
        return teams;
    }
    public ArrayList<TeamDetail> getTeamDetails() {
        return teamDetails;
    }
    public ArrayList<Player> getPlayers() {
        return players;
    }
    public int getSuccess() {
        return success;
    }
    public int getSurplus() {
        return surplus;
    }
}