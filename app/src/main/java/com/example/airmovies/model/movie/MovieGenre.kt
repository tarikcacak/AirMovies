package com.example.airmovies.model.movie


import com.google.gson.annotations.SerializedName

data class MovieGenre(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)