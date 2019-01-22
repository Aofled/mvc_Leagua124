package com.createsmart.aofled.leagua124.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.createsmart.aofled.leagua124.DataParser;
import com.createsmart.aofled.leagua124.modeles.Match;

import com.createsmart.aofled.leagua124.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Afactor on 04.04.2017.
 */


public class MatchesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static ClickListener clickListener;

    private final int TYPE_MOVIE = 0;
    private final int TYPE_LOAD = 1;

    private static Context context;
    private ArrayList<Match> contentListHm;
    private boolean isLoading = false;


    public MatchesAdapter(Context context, ArrayList<Match> contentListHm) {
        this.context = context;
        this.contentListHm = contentListHm;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        MatchesAdapter.clickListener = clickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==TYPE_MOVIE){
            return new MovieHolder(inflater.inflate(R.layout.list_row_m2,parent,false));
        }else{
            return new LoadHolder(inflater.inflate(R.layout.progress_item,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position>=getItemCount()-1  && !isLoading ){
            isLoading = true;
        }
        if(getItemViewType(position)==TYPE_MOVIE){
            ((MovieHolder)holder).bindData(contentListHm.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return contentListHm.get(position) == null ? TYPE_LOAD : TYPE_MOVIE;
    }

    @Override
    public int getItemCount() {
        return contentListHm.size();
    }

    public static class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private ImageView iv_tm_logo1;
        private ImageView iv_tm_logo2;
        private TextView tv_tm_name1;
        private TextView tv_tm_name2;
        private TextView tv_score1;
        private TextView tv_score2;
        private TextView tv_player_goals_scr1;
        private TextView tv_player_goals_scr2;
        private TextView tv_data_matches;
        public MovieHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this); /***/
            itemView.setOnLongClickListener(this); /***/
            iv_tm_logo1 = (ImageView)itemView.findViewById(R.id.iv_tm_logo1);
            iv_tm_logo2 =(ImageView)itemView.findViewById(R.id.iv_tm_logo2);
            tv_tm_name1 =(TextView)itemView.findViewById(R.id.tv_tm_name1);
            tv_tm_name2 =(TextView)itemView.findViewById(R.id.tv_tm_name2);
            tv_score1 =(TextView)itemView.findViewById(R.id.tv_score1);
            tv_score2 =(TextView)itemView.findViewById(R.id.tv_score2);
            tv_player_goals_scr1 =(TextView)itemView.findViewById(R.id.tv_player_goals_scr1);
            tv_player_goals_scr2 =(TextView)itemView.findViewById(R.id.tv_player_goals_scr2);
            tv_data_matches =(TextView)itemView.findViewById(R.id.tv_data_matches);

            iv_tm_logo1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //нажатие на левое лого
                    clickListener.tImageOnClick(v, getAdapterPosition(), true);
                }
            });

            iv_tm_logo2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //нажатие на правое лого
                    clickListener.tImageOnClick(v, getAdapterPosition(), false);
                }
            });
        }
        void bindData(Match events){
            Picasso.get()
                    .load(events.getTeamImg1())
                    .into(iv_tm_logo1);
            Picasso.get()
                    .load(events.getTeamImg2())
                    .into(iv_tm_logo2);
            tv_tm_name1.setText(events.getTeamName1());
            tv_tm_name2.setText(events.getTeamName2());
            tv_score1.setText(""+events.getScoreT1());
            tv_score2.setText(""+events.getScoreT2());
            tv_player_goals_scr1.setText(events.getGoalsScPl1());
            tv_player_goals_scr2.setText(events.getGoalsScPl2());
            tv_data_matches.setText(DataParser.newsDate(events.getTimeGame()));

            if (events.getMatchOver()==1) {
                tv_score1.setVisibility(View.VISIBLE);
                tv_score2.setVisibility(View.VISIBLE);
            } else {
                tv_score1.setVisibility(View.INVISIBLE);
                tv_score2.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition(), true);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onClick(v, getAdapterPosition(), false);
            return true;
        }
    }

    // интрфейс для нажатия
    public interface ClickListener {
        void onClick(View v, int position, boolean click); // нажатие на ячейку
        void tImageOnClick (View v, int position, boolean click);  //нажатие на логотип_1 и логотип_2
    }

    static class LoadHolder extends RecyclerView.ViewHolder{
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    public void notifyDataChanged(){
        notifyDataSetChanged();
        isLoading = false;
    }





}
