package com.example.airmovies.model.movie


import com.google.gson.annotations.SerializedName

data class PopularMoviesList(
    @SerializedName("results")
    val results: List<PopularMoviesResult>
)