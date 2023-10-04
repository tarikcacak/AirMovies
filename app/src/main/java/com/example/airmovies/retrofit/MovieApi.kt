package com.example.airmovies.retrofit

import com.example.airmovies.model.movie.PopularMoviesList
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface MovieApi {

    @GET("3/movie/popular?api_key=63b48d32b10c0628025ebbefd4a3b8c6")
    fun getPopularMovies(): Call<PopularMoviesList>


}