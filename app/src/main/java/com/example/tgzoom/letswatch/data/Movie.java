package com.example.tgzoom.letswatch.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tgzoom on 12/27/16.
 */

public class Movie implements Parcelable {
    @SerializedName("results")
    @Expose
    private List<Movie> movies;

    @SerializedName("title")
    private String title;
    @SerializedName("original_title")
    private String original_title;
    @SerializedName("poster_path")
    private String poster_path;
    @SerializedName("backdrop_path")
    private String backdrop_path;
    @SerializedName("vote_count")
    private int vote_count;
    @SerializedName("release_date")
    private String release_date;
    @SerializedName("vote_average")
    private Double vote_average;
    @SerializedName("overview")
    private String overview;
    @SerializedName("id")
    private int api_movie_id;
    @SerializedName("movie_id")
    private int id;
    @SerializedName("popularity")
    private Double popularity;

    private boolean isFavourite = false;

    protected Movie(Parcel in) {
        title = in.readString();
        original_title = in.readString();
        poster_path = in.readString();
        backdrop_path = in.readString();
        release_date = in.readString();
        overview = in.readString();
        vote_count = in.readInt();
        vote_average = in.readDouble();
        api_movie_id = in.readInt();
        popularity = in.readDouble();
        isFavourite = in.readByte() != 0;
        id = in.readInt();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(original_title);
        parcel.writeString(poster_path);
        parcel.writeString(backdrop_path);
        parcel.writeString(release_date);
        parcel.writeString(overview);
        parcel.writeInt(vote_count);
        parcel.writeDouble(vote_average);
        parcel.writeInt(api_movie_id);
        parcel.writeDouble(popularity);
        parcel.writeByte(isFavourite?(byte) 1 : (byte)0);
        parcel.writeInt(id);
    }

    public class Results{
        @Expose
        @SerializedName("results")
        public List<Movie> movies = new ArrayList<>();
    }

    public Movie(){

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setApi_movie_id(int api_movie_id) {
        this.api_movie_id = api_movie_id;
    }

    public int getApi_movie_id() {
        return api_movie_id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public List<Movie> getMovies() { return movies; }

    public void setMovies(List<Movie> movies) { this.movies = movies; }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favorite) {
        isFavourite = favorite;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }
}
