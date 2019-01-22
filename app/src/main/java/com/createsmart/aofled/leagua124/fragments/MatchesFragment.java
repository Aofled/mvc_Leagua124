package com.createsmart.aofled.leagua124.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.createsmart.aofled.leagua124.activity.TeamDelailsActivity;
import com.createsmart.aofled.leagua124.adapter.MatchesAdapter;
import com.createsmart.aofled.leagua124.api.RestClient;
import com.createsmart.aofled.leagua124.api.RetrofitAPI;
import com.createsmart.aofled.leagua124.dataBaseRealm.RealmWorker;
import com.createsmart.aofled.leagua124.activity.MainActivity;
import com.createsmart.aofled.leagua124.modeles.EntryModel;
import com.createsmart.aofled.leagua124.modeles.Match;
import com.createsmart.aofled.leagua124.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Afactor on 26.09.2017.
 */

public class MatchesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mSwipeRefreshLayout; //свайп для обновления
    private ProgressBar progressBar;  //прогрессбар

    private boolean isDownload = true; // отвечает за загрузку с сервера
    private boolean isLoading;   //отвечает за загрузку RecyclerView
    private int pack =0;
    private boolean dvn_upd;

    private Context cnxt;

    private RecyclerView recyclerView;
    private ArrayList<Match> mMatchArray = new ArrayList<>();
    private MatchesAdapter matchesAdapter;
    private LinearLayoutManager mLayoutManager;

    private RealmWorker realm;
    private RetrofitAPI api;

    private static final String TAG = "MatchesFragment";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity){
            cnxt = getActivity().getApplicationContext();
        }
    }



    @Override
    public void onResume() {
        super.onResume();

    }



    @Override //полкчаем данные
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm= new RealmWorker(cnxt,"matches");
        pack =((MainActivity)getActivity()).getMatchScr(); //получаем число для загрузки
        isDownload=((MainActivity)getActivity()).getMatchDwn(); //получаем ответ о загрузке
        //получаем данные с нашей активити
        Bundle bundle = getArguments();
        if (bundle != null) {
            dvn_upd=bundle.getBoolean("dwn_upd"); //грузим/обноваляем
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.f_matches_fragment2, container, false);
        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        //инициализация Pull to Refresh (выбираем цвета)
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light,    //holo_purple   //holo_red_light
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_light);
        //прогрессбар
        progressBar = (ProgressBar) view.findViewById(R.id.progressMatches);

        mLayoutManager = new LinearLayoutManager(cnxt);

        pack =((MainActivity)getActivity()).getMatchScr(); //получаем число для загрузки
        isDownload=((MainActivity)getActivity()).getMatchDwn(); //получаем ответ о загрузке



        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        matchesAdapter = new MatchesAdapter(cnxt, mMatchArray);







        recyclerView.setAdapter(matchesAdapter);
        progressBar.setVisibility(View.VISIBLE);


        //грузим/обновляем
        if (dvn_upd) {
            dwnMatches();
        } else {
            loadHome(); //грузим с бд
        }

        /*
        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
        */

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
                        downloadNewsEver();
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


            //обработка нажатия на логотип команды
            @Override
            public void tImageOnClick(View v, int position, boolean click) {
                Log.v(TAG,"нажатие на логотип команды= "+ position);
                Intent intent = new Intent(getContext(), TeamDelailsActivity.class);
                if (click) {
                    intent.putExtra("idteam", mMatchArray.get(position).getIdTeam1());
                } else {
                    intent.putExtra("idteam", mMatchArray.get(position).getIdTeam2());
                }
                startActivity(intent);
            }
        });


        return view;
    }










    //для загрузки данных из интренета
    public void dwnMatches() {
        api = RestClient.getApi();
        Call<EntryModel> call = api.loadContent("matches_get.php", pack);
        call.enqueue(new Callback<EntryModel>() {
            @Override //грузим из интренета
            public void onResponse(Call<EntryModel> call, Response<EntryModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSurplus() < 2) { //если данных осталось 0 и меньше, то больше не грузим
                        isDownload = false;
                        isLoading = false;
                    }
                    mMatchArray.clear();
                    realm.deleteData(); //если загрузка идет, то чистим БД
                    realm.insertData(response.body().getMatches());
                    mMatchArray.addAll(response.body().getMatches());
                    matchesAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    Log.v(TAG,"Данные загружены пачка 0");
                } else {  //грузим, если ошибка при загрузке
                    Log.v(TAG,"Ошибка при загрузке, пачка 0");
                    loadHome();
                    progressBar.setVisibility(View.INVISIBLE);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override  //грузим из базы если нету интрнета
            public void onFailure(Call<EntryModel> call, Throwable t) {
                Log.v(TAG,"Ошибка при загрузке, не работает интрнет, пачка 0");
                Toast.makeText(cnxt, "Проверьте ваше инетрнет соединение", Toast.LENGTH_LONG).show();
                loadHome(); //грузим из базы
                progressBar.setVisibility(View.INVISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }



    //для ДОгрузки данных из интренета
    public void downloadNewsEver() {
        if (isDownload) {  //Если есть еще место для загрузки
            pack++; //номер партии для загрузки
            mMatchArray.add(null);//добовляем прогрессбар
            matchesAdapter.notifyItemInserted(mMatchArray.size() - 1);//добовляем прогрессбар
            api = RestClient.getApi();
            Call<EntryModel> call = api.loadContent("matches_get.php", pack);
            call.enqueue(new Callback<EntryModel>() {
                @Override //грузим из интренета
                public void onResponse(Call<EntryModel> call, Response<EntryModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getSurplus() < 2) { //если данных осталось 0 и меньше, то больше не грузим
                            isDownload = false;
                            isLoading = false;
                        }
                        mMatchArray.remove(mMatchArray.size() - 1);//"удаляем" прогрессбар
                        realm.insertData(response.body().getMatches());
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
                    Toast.makeText(cnxt, "Проверьте ваше инетрнет соединение", Toast.LENGTH_LONG).show();
                }
            });
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            Log.v(TAG,"Конец списка последняя пачка " + pack);
            Toast.makeText(cnxt, "Конец списка", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity)getActivity()).setMatch(pack);
        ((MainActivity)getActivity()).setMatchDwn(isDownload);
    }



    //для загрузки данных из БД
    public void loadHome(){  //грузим при возвращении на фрагмент
        mMatchArray.clear();
        mMatchArray.addAll(realm.getData(new Match(), "id_matches", "DESCENDING" ));
        mMatchArray.size();
        matchesAdapter.notifyDataSetChanged();
        //сварачиваем прогресс
        progressBar.setVisibility(View.INVISIBLE);
        Log.v(TAG,"Данные загружены с базы данных");
    }





    //действие для Pull to Refresh (обновить данные)
    @Override public void onRefresh() {
        pack =0;  //сбрасываем значения (грузим заново)
        isDownload = true;
        isLoading = true;
        dwnMatches();
    }










}
