package com.example.airmovies.model.actor


import com.google.gson.annotations.SerializedName

data class ActorMovieList(
    @SerializedName("cast")
    val cast: List<ActorMovieResult>
)