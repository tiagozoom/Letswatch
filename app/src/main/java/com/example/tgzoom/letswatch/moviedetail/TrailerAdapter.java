package com.example.tgzoom.letswatch.moviedetail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tgzoom.letswatch.R;
import com.example.tgzoom.letswatch.data.Trailer;
import com.example.tgzoom.letswatch.util.URIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by tgzoom on 11/10/16.
 */
public class TrailerAdapter extends BaseAdapter {

    @BindView(R.id.trailers_container)
    ViewGroup mTrailerViewGroup;

    private List<Trailer> mTrailerList = new ArrayList<>();

    private Context mContext;

    private LayoutInflater mIinflater;

    private final static int HEADER_ITEM = 0;

    private final static int NORMAL_ITEM = 1;

    private MovieDetailListener mMovieDetailListener;

    public TrailerAdapter(Context context, MovieDetailListener movieDetailListener) {
        mContext = context;
        mIinflater = LayoutInflater.from(context);
        mMovieDetailListener = movieDetailListener;
    }

    @Override
    public int getCount() {
        return mTrailerList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTrailerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Trailer trailer = mTrailerList.get(position);

        if (HEADER_ITEM == getItemViewType(position)) {
            setHeaderItem(convertView,parent);
        }

        convertView = mIinflater.inflate(R.layout.fragment_detail_trailer_list_item, parent, false);


        TextView trailerName = (TextView) convertView.findViewById(R.id.trailer_name);
        ViewGroup item_container = (ViewGroup) convertView.findViewById(R.id.item_container);

        trailerName.setText(trailer.getName());
        item_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMovieDetailListener.onTrailerClick(trailer.getKey());
            }
        });

        return convertView;
    }

    private void setHeaderItem(View convertView, ViewGroup parent) {
        convertView = mIinflater.inflate(R.layout.fragment_detail_trailer_first_list_item, parent, true);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == HEADER_ITEM) ? HEADER_ITEM : NORMAL_ITEM;
    }

    public void swapArrayList(List<Trailer> trailerList) {
        if (trailerList != null) {
            mTrailerList = trailerList;
            notifyDataSetChanged();
        }
    }

    public List<Trailer> getList() {
        return mTrailerList;
    }
}
