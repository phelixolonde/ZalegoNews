package com.automata.zalego_news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class AdapterNews extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<NewsModel> data = Collections.emptyList();

    public AdapterNews(Context context, List<NewsModel> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.news_row, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        final NewsModel current = data.get(position);

        myHolder.newsTitle.setText(current.title);
        myHolder.newsDesc.setText(current.description);




    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();

    }


    public class MyHolder extends RecyclerView.ViewHolder {

        TextView newsTitle;
        TextView  newsDesc;


        public MyHolder(View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsDesc = itemView.findViewById(R.id.newsDesc);
        }

    }

}