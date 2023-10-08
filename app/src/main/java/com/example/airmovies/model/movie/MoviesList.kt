package com.example.airmovies.model.movie


import com.google.gson.annotations.SerializedName

data class MoviesList(
    @SerializedName("results")
    val results: List<MoviesResult>
)