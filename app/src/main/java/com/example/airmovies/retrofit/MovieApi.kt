package com.example.airmovies.retrofit

import com.example.airmovies.model.movie.PopularMoviesList
import com.example.airmovies.model.movie.RecentMoviesList
import com.example.airmovies.model.tv.PopularTvShowsList
import retrofit2.Call
import retrofit2.http.GET

interface MovieApi {

    @GET("3/movie/popular?api_key=63b48d32b10c0628025ebbefd4a3b8c6")
    fun getPopularMovies(): Call<PopularMoviesList>

    @GET("3/tv/popular?api_key=63b48d32b10c0628025ebbefd4a3b8c6")
    fun getPopularTvShows(): Call<PopularTvShowsList>

    @GET("3/movie/now_playing?api_key=63b48d32b10c0628025ebbefd4a3b8c6")
    fun getRecentMovies(): Call<RecentMoviesList>
}