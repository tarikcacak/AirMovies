package com.example.airmovies.model.movie


import com.google.gson.annotations.SerializedName

data class MovieCredits(
    @SerializedName("cast")
    val cast: List<Cast>,
    @SerializedName("id")
    val id: Int
)