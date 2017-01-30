package com.example.tgzoom.letswatch.favourites;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.tgzoom.letswatch.BR;
import com.example.tgzoom.letswatch.R;
import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.listener.MoviesItemListener;
import com.example.tgzoom.letswatch.util.StringUtils;
import com.example.tgzoom.letswatch.util.URIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tgzoom on 1/27/17.
 */

public class FavouriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Movie> mMovieDBArrayList = new ArrayList<Movie>();
    private MoviesItemListener mMoviesItemListener;
    public static final int VIEW_ITEM = 0;
    public static final int PROGRESS_BAR = 1;

    public FavouriteAdapter(ArrayList<Movie> movieArrayList, MoviesItemListener moviesItemListener) {
        mMovieDBArrayList = movieArrayList;
        mMoviesItemListener = moviesItemListener;
    }

    public void swapArrayList(List<Movie> movies) {
        mMovieDBArrayList = movies;
        notifyDataSetChanged();
    }

    public static class MovieHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding mViewDataBinding;
        public MovieHolder(ViewDataBinding view) {
            super(view.getRoot());
            mViewDataBinding = view;
        }
        public ViewDataBinding getViewDataBinding(){
            return mViewDataBinding;
        }
    }

    public static class ProgressBarHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding mViewDataBinding;
        public ProgressBarHolder(ViewDataBinding view) {
            super(view.getRoot());
            mViewDataBinding = view;
        }

        public ViewDataBinding getViewDataBinding(){
            return mViewDataBinding;
        }
    }


    public List<Movie> getArrayList() {
        return mMovieDBArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding view;
        RecyclerView.ViewHolder viewHolder;

        if (viewType == VIEW_ITEM) {
            view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.fragment_movies_list_item,parent,false);
            viewHolder = new MovieHolder(view);
        } else {
            view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.loading_list_item, parent, false);
            viewHolder = new ProgressBarHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof MovieHolder) {
            final MovieHolder holder = (MovieHolder) viewHolder;
            final Movie movie = mMovieDBArrayList.get(position);
            ViewDataBinding viewDataBinding = ((MovieHolder) viewHolder).getViewDataBinding();
            viewDataBinding.setVariable(BR.movie,movie);
            /*String title = StringUtils.formatMovieTitle(holder.mContext, movie.getTitle());
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

            holder.card_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(holder.mContext, holder.card_menu);
                    MenuInflater inflater = popup.getMenuInflater();
                    int menuResourceId = (movie.isFavourite()) ? R.menu.menu_remove_movie : R.menu.menu_add_movie;
                    inflater.inflate(menuResourceId, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (movie.isFavourite()) {
                                mMoviesItemListener.onUnmarAsFavorite(movie.getApi_movie_id());
                            } else {
                                mMoviesItemListener.onMarkAsFavorite(movie);
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            });

            holder.movie_rate.setText(movie.getVote_average().toString());

            holder.movie_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMoviesItemListener.onClick(movie);
                }
            });*/
        }else{
            ProgressBarHolder holder = (ProgressBarHolder) viewHolder;
//            holder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (mMovieDBArrayList.get(position) != null) ? VIEW_ITEM : PROGRESS_BAR;
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

    public void clear() {
        mMovieDBArrayList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addItem(Movie movie){
        mMovieDBArrayList.add(movie);
        notifyDataSetChanged();
    }
}