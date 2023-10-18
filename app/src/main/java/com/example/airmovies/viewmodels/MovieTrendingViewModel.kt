package com.example.airmovies.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airmovies.model.movie.MoviesList
import com.example.airmovies.model.movie.MoviesResult
import com.example.airmovies.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieTrendingViewModel : ViewModel() {

    private val trendingMoviesLiveData = MutableLiveData<List<MoviesResult>>()

    fun getTrendingMovies() {
        RetrofitInstance.api.getTrendingMovies().enqueue(object : Callback<MoviesList> {
            override fun onResponse(call: Call<MoviesList>, response: Response<MoviesList>) {
                if (response.body() != null) {
                    trendingMoviesLiveData.value = response.body()!!.results
                }
            }

            override fun onFailure(call: Call<MoviesList>, t: Throwable) {
                Log.e("MovieTrendingViewModel", t.message.toString())
            }
        })
    }

    fun observeTrendingMoviesLiveData(): LiveData<List<MoviesResult>> {
        return trendingMoviesLiveData
    }
}