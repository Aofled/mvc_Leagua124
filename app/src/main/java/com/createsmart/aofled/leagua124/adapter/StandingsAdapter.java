package com.createsmart.aofled.leagua124.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.createsmart.aofled.leagua124.modeles.Standing;
import com.createsmart.aofled.leagua124.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class StandingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static ClickListener clickListener;
    private ArrayList<Standing> contentList;
    private Context context;

    public StandingsAdapter(Context context, ArrayList<Standing> contentList) {
        this.contentList = contentList;
        this.context=context;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        StandingsAdapter.clickListener = clickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new MovieHolder(inflater.inflate(R.layout.list_row_s2,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((MovieHolder)holder).bindData(contentList.get(position));
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public static class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private FrameLayout fr_winning_place;
        private TextView tv_namber;
        private ImageView iv_up_down;
        private TextView tv_name_team;
        private TextView tv_games;
        private TextView tv_winning;
        private TextView tv_draws;
        private TextView tv_defeats;
        private TextView tv_goals;
        private TextView tv_points;
        public MovieHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this); /***/
            itemView.setOnLongClickListener(this); /***/
            fr_winning_place = (FrameLayout)itemView.findViewById(R.id.fr_winning_place);
            tv_namber=(TextView)itemView.findViewById(R.id.tv_namber);
            iv_up_down=(ImageView)itemView.findViewById(R.id.iv_up_down);
            tv_name_team=(TextView)itemView.findViewById(R.id.tv_name_team);
            tv_games=(TextView)itemView.findViewById(R.id.tv_games);
            tv_winning=(TextView)itemView.findViewById(R.id.tv_winning);
            tv_draws=(TextView)itemView.findViewById(R.id.tv_draws);
            tv_defeats=(TextView)itemView.findViewById(R.id.tv_defeats);
            tv_goals=(TextView)itemView.findViewById(R.id.tv_goals);
            tv_points=(TextView)itemView.findViewById(R.id.tv_points);
        }

        void bindData(Standing events){
            if (events.getWinningPlace()==1) { //выходит ли команда с группы
                fr_winning_place.setBackgroundColor(Color.parseColor("#00CC66"));
            } else {
                fr_winning_place.setBackgroundColor(Color.parseColor("#F7F7F7"));
            }
            if (events.getUpDown()==1) {
                Picasso.get()
                        .load(R.drawable.up)
                        .into(iv_up_down);
            } else if (events.getUpDown()==2)  {
                Picasso.get()
                        .load(R.drawable.down)
                        .into(iv_up_down);
            }  else {
                Picasso.get()
                        .load(R.drawable.stop)
                        .into(iv_up_down);
            }
            tv_namber.setText(events.getNamber());
            tv_name_team.setText(events.getNameTeam());
            tv_games.setText(events.getGames());
            tv_winning.setText(events.getWinning());
            tv_draws.setText(events.getDraws());
            tv_defeats.setText(events.getDefeats());
            tv_goals.setText(events.getGoals());
            tv_points.setText(events.getPoints());
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
        void onClick(View v, int position,  boolean click);
    }

}
