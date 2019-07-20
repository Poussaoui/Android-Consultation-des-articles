package com.app.elmoussm.casiranaroidprojet.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.elmoussm.casiranaroidprojet.model.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HttpRequest {

    private Context context;

    public HttpRequest(Context context) {
        this.context = context;
    }

    public List<Article> getData() {
        final List<Article> articles = new ArrayList<Article>();

        String URL = "http://www.dailyinfospace.com/newsfeed.json";
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this.context);

        // get data using JsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        // Process the JSON
                        try {
                            // Get the JSON array
                            JSONArray array = jsonObject.getJSONArray("articles");

                            // Loop through the array elements
                            for (int i = 0; i < array.length(); i++) {
                                // Get current json object
                                JSONObject articleJson = array.getJSONObject(i);

                                Article article = new Article();

                                // Get the current article (json object) data
                                article.setId(0);
                                article.setAuthor(articleJson.getString("author"));
                                article.setTitle(articleJson.getString("title"));
                                article.setUrlToImage(articleJson.getString("urlToImage"));
                                article.setSourceName(articleJson.getJSONObject("source").getString("name"));
                                article.setPublishedAt(articleJson.getString("publishedAt"));
                                article.setDescription(articleJson.getString("description"));
                                article.setContent(articleJson.getString("content"));

                                articles.add(article);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("jsonObject Error", volleyError.toString());
                    }
                }
        );

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

        return articles;
    }

}
