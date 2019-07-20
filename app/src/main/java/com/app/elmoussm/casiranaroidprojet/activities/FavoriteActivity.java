package com.app.elmoussm.casiranaroidprojet.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.app.elmoussm.casiranaroidprojet.R;
import com.app.elmoussm.casiranaroidprojet.adapters.ArticlesAdapter;
import com.app.elmoussm.casiranaroidprojet.adapters.RecyclerItemTouchHelper;
import com.app.elmoussm.casiranaroidprojet.model.Article;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    RecyclerView articlesRecyclerview;
    ArticlesAdapter articlesAdapter;
    List<Article> favoriteArticleList;
    ConstraintLayout rootLayout;
    EditText searchInput;
    CharSequence search = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // make activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_favorite);

        // hide the action bar
        getSupportActionBar().hide();

        // ini view
        rootLayout = findViewById(R.id.root_layout);
        searchInput = findViewById(R.id.search_input);
        articlesRecyclerview = findViewById(R.id.news_rv);
        searchInput.setBackgroundResource(R.drawable.search_input_style);
        rootLayout.setBackgroundColor(getResources().getColor(R.color.white));

        // get saved list or create new
        loadData();
        Log.e("Favorite List", favoriteArticleList.toString());

        // adapter ini and setup
        articlesAdapter = new ArticlesAdapter(FavoriteActivity.this, favoriteArticleList);
        articlesRecyclerview.setAdapter(articlesAdapter);
        articlesRecyclerview.setLayoutManager(new LinearLayoutManager(FavoriteActivity.this));

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, FavoriteActivity.this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(articlesRecyclerview);

        // search action
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


    // callback when recycler view is swiped : item will be removed on swiped
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ArticlesAdapter.ArticleViewHolder) {

            // get the removed item name to display it in snack bar
            String name = favoriteArticleList.get(viewHolder.getAdapterPosition()).getTitle();

            // backup of removed item for undo purpose
            final Article deletedItem = favoriteArticleList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            AlertDialog diaBox = deleteConfirmDialog(deletedItem,viewHolder.getAdapterPosition());
            diaBox.show();

        }
    }

    private AlertDialog deleteConfirmDialog(final Article deletedItem, final int adapterPosition)
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        // remove the item from recycler view
                        articlesAdapter.removeItem(adapterPosition);
                        dialog.dismiss();
                    }

                })


                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // undo is selected, restore the deleted item
                        articlesAdapter.restoreItem(deletedItem, adapterPosition);
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;

    }

    public void onClickBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("favorite_preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("favorite_list", null);
        Type type = new TypeToken<ArrayList<Article>>() {
        }.getType();
        favoriteArticleList = gson.fromJson(json, type);

        if (favoriteArticleList == null) {
            favoriteArticleList = new ArrayList<>();
        } else {
            Log.e("DataDataData", favoriteArticleList.toString());
        }
    }
}
