package com.example.airmovies.retrofit

import com.example.airmovies.model.movie.MovieCredits
import com.example.airmovies.model.movie.MovieDetails
import com.example.airmovies.model.movie.MoviesList
import com.example.airmovies.model.tv.TvShowCredits
import com.example.airmovies.model.tv.TvShowDetails
import com.example.airmovies.model.tv.TvShowsList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieApi {

    @GET("3/movie/popular?api_key=63b48d32b10c0628025ebbefd4a3b8c6")
    fun getPopularMovies(): Call<MoviesList>

    @GET("3/movie/now_playing?api_key=63b48d32b10c0628025ebbefd4a3b8c6")
    fun getRecentMovies(): Call<MoviesList>

    @GET("3/movie/top_rated?api_key=63b48d32b10c0628025ebbefd4a3b8c6")
    fun getTopRatedMovies(): Call<MoviesList>

    @GET("3/movie/{movie_id}/similar?api_key=63b48d32b10c0628025ebbefd4a3b8c6")
    fun getSimilarMovies(@Path("movie_id") movieId: String): Call<MoviesList>

    @GET("3/movie/{movie_id}/credits?api_key=63b48d32b10c0628025ebbefd4a3b8c6")
    fun getMovieCredits(@Path("movie_id") movieId: String): Call<MovieCredits>

    @GET("3/movie/{movie_id}?api_key=63b48d32b10c0628025ebbefd4a3b8c6")
    fun getMovieDetails(@Path("movie_id") movieId: String): Call<MovieDetails>



    @GET("3/tv/popular?api_key=63b48d32b10c0628025ebbefd4a3b8c6")
    fun getPopularTvShows(): Call<TvShowsList>

    @GET("3/tv/top_rated?api_key=63b48d32b10c0628025ebbefd4a3b8c6")
    fun getTopRatedTvShows(): Call<TvShowsList>

    @GET("3/tv/{series_id}/similar?api_key=63b48d32b10c0628025ebbefd4a3b8c6")
    fun getSimilarTvShows(@Path("series_id") seriesId: String): Call<TvShowsList>

    @GET("3/tv/{series_id}/credits?api_key=63b48d32b10c0628025ebbefd4a3b8c6")
    fun getTvShowCredits(@Path("series_id") seriesId: String): Call<TvShowCredits>

    @GET("3/tv/{series_id}?api_key=63b48d32b10c0628025ebbefd4a3b8c6")
    fun getTvShowDetails(@Path("series_id") seriesId: String): Call<TvShowDetails>

}