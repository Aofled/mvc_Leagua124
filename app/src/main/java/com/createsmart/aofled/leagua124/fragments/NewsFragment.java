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


import com.createsmart.aofled.leagua124.activity.NewsDetalsActivity;
import com.createsmart.aofled.leagua124.adapter.NewsAdapter;
import com.createsmart.aofled.leagua124.api.RestClient;
import com.createsmart.aofled.leagua124.api.RetrofitAPI;
import com.createsmart.aofled.leagua124.Constants;
import com.createsmart.aofled.leagua124.dataBaseRealm.RealmWorker;
import com.createsmart.aofled.leagua124.activity.MainActivity;
import com.createsmart.aofled.leagua124.modeles.EntryModel;
import com.createsmart.aofled.leagua124.modeles.News;
import com.createsmart.aofled.leagua124.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class NewsFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mSwipeRefreshLayout; //свайп для обновления
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;

    private boolean is_download = true; // отвечает за загрузку с сервера
    private boolean is_loading;   //отвечает за загрузку RecyclerView
    private int bundle_dwn =0;
    private boolean dvn_upd;

    private ArrayList<News> mNewsArray = new ArrayList<>();
    private NewsAdapter newsAdapter;
    private LinearLayoutManager mLayoutManager;

    private RealmWorker realm;
    private RetrofitAPI api;

    private static final String TAG = "NewsFragment";
    private static final String FRG = "news";

    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity){
            this.context = getActivity();
        }
    }


    @Override //грузим данные с других форм и тд
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm= new RealmWorker(context,FRG);
        bundle_dwn =((MainActivity)getActivity()).getNewsScr(); //получаем число для загрузки
        is_download =((MainActivity)getActivity()).getNewsDwn(); //получаем ответ о загрузке
        Bundle bundle = getArguments();
        if (bundle != null) {
            dvn_upd=bundle.getBoolean("dwn_upd"); //грузим/обноваляем
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.f_news_fragment, container, false);
        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

       //инициализация Pull to Refresh (выбираем цвета)
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_light);

        //прогрессбар
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mLayoutManager = new LinearLayoutManager(context);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        newsAdapter = new NewsAdapter(context, mNewsArray);
        mRecyclerView.setAdapter(newsAdapter);

        mProgressBar.setVisibility(View.VISIBLE);

        //грузим/обновляем
        if (dvn_upd) {
            dwnNews();
        } else {
            loadDataBase(); //грузим с бд
        }

        //Динамическое пролистывание в начало
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.scrollToPosition(0);
            }
        });

        is_loading = true;
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = mLayoutManager.getChildCount();//смотрим сколько элементов на экране
                int totalItemCount = mLayoutManager.getItemCount();//сколько всего элементов
                int firstVisibleItems = mLayoutManager.findFirstVisibleItemPosition();//какая позиция первого элемента
                if (is_loading) {
                    if ((visibleItemCount + firstVisibleItems) >= totalItemCount) {
                        is_loading = false;//ставим флаг что мы попросили еще элементы . После загрузки данных его надо будет сбросить снова в False;
                        downloadNewsEver();
                    }
                }
            }
        });

        //Обработка нажатия
        newsAdapter.setOnItemClickListener(new NewsAdapter.ClickListener() {
            @Override
            public void onClick(View v, int position,  boolean click) {
                if (click) {
                    Log.v(TAG,getString(R.string.fast_click)+ position);
                    Intent intent = new Intent(context, NewsDetalsActivity.class);
                    intent.putExtra("position", position);
                    startActivity(intent);
                } else {
                    Log.v(TAG,getString(R.string.long_click)+ position);
                }
            }
        });

        return view;
    }


    //для загрузки данных из интренета
    public void dwnNews() {
        api = RestClient.getApi();
        Call<EntryModel> call = api.loadContent(Constants.URL_NEWS, bundle_dwn);
        call.enqueue(new Callback<EntryModel>() {
            @Override //грузим из интренета
            public void onResponse(Call<EntryModel> call, Response<EntryModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSurplus() < 1) { //если данных осталось 0 и меньше, то больше не грузим
                        is_download = false;
                        is_loading = false;
                    }
                    mNewsArray.clear();
                    realm.deleteData(); //если загрузка идет, то чистим БД
                    realm.insertData(response.body().getNews());
                    mNewsArray.addAll(response.body().getNews());
                    newsAdapter.notifyDataSetChanged();
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    Log.v(TAG,getString(R.string.data_loading_0));
                } else {  //грузим, если ошибка при загрузке
                    Log.v(TAG,getString(R.string.data_fail_0));
                    loadDataBase();
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
            @Override  //грузим из базы если нету интрнета
            public void onFailure(Call<EntryModel> call, Throwable t) {
                Log.v(TAG,getString(R.string.connection_fail_0));
                Toast.makeText(context, R.string.connect_fail, Toast.LENGTH_LONG).show();
                loadDataBase(); //грузим из базы
                mProgressBar.setVisibility(View.INVISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    //для ДОгрузки данных из интренета
    public void downloadNewsEver() {
        if (is_download) {  //Если есть еще место для загрузки
            bundle_dwn++; //номер партии для загрузки
            mNewsArray.add(null);//добовляем прогрессбар
            newsAdapter.notifyItemInserted(mNewsArray.size() - 1);//добовляем прогрессбар
            api = RestClient.getApi();
            Call<EntryModel> call = api.loadContent(Constants.URL_NEWS, bundle_dwn);
            call.enqueue(new Callback<EntryModel>() {
                @Override //грузим из интренета
                public void onResponse(Call<EntryModel> call, Response<EntryModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getSurplus() < 2) { //если данных осталось 0 и меньше, то больше не грузим
                            is_download = false;
                            is_loading = false;
                        }
                        mNewsArray.remove(mNewsArray.size() - 1);//"удаляем" прогрессбар
                        realm.insertData(response.body().getNews());
                        mNewsArray.addAll(response.body().getNews());
                        newsAdapter.notifyDataSetChanged();
                        Log.v(TAG,getString(R.string.data_loading_next) + bundle_dwn);
                        is_loading = true;
                        newsAdapter.notifyDataChanged();
                    } else {  //грузим, если ошибка при загрузке
                        //  newsAdapter.setMoreDataAvailable(false);
                        mNewsArray.remove(mNewsArray.size() - 1);//"удаляем" прогрессбар
                        newsAdapter.notifyDataSetChanged();
                        Log.v(TAG,getString(R.string.data_fail_next) + bundle_dwn);
                    }
                }

                @Override  //грузим из базы если нету интрнета
                public void onFailure(Call<EntryModel> call, Throwable t) {
                    mNewsArray.remove(mNewsArray.size() - 1);//"удаляем" прогрессбар
                    newsAdapter.notifyDataSetChanged();
                    Log.v(TAG,getString(R.string.connection_fail_next) + bundle_dwn);
                    Toast.makeText(context, R.string.connect_fail, Toast.LENGTH_LONG).show();
                }
            });
            mProgressBar.setVisibility(View.INVISIBLE);
        } else {
            Log.v(TAG,getString(R.string.end_of_list_next) + bundle_dwn);
            Toast.makeText(context, R.string.end_of_list, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity)getActivity()).setNewsScr(bundle_dwn);
        ((MainActivity)getActivity()).setNewsDwn(is_download);
    }


    //для загрузки данных из БД
    public void loadDataBase(){  //грузим при возвращении на фрагмент
        mNewsArray.clear();
        mNewsArray.addAll(realm.getData(new News(), "id_news", "DESCENDING"));
        mNewsArray.size();
        newsAdapter.notifyDataSetChanged();
        mProgressBar.setVisibility(View.INVISIBLE);
        Log.v(TAG,getString(R.string.data_loading_DB));
    }


    //действие для Pull to Refresh (обновить данные)
    @Override public void onRefresh() {
        bundle_dwn =0;  //сбрасываем значения (грузим заново)
        is_download = true;
        is_loading = true;
        dwnNews();
    }



}
