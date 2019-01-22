package com.createsmart.aofled.leagua124.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.createsmart.aofled.leagua124.modeles.Player;
import com.createsmart.aofled.leagua124.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class PlayersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static ClickListener clickListener;
    private static Context context;
    private ArrayList<Player> contentList;
    private final int TYPE_MOVIE = 0;
    private final int TYPE_LOAD = 1;
    private boolean isLoading = false;



    public PlayersAdapter(Context context, ArrayList<Player> contentList) {
        this.context = context;
        this.contentList = contentList;
    }


     public void setOnItemClickListener(ClickListener clickListener) {
        PlayersAdapter.clickListener = clickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==TYPE_MOVIE){
            return new MovieHolder(inflater.inflate(R.layout.list_row_p2,parent,false));
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
            ((MovieHolder)holder).bindData(contentList.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return contentList.get(position) == null ? TYPE_LOAD : TYPE_MOVIE;
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }


    public static class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private ImageView iv_team_logo_small;
        private TextView tv_player_name;//никнейм
        private TextView tv_player_position;
        private TextView tv_player_age;
        private TextView tv_player_goals;

        public MovieHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this); /***/
            itemView.setOnLongClickListener(this); /***/
            iv_team_logo_small =(ImageView) itemView.findViewById(R.id.iv_team_logo_small);
            tv_player_name =(TextView)itemView.findViewById(R.id.tv_player_name);
            tv_player_position =(TextView)itemView.findViewById(R.id.tv_player_position);
            tv_player_age = (TextView)itemView.findViewById(R.id.tv_player_age);
            tv_player_goals = (TextView)itemView.findViewById(R.id.tv_player_goals);
        }
        void bindData(Player events){
            Picasso.get()
                    .load(events.getTeamImgSmall())
                    .into(iv_team_logo_small);
            tv_player_position.setText(String.valueOf(getAdapterPosition()+1));
            tv_player_name.setText(events.getPlName());
            tv_player_age.setText(events.getAge());
            tv_player_goals.setText(events.getGoalsScored());
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
