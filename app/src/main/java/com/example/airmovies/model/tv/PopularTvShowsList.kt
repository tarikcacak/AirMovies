package com.example.airmovies.model.tv


import com.google.gson.annotations.SerializedName

data class PopularTvShowsList(
    @SerializedName("results")
    val results: List<PopularTvShowsResult>
)