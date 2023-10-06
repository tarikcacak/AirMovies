package com.example.airmovies.model.movie


import com.google.gson.annotations.SerializedName

data class TopRatedMoviesList(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<TopRatedMoviesResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)