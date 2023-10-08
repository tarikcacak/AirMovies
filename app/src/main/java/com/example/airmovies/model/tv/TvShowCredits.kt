package com.example.airmovies.model.tv


import com.google.gson.annotations.SerializedName

data class TvShowCredits(
    @SerializedName("cast")
    val cast: List<TvShowCast>,
    @SerializedName("id")
    val id: Int
)