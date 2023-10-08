package com.example.airmovies.model.movie


import com.google.gson.annotations.SerializedName

data class MovieCredits(
    @SerializedName("cast")
    val cast: List<MovieCast>,
    @SerializedName("id")
    val id: Int
)