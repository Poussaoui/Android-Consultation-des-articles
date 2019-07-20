package com.app.elmoussm.casiranaroidprojet.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.app.elmoussm.casiranaroidprojet.R;
import com.app.elmoussm.casiranaroidprojet.adapters.ArticlesAdapter;
import com.app.elmoussm.casiranaroidprojet.model.Article;
import com.app.elmoussm.casiranaroidprojet.network.HttpRequest;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView articlesRecyclerview;
    ArticlesAdapter articlesAdapter;
    List<Article> articleList;
    ConstraintLayout rootLayout;
    EditText searchInput;
    CharSequence search = "";

    // save state
    static final String SAVED_MDATA = "StatList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // let's make this activity on full screen

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            //      articleList = () savedInstanceState.get(SAVED_MDATA);
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        // hide the action bar

        getSupportActionBar().hide();


        // ini view
        rootLayout = findViewById(R.id.root_layout);
        searchInput = findViewById(R.id.search_input);
        articlesRecyclerview = findViewById(R.id.news_rv);

        // light theme is on
        searchInput.setBackgroundResource(R.drawable.search_input_style);
        rootLayout.setBackgroundColor(getResources().getColor(R.color.white));


        articleList = new ArrayList<>();

        HttpGetRequestAsyncTask asyncTask = new HttpGetRequestAsyncTask();
        asyncTask.execute();

        // adapter init and setup
        showArticleList();


        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                articlesAdapter.getFilter().filter(s);
                search = s;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public void onClickFavoriteList(View view) {
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
    }

    // AsyncTask
    private class HttpGetRequestAsyncTask extends AsyncTask<String, Void, String> {

        HttpRequest httpRequest;
        private ProgressDialog p;

        public HttpGetRequestAsyncTask() {
            this.p = new ProgressDialog(MainActivity.this);
            httpRequest = new HttpRequest(MainActivity.this);
            articleList = new ArrayList<Article>();
        }

        @Override
        protected void onPreExecute() {
            p.setMessage("Please wait...");
            p.setIndeterminate(false);
            p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {

            articleList = httpRequest.getData();
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            showArticleList();
            p.dismiss();
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

    }


    public void showArticleList(){
        articlesAdapter = new ArticlesAdapter(MainActivity.this, articleList);
        articlesRecyclerview.setAdapter(articlesAdapter);
        articlesRecyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }
}
