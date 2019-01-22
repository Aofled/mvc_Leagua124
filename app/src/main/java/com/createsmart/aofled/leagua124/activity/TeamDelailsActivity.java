package com.createsmart.aofled.leagua124.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.createsmart.aofled.leagua124.adapter.TeamDetailsAdapter;
import com.createsmart.aofled.leagua124.api.RestClient;
import com.createsmart.aofled.leagua124.api.RetrofitAPI;
import com.createsmart.aofled.leagua124.modeles.EntryModel;
import com.createsmart.aofled.leagua124.modeles.TeamDetail;
import com.createsmart.aofled.leagua124.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TeamDelailsActivity extends AppCompatActivity {


    //прогрессбар
    protected ProgressBar progressBar;
    protected ArrayList<TeamDetail> teamDetails = new ArrayList<>();

    protected TeamDetailsAdapter teamDelailsAdapter;
    protected LinearLayoutManager linearLayoutManager ;
    protected RecyclerView mainRecyclerView;

    private RetrofitAPI api;

    private int id_team;

    private TextView tv_team_name;

    private static final String TAG = "TeamDelailsActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_details2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progressTeamDetals);

        teamDelailsAdapter = new TeamDetailsAdapter(teamDetails,this);
        linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        mainRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewTD);

        mainRecyclerView.setLayoutManager(linearLayoutManager);
        mainRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mainRecyclerView.setAdapter(teamDelailsAdapter);
        progressBar.setVisibility(View.VISIBLE);

        tv_team_name=(TextView)findViewById(R.id.tv_team_name);

        Intent intent = getIntent();
        id_team =intent.getIntExtra("idteam",0); //полуили ли мы токен

        downloadFeed(); //грузим Feed

        //нажатие левый кнопки "выход"
        findViewById(R.id.drawer_button1).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }



    //для загрузки данных из интренета
    public void downloadFeed() {
        api = RestClient.getApi();
        Call<EntryModel> call = api.loadContent_team("teams_details_get.php", id_team);
        call.enqueue(new Callback<EntryModel>() {
            @Override //грузим из интренета
            public void onResponse(Call<EntryModel> call, Response<EntryModel> response) {
                if (response.isSuccessful()) {
                    teamDetails.addAll(response.body().getTeamDetails());
                    teamDelailsAdapter.notifyDataSetChanged();
                    tv_team_name.setText(response.body().getTeamDetails().get(0).getTeamName());
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.v(TAG,"Данные загружены пачка 0");
                } else {  //грузим, если ошибка при загрузке
                    Log.v(TAG,"Ошибка при загрузке, пачка 0");
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override  //грузим из базы если нету интрнета
            public void onFailure(Call<EntryModel> call, Throwable t) {
                Log.v(TAG,"Ошибка при загрузке, не работает интрнет, пачка 0");
                Toast.makeText(TeamDelailsActivity.this, "Проверьте ваше инетрнет соединение", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }









}
