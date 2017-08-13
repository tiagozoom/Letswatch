package com.example.tgzoom.letswatch.movies;

import android.content.Context;
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
import android.widget.ProgressBar;
import com.bumptech.glide.Glide;
import com.example.tgzoom.letswatch.BR;
import com.example.tgzoom.letswatch.R;
import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.databinding.FragmentMoviesListItemBinding;
import com.example.tgzoom.letswatch.listener.MoviesItemListener;
import com.example.tgzoom.letswatch.util.URIUtils;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tgzoom on 1/4/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Movie> mMovieDBArrayList;
    private MoviesItemListener mMoviesItemListener;
    public static final int VIEW_ITEM = 0;
    public static final int PROGRESS_BAR = 1;

    public MovieAdapter(List<Movie> movies, MoviesItemListener moviesItemListener) {
        mMovieDBArrayList = movies;
        mMoviesItemListener = moviesItemListener;
    }

    public void swapArrayList(List<Movie> movies) {
        mMovieDBArrayList = movies;
        notifyDataSetChanged();
    }

    public static class MovieHolder extends RecyclerView.ViewHolder
    {
        private ViewDataBinding mViewDataBinding;

        public MovieHolder(ViewDataBinding view) {
            super(view.getRoot());
            mViewDataBinding = view;
        }

        public ViewDataBinding getViewDataBinding() {
            return mViewDataBinding;
        }
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


    public List<Movie> getArrayList() {
        return mMovieDBArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        if (viewType == VIEW_ITEM) {
            FragmentMoviesListItemBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.fragment_movies_list_item, parent, false);
            viewHolder = new MovieHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_list_item, parent, false);
            viewHolder = new ProgressBarHolder(view, parent.getContext());
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
        }else{
            ProgressBarHolder holder = (ProgressBarHolder) viewHolder;
            holder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (mMovieDBArrayList.get(position) != null) ? VIEW_ITEM : PROGRESS_BAR;
    }

    @Override
    public int getItemCount() {
        return mMovieDBArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return mMovieDBArrayList.get(position).getApi_movie_id();
    }

    public void clear() {
        mMovieDBArrayList.clear();
        notifyDataSetChanged();
    }

    public void addItem(Movie movie){
        mMovieDBArrayList.add(movie);
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

    @BindingAdapter("bind:movieCardImage")
    public static void loadMovieCardImage(ImageView cardImage,String posterPath){
        if(posterPath != null){
            String url = URIUtils.buildPosterPath(posterPath).toString();
            Glide.with(cardImage.getContext())
                    .load(url)
                    .centerCrop()
                    .placeholder(R.color.colorPrimary)
                    .error(R.color.colorPrimary)
                    .into(cardImage);
        }
    }
}