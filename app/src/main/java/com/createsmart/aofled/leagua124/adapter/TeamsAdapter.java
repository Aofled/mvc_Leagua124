package com.createsmart.aofled.leagua124.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.createsmart.aofled.leagua124.modeles.Team;
import com.createsmart.aofled.leagua124.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class TeamsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static ClickListener clickListener;

    private ArrayList<Team> contentList;
    private Context context;

    public TeamsAdapter(Context context, ArrayList<Team> contentList) {
        this.contentList = contentList;
        this.context=context;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        TeamsAdapter.clickListener = clickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new MovieHolder(inflater.inflate(R.layout.list_row_t2,parent,false));
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
        private TextView tv_teamname;
        private ImageView iv_logo;
        public MovieHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this); /***/
            itemView.setOnLongClickListener(this); /***/
            tv_teamname=(TextView)itemView.findViewById(R.id.tv_name_team);
            iv_logo=(ImageView)itemView.findViewById(R.id.iv_logo);
        }

        void bindData(Team events){
            Picasso.get()
                    .load(events.getTeamImg())
                    .into(iv_logo);
            tv_teamname.setText(events.getTeamName());
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
