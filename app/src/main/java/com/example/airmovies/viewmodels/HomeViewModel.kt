package com.example.airmovies.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airmovies.model.movie.PopularMoviesList
import com.example.airmovies.model.movie.PopularMoviesResult
import com.example.airmovies.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel: ViewModel() {
    private var popularMoviesLiveData = MutableLiveData<List<PopularMoviesResult>>()

    fun getPopularMovies() {
        RetrofitInstance.api.getPopularMovies().enqueue(object : Callback<PopularMoviesList> {
            override fun onResponse(
                call: Call<PopularMoviesList>,
                response: Response<PopularMoviesList>
            ) {
                if (response.body() != null) {
                    popularMoviesLiveData.value = response.body()!!.results
                }
            }

            override fun onFailure(call: Call<PopularMoviesList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }
        })
    }

    fun observePopularMoviesLiveData(): LiveData<List<PopularMoviesResult>> {
        return popularMoviesLiveData
    }
}