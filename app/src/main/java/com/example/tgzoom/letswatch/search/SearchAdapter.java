package com.example.tgzoom.letswatch.search;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.tgzoom.letswatch.BR;
import com.example.tgzoom.letswatch.R;
import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.databinding.FragmentMoviesListItemBinding;
import com.example.tgzoom.letswatch.databinding.FragmentSearchListItemBinding;
import com.example.tgzoom.letswatch.listener.MoviesItemListener;
import com.example.tgzoom.letswatch.util.URIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tgzoom on 21/03/17.
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Movie> mMovieDBArrayList = new ArrayList<Movie>();
    private MoviesItemListener mMoviesItemListener;
    public static final int VIEW_ITEM = 0;
    public static final int PROGRESS_BAR = 1;


    public SearchAdapter(MoviesItemListener moviesItemListener) {
        mMoviesItemListener = moviesItemListener;
    }


    public void swapArrayList(List<Movie> movies) {
        mMovieDBArrayList = movies;
        notifyDataSetChanged();
    }

    public static class ProgressBarHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_list_progress_bar)
        ProgressBar progressBar;
        Context mContext;

        public ProgressBarHolder(View view, final Context context) {
            super(view);
            ButterKnife.bind(this, view);
            mContext = context;
        }
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

    public List<Movie> getArrayList() {
        return mMovieDBArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        if (viewType == VIEW_ITEM) {
            FragmentSearchListItemBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.fragment_search_list_item, parent, false);
            viewHolder = new SearchAdapter.MovieHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_list_item, parent, false);
            viewHolder = new SearchAdapter.ProgressBarHolder(view, parent.getContext());
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof SearchAdapter.MovieHolder) {
            final Movie movie = mMovieDBArrayList.get(position);
            ViewDataBinding viewDataBinding = ((SearchAdapter.MovieHolder) viewHolder).getViewDataBinding();
            viewDataBinding.setVariable(BR.movie, movie);
            viewDataBinding.setVariable(BR.movieListener, mMoviesItemListener);
            viewDataBinding.executePendingBindings();
        }else{
            SearchAdapter.ProgressBarHolder holder = (SearchAdapter.ProgressBarHolder) viewHolder;
            holder.progressBar.setIndeterminate(true);
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

    public void onCardMenuClick(final View view, final Movie movie) {
        if (movie.isFavourite()) {
            mMoviesItemListener.onUnmarAsFavorite(movie.getApi_movie_id());
        } else {
            mMoviesItemListener.onMarkAsFavorite(movie);
        }
    }

    @BindingAdapter("bind:cardImage")
    public static void loadCardImage(ImageView cardImage, String posterPath){
        if(posterPath != null){
            String url = URIUtils.buildPosterPathW45(posterPath).toString();
            Glide.with(cardImage.getContext())
                    .load(url)
                    .placeholder(R.color.colorPrimary)
                    .into(cardImage);
        }
    }

    @BindingAdapter("bind:imageButton")
    public static void loadImageButton(ImageView imageButton, boolean isFavourite){
        int imageDrawableId = (isFavourite) ? R.drawable.ic_heart_black_36dp : R.drawable.ic_heart_outline_grey600_36dp;
        imageButton.setImageResource(imageDrawableId);
    }
}
