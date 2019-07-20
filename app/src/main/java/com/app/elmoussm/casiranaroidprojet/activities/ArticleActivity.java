package com.app.elmoussm.casiranaroidprojet.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.elmoussm.casiranaroidprojet.R;
import com.app.elmoussm.casiranaroidprojet.model.Article;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ArticleActivity extends AppCompatActivity {

    Article article;

    TextView tv_title;
    TextView tv_url;
    TextView tv_author;
    TextView tv_content;
    TextView tv_published_at;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_article);

        // hide the default actionbar
        getSupportActionBar().hide();

        // Recieve data from extras to article object

        article = new Article();
        article.setAuthor(getIntent().getExtras().getString("article_author"));
        article.setTitle(getIntent().getExtras().getString("article_title"));
        article.setUrlToImage(getIntent().getExtras().getString("article_urlToImage"));
        article.setSourceName(getIntent().getExtras().getString("article_sourceName"));
        article.setPublishedAt(getIntent().getExtras().getString("article_publishedAt"));
        article.setDescription(getIntent().getExtras().getString("article_description"));
        article.setContent(getIntent().getExtras().getString("article_content"));

        // init views
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar_id);
        collapsingToolbarLayout.setTitleEnabled(true);

        tv_title = findViewById(R.id.aa_article_title);
        tv_url = findViewById(R.id.aa_url);
        tv_author = findViewById(R.id.aa_author);
        tv_content = findViewById(R.id.content);
        tv_published_at = findViewById(R.id.aa_published_at);
        img = findViewById(R.id.aa_thumbnail);

        // setting values to each view
        tv_title.setText(article.getTitle());
        tv_url.setText("Source : " + article.getSourceName());
        tv_content.setText(article.getContent());
        tv_author.setText("Author : " + article.getAuthor());
        tv_published_at.setText("Published at : " + article.getPublishedAt());
        // collapsingToolbarLayout : scroling down
        collapsingToolbarLayout.setTitle("Source : " + article.getSourceName());
        // placeholder for image Glide
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
        // set image using Glide
        Glide.with(this).load(article.getUrlToImage()).apply(requestOptions).into(img);
    }

    public void onClickBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void OnClickSaveToFavorite(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("favorite_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        // get old list and add current article
        List<Article> favoriteList = getsavedFavorite();
        favoriteList.add(article);
        // save to prefrences using json
        String json = gson.toJson(favoriteList);
        editor.putString("favorite_list", json);
        editor.apply();
        // show message to user
        Toast.makeText(this, "Saved to favorite list", Toast.LENGTH_LONG).show();
    }

    private List<Article> getsavedFavorite() {
        List<Article> savedFavoriteList = new ArrayList<Article>();
        SharedPreferences sharedPreferences = getSharedPreferences("favorite_preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("favorite_list", null);
        Type type = new TypeToken<ArrayList<Article>>() {
        }.getType();
        savedFavoriteList = gson.fromJson(json, type);

        if (savedFavoriteList == null) {
            savedFavoriteList = new ArrayList<>();
        }

        return savedFavoriteList;
    }

}
