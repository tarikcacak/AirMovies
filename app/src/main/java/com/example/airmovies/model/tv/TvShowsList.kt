package com.example.airmovies.model.tv


import com.google.gson.annotations.SerializedName

data class TvShowsList(
    @SerializedName("results")
    val results: List<TvShowsResult>
)