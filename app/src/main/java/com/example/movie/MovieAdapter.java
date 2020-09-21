package com.example.movie;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.movie.Model.Movie;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    Context ctx;
    ArrayList<Movie> movieList;
    Database db;

    public MovieAdapter(Context ctx, ArrayList<Movie> movieList) {
        this.ctx = ctx;
        this.movieList = movieList;
    }

    public void setMovieList(ArrayList<Movie> movieList){
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        Glide.with(ctx).load(movie.getPoster()).into(holder.imageView);
        holder.title.setText(movie.getTitle());
//        holder.year.setText(movie.getYear());
//        holder.id.setText(movie.getImdbid());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView imageView;
        TextView title, year, id;
        String Stringid, Stringtitle, Stringposter;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.tv_title);
//            year = itemView.findViewById(R.id.tv_year);
//            id = itemView.findViewById(R.id.tv_id);
//            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Movie clickedDataItem = movieList.get(position);
                        Intent i = new Intent(ctx, DetailActivity.class);
                        i.putExtra("poster", movieList.get(position).getPoster());
                        i.putExtra("title", movieList.get(position).getTitle());
                        i.putExtra("imdbID", movieList.get(position).getImdbid());
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ctx.startActivity(i);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final PopupMenu popupMenu = new PopupMenu(itemView.getContext(), view);
                    db = new Database(itemView.getContext());
                    Stringid = movieList.get(getAdapterPosition()).getImdbid();
                    Stringtitle = movieList.get(getAdapterPosition()).getTitle();
                    Stringposter = movieList.get(getAdapterPosition()).getPoster();
                    final Movie movie = new Movie();
                    popupMenu.inflate(R.menu.main_menu);

                    if (db.checkMovie(Stringid)) {
                        popupMenu.getMenu().getItem(0).setTitle("Save");
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                movie.setImdbid(Stringid);
                                movie.setTitle(Stringtitle);
                                movie.setPoster(Stringposter);
                                db.addMovie(movie);
                                return true;
                            }
                        });
                    }
                    else {
                        popupMenu.getMenu().getItem(0).setTitle("Delete");
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                db.deleteMovie(Stringid);
                                return true;
                            }
                        });
                    }
                    popupMenu.show();
                    return false;
                }
            });
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Movie movie = movieList.get(position);
        }

        @Override
        public boolean onLongClick(View view) {
            return true;
        }
    }
}