package com.createsmart.aofled.leagua124.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.createsmart.aofled.leagua124.DataParser;
import com.createsmart.aofled.leagua124.modeles.News;
import com.createsmart.aofled.leagua124.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Afactor on 04.04.2017.
 */


public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static ClickListener clickListener;




    private final int TYPE_MOVIE = 0;
    private final int TYPE_LOAD = 1;



    private static Context context;
    private ArrayList<News> contentListHm;


    private boolean isLoading = false;


    public NewsAdapter(Context context, ArrayList<News> contentListHm) {
        this.context = context;
        this.contentListHm = contentListHm;
            }

    public void setOnItemClickListener(ClickListener clickListener) {
        NewsAdapter.clickListener = clickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==TYPE_MOVIE){
            return new MovieHolder(inflater.inflate(R.layout.list_row_n2,parent,false));
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
        private ImageView image;
        private TextView title;//никнейм
        private TextView description;
        private TextView time_create;  //эвент
        public MovieHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this); /***/
            itemView.setOnLongClickListener(this); /***/
            image = (ImageView)itemView.findViewById(R.id.iv_image);
            title =(TextView)itemView.findViewById(R.id.tv_title);
            description =(TextView)itemView.findViewById(R.id.tv_description);
            time_create = (TextView)itemView.findViewById(R.id.tv_time);//имя
        }
        void bindData(final News events){
            Picasso.get()
                    .load(events.getImage())
                    .placeholder(R.drawable.click_no)
                    .error(R.drawable.no_img_news)
                    .into(image);
            title.setText(events.getTitle());
            description.setText(events.getDescription());
            time_create.setText(DataParser.newsDate(events.getTimeCreate()));

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
