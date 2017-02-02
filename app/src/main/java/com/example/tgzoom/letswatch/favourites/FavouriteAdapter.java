package com.example.tgzoom.letswatch.favourites;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.tgzoom.letswatch.BR;
import com.example.tgzoom.letswatch.R;
import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.listener.MoviesItemListener;
import com.example.tgzoom.letswatch.util.URIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tgzoom on 1/27/17.
 */

public class FavouriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Movie> mMovieDBArrayList = new ArrayList<Movie>();
    private MoviesItemListener mMoviesItemListener;
    public static final int ITEM_VIEW = 0;
    public static final int NO_ITEMS = 1;

    public FavouriteAdapter(MoviesItemListener moviesItemListener) {
        mMoviesItemListener = moviesItemListener;
    }

    public static class MovieHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding mViewDataBinding;

        public MovieHolder(ViewDataBinding view) {
            super(view.getRoot());
            mViewDataBinding = view;
        }

        public ViewDataBinding getViewDataBinding() {
            return mViewDataBinding;
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(View view) {
            super(view);
        }
    }

    public List<Movie> getArrayList() {
        return mMovieDBArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        if (viewType == NO_ITEMS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_not_found, parent, false);
            viewHolder = new EmptyViewHolder(view);
        } else {
            ViewDataBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.fragment_movies_list_item, parent, false);
            viewHolder = new MovieHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof MovieHolder) {
            final Movie movie = mMovieDBArrayList.get(position);
            ViewDataBinding viewDataBinding = ((MovieHolder) viewHolder).getViewDataBinding();
            viewDataBinding.setVariable(BR.movie, movie);
            viewDataBinding.setVariable(BR.movieListener, mMoviesItemListener);
            viewDataBinding.executePendingBindings();
        }
    }

    public void swapArrayList(List<Movie> movies) {
        mMovieDBArrayList = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return (mMovieDBArrayList.size() == 0 ? NO_ITEMS : ITEM_VIEW);
    }

    @Override
    public int getItemCount() {
        return mMovieDBArrayList.size() > 0 ? mMovieDBArrayList.size() : 1;
    }

    @Override
    public long getItemId(int position) {
        return mMovieDBArrayList.get(position).getApi_movie_id();
    }

    public void clear() {
        mMovieDBArrayList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addItem(Movie movie) {
        mMovieDBArrayList.add(movie);
        notifyDataSetChanged();
    }

    public void onCardMenuClick(final View view, final Movie movie) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
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

    @BindingAdapter("bind:cardImage")
    public static void loadCardImage(ImageView cardImage,String posterPath){
        if(posterPath != null){
            String url = URIUtils.buildPosterPath(posterPath).toString();
            Glide.with(cardImage.getContext())
                    .load(url)
                    .centerCrop()
                    .placeholder(R.color.colorPrimary)
                    .into(cardImage);
        }
    }
}