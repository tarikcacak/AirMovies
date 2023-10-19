package com.example.airmovies.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airmovies.model.actor.PopularActorList
import com.example.airmovies.model.actor.PopularActorResult
import com.example.airmovies.model.movie.MoviesList
import com.example.airmovies.model.movie.MoviesResult
import com.example.airmovies.model.tv.TvShowsList
import com.example.airmovies.model.tv.TvShowsResult
import com.example.airmovies.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {

    private val searchMoviesLiveData = MutableLiveData<List<MoviesResult>>()
    private val searchTvShowsLiveData = MutableLiveData<List<TvShowsResult>>()
    private val searchActorsLiveData = MutableLiveData<List<PopularActorResult>>()

    fun getSearchMovies(searchQuery: String) {
        RetrofitInstance.api.getSearchMovies(searchQuery).enqueue(object : Callback<MoviesList> {
            override fun onResponse(call: Call<MoviesList>, response: Response<MoviesList>) {
                if (response.body() != null) {
                    searchMoviesLiveData.value = response.body()!!.results
                }
            }

            override fun onFailure(call: Call<MoviesList>, t: Throwable) {
                Log.e("SearchViewModel", t.message.toString())
            }
        })
    }

    fun getSearchTvShows(searchQuery: String) {
        RetrofitInstance.api.getSearchTvShows(searchQuery).enqueue(object : Callback<TvShowsList> {
            override fun onResponse(call: Call<TvShowsList>, response: Response<TvShowsList>) {
                if (response.body() != null) {
                    searchTvShowsLiveData.value = response.body()!!.results
                }
            }

            override fun onFailure(call: Call<TvShowsList>, t: Throwable) {
                Log.e("SearchViewModel", t.message.toString())
            }
        })
    }

    fun getSearchActors(searchQuery: String) {
        RetrofitInstance.api.getSearchActors(searchQuery).enqueue(object : Callback<PopularActorList> {
            override fun onResponse(
                call: Call<PopularActorList>,
                response: Response<PopularActorList>
            ) {
                searchActorsLiveData.value = response.body()!!.results
            }

            override fun onFailure(call: Call<PopularActorList>, t: Throwable) {
                Log.e("SearchViewModel", t.message.toString())
            }
        })
    }

    fun observeSearchMoviesLiveData(): LiveData<List<MoviesResult>> {
        return searchMoviesLiveData
    }

    fun observeSearchTvShowsLiveData(): LiveData<List<TvShowsResult>> {
        return searchTvShowsLiveData
    }

    fun observeSearchActorsLiveData(): LiveData<List<PopularActorResult>> {
        return searchActorsLiveData
    }
}