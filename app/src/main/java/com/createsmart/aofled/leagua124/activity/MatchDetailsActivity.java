package com.createsmart.aofled.leagua124.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.createsmart.aofled.leagua124.api.RestClient;
import com.createsmart.aofled.leagua124.api.RetrofitAPI;
import com.createsmart.aofled.leagua124.Constants;
import com.createsmart.aofled.leagua124.DataParser;
import com.createsmart.aofled.leagua124.modeles.EntryModel;
import com.createsmart.aofled.leagua124.R;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchDetailsActivity extends AppCompatActivity  {



    private ProgressBar mProgressBar;

    private ImageView iv_tm_logo1;
    private ImageView iv_tm_logo2;
    private TextView tv_tm_name1;
    private TextView tv_tm_name2;
    private TextView tv_score1;
    private TextView tv_score2;
    private TextView tv_player_goals_scr1;
    private TextView tv_player_goals_scr2;
    private TextView tv_data_matches;
    private TextView tv_title;

    private RetrofitAPI api;

    private int id_match;


    private static final String TAG = "MatchDetailsActivity";




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_details2);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //нет названия у тулбара


        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        tv_title = (TextView)findViewById(R.id.tv_title);

        iv_tm_logo1 =(ImageView)findViewById(R.id.iv_tm_logo1);
        iv_tm_logo2 =(ImageView)findViewById(R.id.iv_tm_logo2);
        tv_tm_name1 =(TextView)findViewById(R.id.tv_tm_name1);
        tv_tm_name2 =(TextView)findViewById(R.id.tv_tm_name2);
        tv_score1 =(TextView)findViewById(R.id.tv_score1);
        tv_score2 =(TextView)findViewById(R.id.tv_score2);
        tv_player_goals_scr1 =(TextView)findViewById(R.id.tv_player_goals_scr1);
        tv_player_goals_scr2 =(TextView)findViewById(R.id.tv_player_goals_scr2);
        tv_data_matches =(TextView)findViewById(R.id.tv_data_matches);


        //нажатие левый кнопки "выход"
        findViewById(R.id.drawer_button1).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();

        if (intent!=null) {
            id_match = intent.getIntExtra("id_match", -1);
            dvnMatch();
        }
    }

    //для загрузки данных из интренета
    protected void dvnMatch () {
        api = RestClient.getApi();
        Call<EntryModel> call = api.loadConten_match_detals(Constants.URL_MATCH_DETAILS, id_match);
        call.enqueue(new Callback<EntryModel>() {
            @Override //грузим из интренета
            public void onResponse(Call<EntryModel> call, Response<EntryModel> response) {
                if (response.isSuccessful()) {
                    Picasso.get()
                            .load(response.body().getMatches().get(0).getTeamImg1())
                            .into(iv_tm_logo1);
                    Picasso.get()
                            .load(response.body().getMatches().get(0).getTeamImg2())
                            .into(iv_tm_logo2);

                    tv_tm_name1.setText(response.body().getMatches().get(0).getTeamName1());
                    tv_tm_name2.setText(response.body().getMatches().get(0).getTeamName2());
                    tv_score1.setText(""+response.body().getMatches().get(0).getScoreT1());
                    tv_score2.setText(""+response.body().getMatches().get(0).getScoreT2());
                    tv_player_goals_scr1.setText(response.body().getMatches().get(0).getGoalsScPl1());
                    tv_player_goals_scr2.setText(response.body().getMatches().get(0).getGoalsScPl1());
                    tv_data_matches.setText(DataParser.newsDate(response.body().getMatches().get(0).getTimeGame()));


                    tv_title.setText(response.body().getMatches().get(0).getTeamName1()+" - "+response.body().getMatches().get(0).getTeamName2());
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Log.v(TAG,getString(R.string.data_loading_0));
                } else {  //грузим, если ошибка при загрузке
                    Log.v(TAG,getString(R.string.data_fail_0));
                }
            }
            @Override  //грузим из базы если нету интрнета
            public void onFailure(Call<EntryModel> call, Throwable t) {
                Log.v(TAG,getString(R.string.connection_fail_0));
                Toast.makeText(MatchDetailsActivity.this, R.string.connect_fail, Toast.LENGTH_LONG).show();
            }
        });
    }


}
