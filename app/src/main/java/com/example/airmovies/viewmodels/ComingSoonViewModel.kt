package com.example.airmovies.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airmovies.model.movie.MoviesList
import com.example.airmovies.model.movie.MoviesResult
import com.example.airmovies.model.tv.TvShowsList
import com.example.airmovies.model.tv.TvShowsResult
import com.example.airmovies.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ComingSoonViewModel : ViewModel() {

    private val upcomingMoviesLiveData = MutableLiveData<List<MoviesResult>>()
    private val upcomingTvShowsLiveData = MutableLiveData<List<TvShowsResult>>()

    fun getUpcomingMovies() {
        RetrofitInstance.api.getComingSoonMovies().enqueue(object : Callback<MoviesList> {
            override fun onResponse(call: Call<MoviesList>, response: Response<MoviesList>) {
                if (response.body() != null) {
                    upcomingMoviesLiveData.value = response.body()!!.results
                }
            }

            override fun onFailure(call: Call<MoviesList>, t: Throwable) {
                Log.e("ComingSoonViewModel", t.message.toString())
            }
        })
    }

    fun getUpcomingTvShows() {
        RetrofitInstance.api.getComingSoonTvShows().enqueue(object : Callback<TvShowsList> {
            override fun onResponse(call: Call<TvShowsList>, response: Response<TvShowsList>) {
                if (response.body() != null) {
                    upcomingTvShowsLiveData.value = response.body()!!.results
                }
            }

            override fun onFailure(call: Call<TvShowsList>, t: Throwable) {
                Log.e("ComingSoonViewModel", t.message.toString())
            }
        })
    }

    fun observeUpcomingMoviesLiveData(): LiveData<List<MoviesResult>> {
        return upcomingMoviesLiveData
    }

    fun observeUpcomingTvShowsLiveData(): LiveData<List<TvShowsResult>> {
        return upcomingTvShowsLiveData
    }

}