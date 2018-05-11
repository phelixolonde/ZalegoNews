package com.automata.zalego_news;


import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    private static final String TAG = "ZALEGO";
    RecyclerView recyclerView;


    ProgressDialog pDialog;

    private String urlJsonObj = "https://newsapi.org/v1/articles?source=bbc-sport&apiKey=100c60eefc1a493c9ff9e0bada164f66";
    List<NewsModel> data = new ArrayList<>();
    private AdapterNews mAdapter;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        LinearLayoutManager lm = new LinearLayoutManager(this);

        pDialog = new ProgressDialog(this);

        recyclerView = findViewById(R.id.recycler_news);
        recyclerView.setLayoutManager(lm);

        db = openOrCreateDatabase("zalego", MODE_PRIVATE, null);
        db.execSQL("create table if not exists news(title VARCHAR,description VARCHAR)");
        db.execSQL("delete from news");

        getNewsFeed();
    }


    public void getNewsFeed() {
        pDialog.show();
        pDialog.setMessage("Please wait...");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                pDialog.dismiss();
                try {
                    JSONArray array = response.getJSONArray("articles");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject article = (JSONObject) array.get(i);
                        String title = article.getString("title");
                        String description = article.getString("description");

                        String sTitle = title.replace("'", "''");
                        String sDesc = description.replace("'", "''");


                        try {
                            String sql = "insert into news values('" + sTitle + "','" + sDesc + "')";
                            Log.d("SQLITE_ZALEGO", sql);
                            db.execSQL(sql);
                        } catch (Exception e) {
                            Log.d("SQLITE_INSERT", e.getMessage(), e);
                        }


                    }
                    displayNewsFromSQLite();


                } catch (JSONException e) {
                    Log.e("ZALEGO_NEWS_EXCEPTION", e.getMessage(), e);

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(Home.this, "Could not fetch news at this time. Please try again later", Toast.LENGTH_SHORT).show();

            }
        });

        ZalegoNews.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void displayNewsFromSQLite() {



        String s = "select * from news";
        Cursor c = db.rawQuery(s, null);
        String data_[][];
        String title, desc;
        if (c != null && c.getCount() > 0) {

            //data_ = new String[2][cursor.getCount()];
            //cursor.moveToFirst();
           /* while (cursor.moveToNext()) {

                for (int i = 0; i < data_.length; i++) {

                    title = data_[0][i];
                    desc = data_[1][i];
                    desc = cursor.getString(1);

                    newsModel.title = title;
                    newsModel.description = desc;
                    data.add(newsModel);
                }*/
            c.moveToFirst();
            while(!c.isAfterLast()) {
                NewsModel newsModel = new NewsModel();
                newsModel.title = c.getString(0);
                newsModel.description = c.getString(1);
                data.add(newsModel);
                c.moveToNext();
            }

            }
           // cursor.close();
            mAdapter = new AdapterNews(Home.this, data);
            recyclerView.setAdapter(mAdapter);

    }
}


