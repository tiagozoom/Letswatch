package com.example.tgzoom.letswatch.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tgzoom.letswatch.R;
import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.util.StringUtils;
import com.example.tgzoom.letswatch.util.URIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tgzoom on 1/4/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieDBHolder> {
    private List<Movie> mMovieDBArrayList = new ArrayList<Movie>();

    public MovieAdapter(ArrayList<Movie> movieArrayList) {
        mMovieDBArrayList = movieArrayList;
    }

    public static class MovieDBHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_image)
        ImageView cardImage;
        @BindView(R.id.card_title)
        TextView cardTitle;
        @BindView(R.id.movie_year) TextView movie_year;
        @BindView(R.id.movie_rate) TextView movie_rate;
        @BindView(R.id.card_menu) ImageView card_menu;
        @BindView(R.id.movie_layout)
        RelativeLayout movie_layout;
        Context mContext;

        public MovieDBHolder(View view, final Context context) {
            super(view);
            ButterKnife.bind(this, view);
            mContext = context;
        }
    }


    public List<Movie> getArrayList() {
        return mMovieDBArrayList;
    }

    @Override
    public MovieDBHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_movies_list_item, parent, false);
        return new MovieDBHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(final MovieDBHolder holder, final int position) {
        final Movie movie = mMovieDBArrayList.get(position);

        String title = StringUtils.formatMovieTitle(holder.mContext, movie.getTitle());
        holder.cardTitle.setText(title);

        String formated_year = StringUtils.formatMovieYear(movie.getRelease_date());
        holder.movie_year.setText(formated_year);

        if (movie.getPoster_path() != null) {

            String poster_path = URIUtils.buildPosterPath(movie.getPoster_path()).toString();

            Glide.with(holder.mContext)
                    .load(poster_path)
                    .centerCrop()
                    .placeholder(R.color.colorPrimary)
                    .into(holder.cardImage);
        }

        holder.movie_rate.setText(movie.getVote_average().toString());

    }

    @Override
    public int getItemCount() {
        if (mMovieDBArrayList != null) {
            return mMovieDBArrayList.size();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return mMovieDBArrayList.get(position).getApi_movie_id();
    }

    public void clear(){
        mMovieDBArrayList = new ArrayList<>();
    }

    public void addArrayList(List<Movie> movieDBArrayList){
        if(mMovieDBArrayList.size() <= 0){
            mMovieDBArrayList = movieDBArrayList;
        }else{
            mMovieDBArrayList.addAll(movieDBArrayList);
        }

        notifyDataSetChanged();
    }

}