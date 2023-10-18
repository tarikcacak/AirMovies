package com.example.airmovies.model.actor


import com.google.gson.annotations.SerializedName

data class ActorMoviesList(
    @SerializedName("cast")
    val cast: List<ActorMoviesResult>
)