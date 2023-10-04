package com.example.airmovies.model.movie


import com.google.gson.annotations.SerializedName

data class PopularMoviesList(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<PopularMoviesResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)