package com.example.movie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.movie.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView tv_saved, tv_search;
    EditText et_search;
    private ArrayList<Movie> movieList;
    private Movie movie;
    private RecyclerView rv;
    private MovieAdapter adapter;
    private RequestQueue rq;
    private JsonArrayRequest request;

    public static final String BASE_URL = "http://www.omdbapi.com/";
    public static final String apikey = "e323d2dd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = getIntent();
        et_search = findViewById(R.id.et_search);
        tv_saved = findViewById(R.id.tv_saved);
        tv_search = findViewById(R.id.tv_search);
//        String search2 = et_search.getText().toString();
        movieList = new ArrayList<>();
        rv = findViewById(R.id.recyclerView);
//        JSONRequest(search2);
//        movieList.add(new Movie("a", "a", "https://m.media-amazon.com/images/M/MV5BZmUwNGU2ZmItMmRiNC00MjhlLTg5YWUtODMyNzkxODYzMmZlXkEyXkFqcGdeQXVyNTIzOTk5ODM@._V1_SX300.jpg", "a"));
//        Log.i("MainActivity", "Isi arraylist : " + movieList.size());
        adapter = new MovieAdapter(this, movieList);
        rq = Volley.newRequestQueue(this);
//        rq.add(getTitle(search2));

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH){
                    String s = textView.getText().toString();
                    Log.d("Input", "onEditorAction: " + s);
                    search(s);
                    return true;
                }
                return false;
            }
        });

        tv_saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SavedMovies.class);
                startActivity(intent);
            }
        });

        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Already on Search Page", Toast.LENGTH_SHORT).show();
            }
        });

//        et_search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                search(editable.toString());
//            }
//        });

        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.setAdapter(adapter);
    }

    private void search(String s) {
//        String search2 = et_search.getText().toString();
        rq.add(getTitle(s));
    }

    JsonObjectRequest getTitle(String term){
        Uri uri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter("apikey", apikey).appendQueryParameter("s", term).build();
        String url = uri.toString();
        Log.d("INPUT", "getTitle: " + url);

        movieList.clear();
//        String url = BASE_URL + "?apikey=e323d2dd&s=" + term;
//        String url = BASE_URL + "?apikey=e323d2dd&s=avenger";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject object = null;
                movieList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("Search");
                    for (int i = 0; i < jsonArray.length(); i++){
                        object = jsonArray.getJSONObject(i);
                        movie = new Movie();
                        movie.setTitle(object.getString("Title"));
                        movie.setPoster(object.getString("Poster"));
                        movie.setImdbid(object.getString("imdbID"));
                        movieList.add(movie);
                    }
                    adapter.setMovieList(movieList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

    return jsonObjectRequest;
    }

//    private void JSONRequest(String term) {
////        String url = "http://www.omdbapi.com/?s=avenger&apikey=e323d2dd";
//        String url = BASE_URL + "?s=" + term + "&apikey=e323d2dd";
//        rq = Volley.newRequestQueue(this);
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                JSONObject object = null;
//                try {
//                    JSONObject jsonObject = new JSONObject(response.toString());
//                    JSONArray jsonArray = jsonObject.getJSONArray("Search");
//                    for (int i = 0; i < jsonArray.length(); i++){
//                        try {
//                            object = jsonArray.getJSONObject(i);
//                            movie = new Movie();
//                            movie.setTitle(object.getString("Title"));
//                            movie.setPoster(object.getString("Poster"));
//                            movie.setImdbid(object.getString("imdbID"));
//                            movie.setYear(object.getString("Year"));
//                            movieList.add(movie);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                showData(movieList);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        rq.add(jsonObjectRequest);
//    }
//
//    private void showData(ArrayList<Movie> movieList) {
//        rv.setLayoutManager(new GridLayoutManager(this, 2));
//        rv.setHasFixedSize(true);
//        MovieAdapter mAdapter = new MovieAdapter(this, movieList);
//        rv.setAdapter(mAdapter);
//    }
}