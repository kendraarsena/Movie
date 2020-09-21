package com.example.movie;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.movie.Model.Movie;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DetailActivity extends AppCompatActivity {
    TextView tv_title, tv_year, tv_imbdid;
    ImageView imageView;
    Button bt_save;
    Database db;
    private Movie movie;
    private final AppCompatActivity activity = DetailActivity.this;
    protected Cursor cursor;
    String thumbnail, year, title, imdbID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initCollapsingToolbar();

        imageView = findViewById(R.id.thumbnail_image_header);
        tv_title = findViewById(R.id.title);
        tv_year = findViewById(R.id.year);
        tv_imbdid = findViewById(R.id.imdbid);
        bt_save = findViewById(R.id.bt_save);
        db = new Database(this);

        Intent intent = getIntent();
        if (intent.hasExtra("title")){
            thumbnail = getIntent().getExtras().getString("poster");
            title = getIntent().getExtras().getString("title");
//            year = getIntent().getExtras().getString("year");
            imdbID = getIntent().getExtras().getString("imdbID");
            if (!db.checkMovie(imdbID)){
                bt_save.setText("Delete");
            }
            else bt_save.setText("Save");

            Glide.with(this).load(thumbnail).into(imageView);

            tv_title.setText(title);
//            tv_year.setText(year);
            tv_imbdid.setText("IMDB ID: " + imdbID);
        } else {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.checkMovie(imdbID)){
                    db = new Database(activity);
                    movie = new Movie();

                    movie.setPoster(thumbnail);
                    movie.setTitle(title);
                    movie.setImdbid(imdbID);
                    db.addMovie(movie);
                    Toast.makeText(activity, "Movies Saved", Toast.LENGTH_SHORT).show();
                    if (!db.checkMovie(imdbID)){
                        bt_save.setText("Delete");
                    }
                    else bt_save.setText("Save");
                } else {
//                    String id = getIntent().getExtras().getString("imdbID");
                    db = new Database(activity);
                    db.deleteMovie(imdbID);
                    Toast.makeText(DetailActivity.this, "Movies Removed", Toast.LENGTH_SHORT).show();
                    if (!db.checkMovie(imdbID)){
                        bt_save.setText("Delete");
                    }
                    else bt_save.setText("Save");
                    SQLiteDatabase database = db.getReadableDatabase();
                    String SQL = "SELECT * FROM SavedVideos";
                    cursor = database.rawQuery(SQL, null);
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++){
                        cursor.moveToPosition(i);
                        System.out.println(cursor.getString(0) + " " + cursor.getString(1)  + " " + cursor.getString(2));
                    }
                }

            }
        });
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (scrollRange == -1){
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + i == 0) {
                    collapsingToolbarLayout.setTitle("Movie Details");
                    isShow = true;
                }
                else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
}
