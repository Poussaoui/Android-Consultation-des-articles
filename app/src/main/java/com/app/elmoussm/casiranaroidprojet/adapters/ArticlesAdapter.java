package com.app.elmoussm.casiranaroidprojet.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.elmoussm.casiranaroidprojet.R;
import com.app.elmoussm.casiranaroidprojet.activities.ArticleActivity;
import com.app.elmoussm.casiranaroidprojet.activities.MainActivity;
import com.app.elmoussm.casiranaroidprojet.model.Article;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder> implements Filterable {


    Context mContext;
    List<Article> mData;
    List<Article> mDataFiltered;
    RequestOptions option;

    public ArticlesAdapter(Context mContext, List<Article> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.mDataFiltered = mData;

        // Request option for Glide
        option = new RequestOptions().centerCrop().placeholder(R.drawable.circul6).error(R.drawable.circul6);
    }



    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.item_article, viewGroup, false);
        return new ArticleViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder articleViewHolder, final int position) {

        // bind data

        // apply animation to views
        // first lets create an animation for photo
        articleViewHolder.img.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation));

        // then create the animation for the whole card
        articleViewHolder.container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));


        articleViewHolder.tv_title.setText(mDataFiltered.get(position).getTitle());
        articleViewHolder.tv_content.setText(mDataFiltered.get(position).getDescription());
        articleViewHolder.tv_date.setText(mDataFiltered.get(position).getPublishedAt());
//        articleViewHolder.img.setImageResource(mDataFiltered.get(position).getUserPhoto());

        // Load Image from the internet and set it into Imageview using Glide
        Glide.with(mContext).load(mData.get(position).getUrlToImage()).apply(option).into(articleViewHolder.img);

        // Actions
        articleViewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ArticleActivity.class);
                intent.putExtra("article_author", mData.get(position).getAuthor());
                intent.putExtra("article_title", mData.get(position).getTitle());
                intent.putExtra("article_urlToImage", mData.get(position).getUrlToImage());
                intent.putExtra("article_sourceName", mData.get(position).getSourceName());
                intent.putExtra("article_publishedAt", mData.get(position).getPublishedAt());
                intent.putExtra("article_description", mData.get(position).getDescription());
                intent.putExtra("article_content", mData.get(position).getContent());

                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataFiltered.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String Key = constraint.toString();
                if (Key.isEmpty()) {

                    mDataFiltered = mData;

                } else {
                    List<Article> lstFiltered = new ArrayList<>();
                    for (Article row : mData) {
                        if (row.getTitle().toLowerCase().contains(Key.toLowerCase())) {
                            lstFiltered.add(row);
                        }
                    }
                    mDataFiltered = lstFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDataFiltered = (List<Article>) results.values;
                notifyDataSetChanged();

            }
        };

    }


    public void removeItem(int adapterPosition) {
        // remove item from adapter
        mData.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);

        // save list to preferences
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("favorite_preferences", mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mData);
        editor.putString("favorite_list", json);
        editor.apply();
    }

    public void restoreItem(Article article, int position) {
        mData.add(position, article);
        // notify item added by position
        notifyItemInserted(position);
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title, tv_content, tv_date;
        ImageView img;
        RelativeLayout container;
        ConstraintLayout view_foreground;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_description);
            tv_date = itemView.findViewById(R.id.tv_date);
            img = itemView.findViewById(R.id.img_user);
            view_foreground = itemView.findViewById(R.id.view_foreground);

        }

    }
}
