package com.example.movie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movie.Model.Movie;

import java.util.ArrayList;

public class SavedMovies extends AppCompatActivity {

    TextView tv_search, tv_saved;
    private Database db;
    private ArrayList<Movie> movieList = new ArrayList<Movie>();
    private Movie movie;
    private RecyclerView rv;
    private RecyclerView.Adapter rvadapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_movies);

        Intent intent = getIntent();
        db = new Database(this);
        Cursor data = db.getListContents();
        if (data.getCount() == 0) Toast.makeText(this, "No Saved Movies", Toast.LENGTH_SHORT).show();
        else {
            while (data.moveToNext()) {
                movieList.add(new Movie(data.getString(1), data.getString(0), data.getString(2)));
            }
        }

        rv = findViewById(R.id.recyclerView);
        rvadapter = new MovieAdapter(this, movieList);
        layoutManager = new GridLayoutManager(this, 2);

        rv.setLayoutManager(layoutManager);
        rv.setAdapter(rvadapter);

        tv_search = findViewById(R.id.tv_search);
        tv_saved = findViewById(R.id.tv_saved);

        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SavedMovies.this, MainActivity.class);
                startActivity(i);
            }
        });

        tv_saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SavedMovies.this, "Already on Saved Page", Toast.LENGTH_SHORT).show();
            }
        });

    }
}