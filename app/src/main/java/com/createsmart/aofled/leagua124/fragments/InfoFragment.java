package com.createsmart.aofled.leagua124.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.createsmart.aofled.leagua124.adapter.InfoAdapter;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.createsmart.aofled.leagua124.R;

public class InfoFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private InfoAdapter adapter;

    private boolean upd_news = true;
    private boolean upd_mtch = true;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            upd_news = bundle.getBoolean("upd_news"); //грузим/обноваляем
            upd_mtch = bundle.getBoolean("upd_mtch"); //грузим/обноваляем
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.f_info_fragments2, container, false);


        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Новости"));
        tabLayout.addTab(tabLayout.newTab().setText("Матчи"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        adapter = new InfoAdapter(getChildFragmentManager(), tabLayout.getTabCount(), upd_news, upd_mtch);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }



}







