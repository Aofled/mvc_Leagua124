package com.createsmart.aofled.leagua124.activity;

import android.os.Bundle;
import android.os.SystemClock;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.createsmart.aofled.leagua124.fragments.InfoFragment;
import com.createsmart.aofled.leagua124.fragments.PlayersFragment;
import com.createsmart.aofled.leagua124.fragments.StandingsFragment;
import com.createsmart.aofled.leagua124.fragments.TeamFragment;
import com.createsmart.aofled.leagua124.R;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int fr =0; //номер фрагмента
    private long last_time; //время для выхода (нажмите два раза)
    private boolean upd_news =true, upd_mtch=true, upd_stnd=true, upd_team=true, upd_pl=true;
    private int news_bnd =0, mtch_bnd =0, pl_bnd =0; //сколько мы загрузили "пачек"
    private boolean down_news =true, down_mtch =true, down_pl =true; //догрузка разрешена в список


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        setFragment(fr); //запускаем фрагмент менеджер
    }



    //инициализируем компоненты
    private void initComponents() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_news) {
            setFragment(0);
        } else if (id == R.id.nav_standings) {
            setFragment(1);
        } else if (id == R.id.nav_teams) {
            setFragment(2);
        }  else if (id == R.id.nav_players) {
            setFragment(3);
        } else if (id == R.id.nav_exit) { //выход
            super.finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //выбираем куда перейти (какой фрагмент запустить)
    public void setFragment(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (position) {

            case 0://переход на новости/*
                InfoFragment infoFragment = new InfoFragment();
                Bundle bundle_i = new Bundle();
                bundle_i.putBoolean("upd_news", upd_news); //обновляем/грузим?
                bundle_i.putBoolean("upd_mtch", upd_mtch); //обновляем/грузим?
                infoFragment.setArguments(bundle_i);
                fragmentTransaction.replace(R.id.fragment, infoFragment);
                upd_news=false;
                upd_mtch=false;
                fr =1;
                break;
            case 1: //переход на страницe турнирной таблицы
                StandingsFragment standingsFragment = new StandingsFragment();
                Bundle bundleS = new Bundle();
                bundleS.putBoolean("dwn_upd", upd_stnd); //обновляем/грузим?
                standingsFragment.setArguments(bundleS);
                fragmentTransaction.replace(R.id.fragment, standingsFragment);
                upd_stnd=false;
                fr =0;
                break;
            case 2: //переход на команды
                TeamFragment teamFragment = new TeamFragment();
                Bundle bundle_L = new Bundle();
                bundle_L.putBoolean("dwn_upd", upd_team); //обновляем/грузим?
                teamFragment.setArguments(bundle_L);
                fragmentTransaction.replace(R.id.fragment, teamFragment);
                upd_team=false;
                fr =2;
                break;
            case 3: //переход на игроков/*
                PlayersFragment playersFragment = new PlayersFragment();
                Bundle bundle_p = new Bundle();
                bundle_p.putBoolean("dwn_upd", upd_pl); //обновляем/грузим?
                playersFragment.setArguments(bundle_p);
                fragmentTransaction.replace(R.id.fragment, playersFragment);
                upd_pl=false;
                fr =3;
                break;
            default:
                break;
        }
        fragmentTransaction.commit();
    }


    //при нажатии кнопки назад - закрываем боковую панель (обработка кнопки "назад")
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        //смотрим, сколько времени прошло с последнего нажатия "назад"
        if ((SystemClock.elapsedRealtime() - last_time) < 1500) {
            finish();
            return;
        }
        if ((SystemClock.elapsedRealtime() - last_time) > 1500) {
            last_time = SystemClock.elapsedRealtime();
            Toast.makeText(getApplicationContext(),
                    R.string.exit, Toast.LENGTH_LONG).show();
        }
        else {
            super.onBackPressed();
        }
    }


    /*news фрагмент*/
    public boolean getNewsDwn() {
        return down_news;    }
    public void setNewsDwn(boolean download) {
        down_news = download;    }

    public int getNewsScr()  {
        return this.news_bnd;    }
    public void setNewsScr(int setscrollnews){
        this.news_bnd =setscrollnews;    }


    /*news фрагмент*/
    public boolean getMatchDwn() {
        return down_mtch;    }
    public void setMatchDwn(boolean download) {
        down_mtch = download;    }

    public int getMatchScr()  {
        return this.mtch_bnd;    }
    public void setMatch(int setscrollmatch){
        this.mtch_bnd =setscrollmatch;    }


    /*player фрагмент*/
    public boolean getPlayerDwnl() {
        return down_pl;    }
    public void setPlayerDwnl(boolean download) {
        down_pl = download;    }

    public int getPlayerScr()  {
        return this.pl_bnd;    }
    public void setPlayerScr(int setscrollpl){
        this.pl_bnd =setscrollpl;    }



}
