package com.createsmart.aofled.leagua124.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.createsmart.aofled.leagua124.adapter.MatchesAdapter;
import com.createsmart.aofled.leagua124.api.RestClient;
import com.createsmart.aofled.leagua124.api.RetrofitAPI;
import com.createsmart.aofled.leagua124.modeles.EntryModel;
import com.createsmart.aofled.leagua124.modeles.Match;
import com.createsmart.aofled.leagua124.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamMatchesActivity extends AppCompatActivity {


    private ProgressBar progressBar;  //прогрессбар

    private boolean isDownload = true; // отвечает за загрузку с сервера
    private boolean isLoading;   //отвечает за загрузку RecyclerView
    private int pack =0;
    private boolean dvn_upd;



    private int id_team;


    private TextView tv_team_name;

    private RecyclerView recyclerView;
    private ArrayList<Match> mMatchArray = new ArrayList<>();
    private MatchesAdapter matchesAdapter;
    private LinearLayoutManager mLayoutManager;

    private RetrofitAPI api;

    private static final String TAG = "TeamMatchesActivity";




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_match);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progress);


        progressBar.setVisibility(View.VISIBLE);

        tv_team_name=(TextView)findViewById(R.id.tv_team_name);

        Intent intent = getIntent();
        id_team =intent.getIntExtra("idteam",0);

        dwnMatches(); //грузим Feed



        //прогрессбар
        progressBar = (ProgressBar) findViewById(R.id.progress);

        mLayoutManager = new LinearLayoutManager(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewTM);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        matchesAdapter = new MatchesAdapter(this, mMatchArray);


        recyclerView.setAdapter(matchesAdapter);
        progressBar.setVisibility(View.VISIBLE);


        //Динамическое пролистывание в начало
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(0);
            }
        });

        isLoading = true;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = mLayoutManager.getChildCount();//смотрим сколько элементов на экране
                int totalItemCount = mLayoutManager.getItemCount();//сколько всего элементов
                int firstVisibleItems = mLayoutManager.findFirstVisibleItemPosition();//какая позиция первого элемента
                if (isLoading) {
                    if ((visibleItemCount + firstVisibleItems) >= totalItemCount) {
                        isLoading = false;//ставим флаг что мы попросили еще элементы . После загрузки данных его надо будет сбросить снова в False;
                        downloadMatchesEver();
                    }
                }
            }
        });



        //Обработка нажатия на ячейку
        matchesAdapter.setOnItemClickListener(new MatchesAdapter.ClickListener() {
            @Override
            public void onClick(View v, int position,  boolean click) {
                if (click) {
                    Log.v(TAG,"Быстрое нажатие на ячейку "+ position);
                    //  Intent intent = new Intent(cnxt, NewsDetalsActivity.class);
                    //  intent.putExtra("position", position);
                    //  startActivity(intent);
                } else {
                    Log.v(TAG,"Долгое нажатие на ячейку "+ position);
                }
            }
        //нажатие на логотип команды
            @Override
            public void tImageOnClick(View v, int position, boolean click) {
                Log.v(TAG,"нажатие на логотип команды= "+ position);
                Intent intent = new Intent(TeamMatchesActivity.this, TeamDelailsActivity.class);
                if (click) {
                    intent.putExtra("idteam", mMatchArray.get(position).getIdTeam1());
                } else {
                    intent.putExtra("idteam", mMatchArray.get(position).getIdTeam2());
                }
                startActivity(intent);
            }
        });



        //нажатие левый кнопки "выход"
        findViewById(R.id.drawer_button1).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }





    //для загрузки данных из интренета
    public void dwnMatches() {
        api = RestClient.getApi();
        Call<EntryModel> call = api.loadContent("team_matches_get.php", pack, id_team);
        call.enqueue(new Callback<EntryModel>() {
            @Override //грузим из интренета
            public void onResponse(Call<EntryModel> call, Response<EntryModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSurplus() < 2) { //если данных осталось 0 и меньше, то больше не грузим
                        isDownload = false;
                        isLoading = false;
                    }
                    mMatchArray.clear();
                    mMatchArray.addAll(response.body().getMatches());
                    tv_team_name.setText("Все игры "+response.body().getMatches().get(1).getTeamName1());
                    matchesAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);
//                    mSwipeRefreshLayout.setRefreshing(false);
                    Log.v(TAG,"Данные загружены пачка 0");
                } else {  //грузим, если ошибка при загрузке
                    Log.v(TAG,"Ошибка при загрузке, пачка 0");
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFailure(Call<EntryModel> call, Throwable t) {
                Log.v(TAG,"Ошибка при загрузке, не работает интрнет, пачка 0");
                Toast.makeText(TeamMatchesActivity.this, "Проверьте ваше инетрнет соединение", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }



    //для ДОгрузки данных из интренета
    public void downloadMatchesEver() {
        if (isDownload) {  //Если есть еще место для загрузки
            pack++; //номер партии для загрузки
            mMatchArray.add(null);//добовляем прогрессбар
            matchesAdapter.notifyItemInserted(mMatchArray.size() - 1);//добовляем прогрессбар
            api = RestClient.getApi();
            Call<EntryModel> call = api.loadContent("team_matches_get.php", pack, id_team);
            call.enqueue(new Callback<EntryModel>() {
                @Override //грузим из интренета
                public void onResponse(Call<EntryModel> call, Response<EntryModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getSurplus() < 2) { //если данных осталось 0 и меньше, то больше не грузим
                            isDownload = false;
                            isLoading = false;
                        }
                        mMatchArray.remove(mMatchArray.size() - 1);//"удаляем" прогрессбар
                        mMatchArray.addAll(response.body().getMatches());
                        matchesAdapter.notifyDataSetChanged();
                        Log.v(TAG,"Данные ДОгружены, пачка " + pack);
                        isLoading = true;
                        matchesAdapter.notifyDataChanged();
                    } else {  //грузим, если ошибка при загрузке
                        //  newsAdapter.setMoreDataAvailable(false);
                        mMatchArray.remove(mMatchArray.size() - 1);//"удаляем" прогрессбар
                        matchesAdapter.notifyDataSetChanged();
                        Log.v(TAG,"Ошибка при загрузке, пачка " + pack);
                    }
                }

                @Override  //грузим из базы если нету интрнета
                public void onFailure(Call<EntryModel> call, Throwable t) {
                    mMatchArray.remove(mMatchArray.size() - 1);//"удаляем" прогрессбар
                    matchesAdapter.notifyDataSetChanged();
                    Log.v(TAG,"Ошибка при загрузке, не работает интрнет, пачка " + pack);
                    Toast.makeText(TeamMatchesActivity.this, "Проверьте ваше инетрнет соединение", Toast.LENGTH_LONG).show();
                }
            });
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            Log.v(TAG,"Конец списка последняя пачка " + pack);
            Toast.makeText(TeamMatchesActivity.this, "Конец списка", Toast.LENGTH_SHORT).show();
        }
    }







}
