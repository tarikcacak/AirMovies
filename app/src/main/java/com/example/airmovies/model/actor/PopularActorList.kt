package com.example.airmovies.model.actor


import com.google.gson.annotations.SerializedName

data class PopularActorList(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<PopularActorResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)