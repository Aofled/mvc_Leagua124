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
import com.createsmart.aofled.leagua124.dataBaseRealm.RealmWorker;
import com.createsmart.aofled.leagua124.modeles.EntryModel;
import com.createsmart.aofled.leagua124.modeles.News;
import com.createsmart.aofled.leagua124.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsDetalsActivity extends AppCompatActivity {


    private ProgressBar mProgressBar;

    private RealmWorker realm;
    private ImageView iv_image;
    private TextView body, tv_title, tv_description;

    private int b=0;
    private int position;
    private int id_news;


    private RetrofitAPI api;


    private static final String TAG = "NewsDetalsActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details2);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //нет названия у тулбара

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        iv_image =(ImageView)findViewById(R.id.iv_image);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_description = (TextView)findViewById(R.id.tv_description);
        body = (TextView)findViewById(R.id.body);

        //нажатие левый кнопки "выход"
        findViewById(R.id.drawer_button1).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        //нажатие размера шрифта
        findViewById(R.id.drawer_button2).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (b==0) {
                    body.setTextSize(18);
                    tv_description.setTextSize(18);
                    b=1;
                    return;
                }
                if (b==1) {
                    body.setTextSize(20);
                    tv_description.setTextSize(20);
                    b=2;
                    return;
                }
                if (b==2) {
                    body.setTextSize(16);
                    tv_description.setTextSize(16);
                    b=0;
                    return;
                }
            }
        });

        realm= new RealmWorker(this,"news");

        Intent intent = getIntent();


        if (intent!=null) {
            position = intent.getIntExtra("position", -1);
            id_news = intent.getIntExtra("id_news", -1);
            if (position>=0) {
                getDB();
            } else if (id_news>=0){
                dvnNews();
            }
        }



    }


    protected void getDB () {
        ArrayList<News> news = realm.getData(new News(), "id_news", "DESCENDING");
        tv_title.setText(news.get(position).getTitle());
        tv_description.setText(news.get(position).getDescription());
        body.setText(news.get(position).getBody());
        Picasso.get()
                .load(news.get(position).getImage())
                .into(iv_image);
        mProgressBar.setVisibility(View.INVISIBLE);
    }




    //для загрузки данных из интренета
    protected void dvnNews () {
        api = RestClient.getApi();
        Call<EntryModel> call = api.loadConten_news_detals(Constants.URL_NEWS_DETAILS, id_news);
        call.enqueue(new Callback<EntryModel>() {
            @Override //грузим из интренета
            public void onResponse(Call<EntryModel> call, Response<EntryModel> response) {
                if (response.isSuccessful()) {
                    tv_title.setText(response.body().getNews().get(0).getTitle());
                    tv_description.setText(response.body().getNews().get(0).getDescription());
                    body.setText(response.body().getNews().get(0).getBody());
                    Picasso.get()
                            .load(response.body().getNews().get(0).getImage())
                            .into(iv_image);
                    Log.v(TAG,getString(R.string.data_loading_0));
                    mProgressBar.setVisibility(View.INVISIBLE);
                } else {  //грузим, если ошибка при загрузке
                    Log.v(TAG,getString(R.string.data_fail_0));
                }
            }
            @Override  //грузим из базы если нету интрнета
            public void onFailure(Call<EntryModel> call, Throwable t) {
                Log.v(TAG,getString(R.string.connection_fail_0));
                Toast.makeText(NewsDetalsActivity.this, R.string.connect_fail, Toast.LENGTH_LONG).show();
            }
        });
    }




}
