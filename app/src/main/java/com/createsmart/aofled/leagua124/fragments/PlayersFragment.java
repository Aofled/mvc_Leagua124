package com.createsmart.aofled.leagua124.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.createsmart.aofled.leagua124.activity.MainActivity;
import com.createsmart.aofled.leagua124.adapter.PlayersAdapter;
import com.createsmart.aofled.leagua124.api.RestClient;
import com.createsmart.aofled.leagua124.api.RetrofitAPI;
import com.createsmart.aofled.leagua124.dataBaseRealm.RealmWorker;
import com.createsmart.aofled.leagua124.modeles.EntryModel;
import com.createsmart.aofled.leagua124.modeles.Player;
import com.createsmart.aofled.leagua124.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mSwipeRefreshLayout; //свайп для обновления
    private ProgressBar progressBar;  //прогрессбар

    private boolean isDownload = true; // отвечает за загрузку с сервера
    private boolean isLoading;   //отвечает за загрузку RecyclerView
    private int pack =0;
    private boolean dvn_upd;

    private Context cnxt;

    private RecyclerView recyclerView;
    private ArrayList<Player> mPlayerArray = new ArrayList<>();
    private PlayersAdapter playerAdapter;
    private LinearLayoutManager mLayoutManager;

    private RealmWorker realm;
    private RetrofitAPI api;

    private static final String TAG = "PlayersFragment";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity){
            cnxt = getActivity();
        }
    }


    @Override //грузим данные с других форм и тд
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm= new RealmWorker(cnxt,"player");

        pack =((MainActivity)getActivity()).getPlayerScr(); //получаем число для загрузки
        isDownload=((MainActivity)getActivity()).getPlayerDwnl(); //получаем ответ о загрузке

        Bundle bundle = getArguments();
        if (bundle != null) {
            dvn_upd=bundle.getBoolean("dwn_upd"); //грузим/обноваляем
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.f_players_fragment2, container, false);
        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        //инициализация Pull to Refresh (выбираем цвета)
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light,    //holo_purple   //holo_red_light
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_light);

        //прогрессбар
        progressBar = (ProgressBar) view.findViewById(R.id.progressPlayers);
        mLayoutManager = new LinearLayoutManager(cnxt);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        playerAdapter = new PlayersAdapter(cnxt, mPlayerArray);
        recyclerView.setAdapter(playerAdapter);

        progressBar.setVisibility(View.VISIBLE);

        //грузим/обновляем
        if (dvn_upd) {
            dwnPlayers();
        } else {
            loadBD(); //грузим с бд
        }

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
                        downloadPlayerEver();
                    }
                }
            }
        });



        //Обработка нажатия
        playerAdapter.setOnItemClickListener(new PlayersAdapter.ClickListener() {
            @Override
            public void onClick(View v, int position,  boolean click) {
                if (click) {
                    Log.v(TAG,"Быстрое нажатие на ячейку "+ position);
                    //   Intent intent = new Intent(cnxt, PlayerDetalsActivity.class);
                    //   intent.putExtra("position", position);
                    //   startActivity(intent);
                } else {
                    Log.v(TAG,"Долгое нажатие на ячейку "+ position);
                }
            }
        });

        return view;
    }

    //для загрузки данных из интренета
    public void dwnPlayers() {
        api = RestClient.getApi();
        Call<EntryModel> call = api.loadContent("players_get.php", pack);
        call.enqueue(new Callback<EntryModel>() {
            @Override //грузим из интренета
            public void onResponse(Call<EntryModel> call, Response<EntryModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSurplus() < 2) { //если данных осталось 0 и меньше, то больше не грузим
                        isDownload = false;
                        isLoading = false;
                    }
                    mPlayerArray.clear();
                    realm.deleteData(); //если загрузка идет, то чистим БД
                    realm.insertData(response.body().getPlayers());
                    mPlayerArray.addAll(response.body().getPlayers());
                    playerAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    Log.v(TAG,"Данные загружены пачка 0");
                } else {  //грузим, если ошибка при загрузке
                    Log.v(TAG,"Ошибка при загрузке, пачка 0");
                    loadBD();
                    progressBar.setVisibility(View.INVISIBLE);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override  //грузим из базы если нету интрнета
            public void onFailure(Call<EntryModel> call, Throwable t) {
                Log.v(TAG,"Ошибка при загрузке, не работает интрнет, пачка 0");
                Toast.makeText(cnxt, "Проверьте ваше интернет соединение", Toast.LENGTH_LONG).show();
                loadBD(); //грузим из базы
                progressBar.setVisibility(View.INVISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }



    //для ДОгрузки данных из интренета
    public void downloadPlayerEver() {
        if (isDownload) {  //Если есть еще место для загрузки
            pack++; //номер партии для загрузки
            mPlayerArray.add(null);//добовляем прогрессбар
            playerAdapter.notifyItemInserted(mPlayerArray.size() - 1);//добовляем прогрессбар
            api = RestClient.getApi();
            Call<EntryModel> call = api.loadContent("players_get.php", pack);
            call.enqueue(new Callback<EntryModel>() {
                @Override //грузим из интренета
                public void onResponse(Call<EntryModel> call, Response<EntryModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getSurplus() < 2) { //если данных осталось 0 и меньше, то больше не грузим
                            isDownload = false;
                            isLoading = false;
                        }
                        mPlayerArray.remove(mPlayerArray.size() - 1);//"удаляем" прогрессбар
                        realm.insertData(response.body().getPlayers());
                        mPlayerArray.addAll(response.body().getPlayers());
                        playerAdapter.notifyDataSetChanged();
                        Log.v(TAG,"Данные ДОгружены, пачка " + pack);
                        isLoading = true;
                        playerAdapter.notifyDataChanged();
                    } else {  //грузим, если ошибка при загрузке
                        //  playerAdapter.setMoreDataAvailable(false);
                        mPlayerArray.remove(mPlayerArray.size() - 1);//"удаляем" прогрессбар
                        playerAdapter.notifyDataSetChanged();
                        Log.v(TAG,"Ошибка при загрузке, пачка " + pack);
                    }
                }

                @Override  //грузим из базы если нету интрнета
                public void onFailure(Call<EntryModel> call, Throwable t) {
                    mPlayerArray.remove(mPlayerArray.size() - 1);//"удаляем" прогрессбар
                    playerAdapter.notifyDataSetChanged();
                    Log.v(TAG,"Ошибка при загрузке, не работает интрнет, пачка " + pack);
                    Toast.makeText(cnxt, "Проверьте ваше интернет соединение", Toast.LENGTH_LONG).show();
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
        ((MainActivity)getActivity()).setPlayerScr(pack);
        ((MainActivity)getActivity()).setPlayerDwnl(isDownload);
    }




    //для загрузки данных из БД
    public void loadBD(){  //грузим при возвращении на фрагмент
        mPlayerArray.clear();
        mPlayerArray.addAll(realm.getData(new Player(), "id_player", "ASCENDING"));
        mPlayerArray.size();
        playerAdapter.notifyDataSetChanged();
        //сварачиваем прогресс
        progressBar.setVisibility(View.INVISIBLE);
        Log.v(TAG,"Данные загружены с базы данных");
    }


    //действие для Pull to Refresh (обновить данные)
    @Override public void onRefresh() {
        pack =0;  //сбрасываем значения (грузим заново)
        isDownload = true;
        isLoading = true;
        dwnPlayers();
    }










}
