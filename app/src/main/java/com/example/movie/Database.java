package com.example.movie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.PopupMenu;

import com.example.movie.Model.Movie;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    SQLiteDatabase db;

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "MOVIES.db";

    private static final String TABLE_NAME = "SavedVideos";
    private static final String FIELD_USER_POSTER = "Poster";
    private static final String FIELD_USER_TITLE = "Title";
    private static final String FIELD_USER_ID = "ID";

    private static final String CREATE_TABLE_MOVIES = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + FIELD_USER_POSTER + " TEXT, " + FIELD_USER_TITLE + " TEXT, " + FIELD_USER_ID + " TEXT);";

    private static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public Database(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_MOVIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void addMovie(Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIELD_USER_POSTER, movie.getPoster());
        cv.put(FIELD_USER_TITLE,movie.getTitle());
        cv.put(FIELD_USER_ID, movie.getImdbid());
        db.insert(TABLE_NAME, null, cv);
        db.close();
    }

    public boolean checkMovie(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM SavedVideos WHERE ID = ?",new String[]{id});
        if (c.getCount()>0) {
            return false;
        }
        else return true;
    }

    public void deleteMovie(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, FIELD_USER_ID + "=?", new String[]{id});
    }

    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }
}
