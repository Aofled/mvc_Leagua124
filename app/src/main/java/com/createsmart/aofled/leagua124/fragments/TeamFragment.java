package com.createsmart.aofled.leagua124.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.createsmart.aofled.leagua124.activity.TeamDelailsActivity;
import com.createsmart.aofled.leagua124.adapter.TeamsAdapter;
import com.createsmart.aofled.leagua124.api.RestClient;
import com.createsmart.aofled.leagua124.api.RetrofitAPI;
import com.createsmart.aofled.leagua124.dataBaseRealm.RealmWorker;
import com.createsmart.aofled.leagua124.modeles.EntryModel;
import com.createsmart.aofled.leagua124.modeles.Team;
import com.createsmart.aofled.leagua124.R;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Afactor on 26.09.2017.
 */

public class TeamFragment extends Fragment {


    private ProgressBar progressBar;  //прогрессбар
    private Context context;
    private RecyclerView recyclerView;

    private GridLayoutManager gridLayoutManager;

    private ArrayList<Team> mTeamArray = new ArrayList<>();
    private TeamsAdapter teamsAdapter;
    private LinearLayoutManager mLayoutManager;

    private RealmWorker realm;
    private RetrofitAPI api;

    private boolean dvn_upd;

    private static final String TAG = "TeamFragment";


    @Override
    public void onResume() {
        super.onResume();
        //инициализируем базу данных
        realm= new RealmWorker(context,"teams");
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            dvn_upd=bundle.getBoolean("dwn_upd"); //грузим/обноваляем
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.f_team_fragment2, container, false);
        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        progressBar = (ProgressBar) view.findViewById(R.id.progressTeamsBar);
        context = getActivity().getApplicationContext();
        mLayoutManager = new LinearLayoutManager(context);

        onResume();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerTeamsView);

        gridLayoutManager = new GridLayoutManager(context, 2);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        teamsAdapter = new TeamsAdapter(context, mTeamArray);
        recyclerView.setAdapter(teamsAdapter);

        progressBar.setVisibility(View.VISIBLE);


        //грузим/обновляем
        if (dvn_upd) {
            dwnTeams();
        } else {
            loadTeams(); //грузим с бд
        }


        //Динамическое пролистывание в начало
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(0);
            }
        });



        teamsAdapter.setOnItemClickListener(new TeamsAdapter.ClickListener() {
            @Override
            public void onClick(View v, int position, boolean click) {
                if (click) {
                    Log.v(TAG,"Быстрое нажатие на ячейку "+ position);
                    Intent intent = new Intent(context, TeamDelailsActivity.class);
                    intent.putExtra("idteam", mTeamArray.get(position).getIdTeams());
                    startActivity(intent);
                } else {
                    Log.v(TAG,"Долгое нажатие на ячейку "+ position);
                }
            }
        });


        return view;
    }





    //для загрузки данных из интренета
    public void dwnTeams() {
        api = RestClient.getApi();
        Call<EntryModel> call = api.loadContent("teams.php");
        call.enqueue(new Callback<EntryModel>() {
            @Override //грузим из интренета
            public void onResponse(Call<EntryModel> call, Response<EntryModel> response) {
                if (response.isSuccessful()) {
                    mTeamArray.clear();
                    realm.deleteData(); //если загрузка идет, то чистим БД


                    realm.insertData(response.body().getTeams());

                 //   realm.insertTeams(response.body().getTeams());
                    mTeamArray.addAll(response.body().getTeams());
                    teamsAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.v(TAG,"Данные загружены пачка 0");
                } else {  //грузим, если ошибка при загрузке
                    Log.v(TAG,"Ошибка при загрузке, пачка 0");
                    loadTeams();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override  //грузим из базы если нету интрнета
            public void onFailure(Call<EntryModel> call, Throwable t) {
                Log.v(TAG,"Ошибка при загрузке, не работает интрнет, пачка 0");
                Toast.makeText(context, "Проверьте ваше интернет соединение", Toast.LENGTH_LONG).show();
                mTeamArray.clear(); //чистим старые данные
                loadTeams(); //грузим из базы
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }


    //для загрузки данных из БД
    public void loadTeams(){  //грузим при возвращении на фрагмент
        mTeamArray.clear();
        mTeamArray.addAll(realm.getData(new Team(), "team_name", "ASCENDING"));
        mTeamArray.size();
        teamsAdapter.notifyDataSetChanged();
        //сварачиваем прогресс
        progressBar.setVisibility(View.INVISIBLE);
        Log.v(TAG,"Данные загружены с базы данных");
    }
















}
