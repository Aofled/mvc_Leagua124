package com.createsmart.aofled.leagua124.api;


import com.createsmart.aofled.leagua124.Constants;
import com.createsmart.aofled.leagua124.modeles.EntryModel;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Afactor on 04.04.2017.
 */




public interface RetrofitAPI {

    @GET(Constants.ROOT_API+"{way}")
    Call<EntryModel> loadContent(@Path("way") String way, @Query("kol") int kol);

    @GET(Constants.ROOT_API+"{way}")
    Call<EntryModel> loadConten_news_detals(@Path("way") String way, @Query("id_news") int id_news);

    @GET(Constants.ROOT_API+"{way}")
    Call<EntryModel> loadConten_match_detals(@Path("way") String way, @Query("id_match") int id_match);

    @GET(Constants.ROOT_API+"{way}")
    Call<EntryModel> loadContent(@Path("way") String way);

    @GET(Constants.ROOT_API+"{way}")
    Call<EntryModel> loadContent_team(@Path("way") String way, @Query("id_teams") int id_teams);

    @GET(Constants.ROOT_API+"{way}")
    Call<EntryModel> loadContent(@Path("way") String way, @Query("kol") int kol, @Query("team") int team);

}