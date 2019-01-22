package com.createsmart.aofled.leagua124.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.createsmart.aofled.leagua124.modeles.TeamDetail;
import com.createsmart.aofled.leagua124.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TeamDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<TeamDetail> dataSet;
    private Context mContext;
    private int total_types;


    public TeamDetailsAdapter(ArrayList<TeamDetail> data, Context context) {
        this.dataSet = data;
        this.mContext = context;
        this.total_types = dataSet.size();
    }

    //типы наших ячеек
    //TeamType
    protected class TeamTypeViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_team_logo;
        private TextView tv_mid_age;
        private TextView tv_tk_win;
        private TextView tv_goals_scored;
        private TextView tv_goals_missing;
        private TextView tv_difference;
        public TeamTypeViewHolder(View itemView) {
            super(itemView);
            this.iv_team_logo = (ImageView) itemView.findViewById(R.id.iv_team_logo);
            this.tv_mid_age = (TextView) itemView.findViewById(R.id.tv_mid_age);
            this.tv_tk_win = (TextView) itemView.findViewById(R.id.tv_tk_win);
            this.tv_goals_scored = (TextView) itemView.findViewById(R.id.tv_mid_age);
            this.tv_goals_missing = (TextView) itemView.findViewById(R.id.tv_goals_missing);
            this.tv_difference = (TextView) itemView.findViewById(R.id.tv_difference);
        }
    }

    //PlayerType
    protected static class PlayerTypeViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_id_player;
        private TextView tv_player_name;
        private TextView tv_player_position;
        private TextView tv_player_age;
        private TextView tv_player_goals;

        public PlayerTypeViewHolder(View itemView) {
            super(itemView);
            this.tv_id_player = (TextView) itemView.findViewById(R.id.tv_id_player);
            this.tv_player_name = (TextView) itemView.findViewById(R.id.tv_player_name);
            this.tv_player_position = (TextView) itemView.findViewById(R.id.tv_player_position);
            this.tv_player_age = (TextView) itemView.findViewById(R.id.tv_player_age);
            this.tv_player_goals = (TextView) itemView.findViewById(R.id.tv_player_goals);
        }
    }


    //подключения моделей
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TeamDetail.type_team:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_td_team2, parent, false);
                return new TeamTypeViewHolder(view);
            case TeamDetail.type_players:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_td_players2, parent, false);
                return new PlayerTypeViewHolder(view);
        }
        return null;
    }




    //выбор моделей
    @Override
    public int getItemViewType(int position) {
        switch (dataSet.get(position).getType()) {
            case "type-team":
                return TeamDetail.type_team;
            case "type-players":
                return TeamDetail.type_players;
            default:
                return -1;
        }
    }




    //заполнения наших моделей
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final TeamDetail object = dataSet.get(position);
        if (object != null) {
            switch (object.getType()) {
                case "type-team": {

                    Picasso.get()
                            .load(object.getTeamImg())
                            .error(R.drawable.noimage)
                            .into(((TeamTypeViewHolder) holder).iv_team_logo);

                    ((TeamTypeViewHolder) holder).tv_mid_age.setText(new DecimalFormat("#0.0").format( (double)(object.getMidAge())));
                    ((TeamTypeViewHolder) holder).tv_tk_win.setText(Integer.toString(object.getTkWin()));
                    ((TeamTypeViewHolder) holder).tv_goals_scored.setText(Integer.toString(object.getGoalsScoredTeam()));
                    ((TeamTypeViewHolder) holder).tv_goals_missing.setText(Integer.toString(object.getGoalsMissing()));
                    ((TeamTypeViewHolder) holder).tv_difference.setText(new DecimalFormat("#0.00").format( (double)(object.getGoalsScoredTeam())/object.getGoalsMissing()));
                }
                break;
                case "type-players": {
                    ((PlayerTypeViewHolder) holder).tv_id_player.setText(object.getNumberInTeam());
                    ((PlayerTypeViewHolder) holder).tv_player_name.setText(object.getPlName());
                    ((PlayerTypeViewHolder) holder).tv_player_position.setText(object.getPosition());
                    ((PlayerTypeViewHolder) holder).tv_player_age.setText(object.getAge());
                    ((PlayerTypeViewHolder) holder).tv_player_goals.setText(object.getGoalsScored());
                }
                break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }






}
