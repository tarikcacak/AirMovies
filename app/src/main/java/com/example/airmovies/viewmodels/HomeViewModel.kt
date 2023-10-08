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

class HomeViewModel: ViewModel() {
    private var popularMoviesLiveData = MutableLiveData<List<MoviesResult>>()
    private var popularTvShowsLiveData = MutableLiveData<List<TvShowsResult>>()
    private var recentMoviesLiveData = MutableLiveData<List<MoviesResult>>()
    private var topRatedMoviesLiveData = MutableLiveData<List<MoviesResult>>()
    private var topRatedTvShowsLiveData = MutableLiveData<List<TvShowsResult>>()

    fun getPopularMovies() {
        RetrofitInstance.api.getPopularMovies().enqueue(object : Callback<MoviesList> {
            override fun onResponse(
                call: Call<MoviesList>,
                response: Response<MoviesList>
            ) {
                if (response.body() != null) {
                    popularMoviesLiveData.value = response.body()!!.results
                }
            }

            override fun onFailure(call: Call<MoviesList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }
        })
    }

    fun getPopularTvShows() {
        RetrofitInstance.api.getPopularTvShows().enqueue(object : Callback<TvShowsList> {
            override fun onResponse(
                call: Call<TvShowsList>,
                response: Response<TvShowsList>
            ) {
                if (response.body() != null) {
                    popularTvShowsLiveData.value = response.body()?.results
                }
            }

            override fun onFailure(call: Call<TvShowsList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }
        })
    }

    fun getRecentMovies() {
        RetrofitInstance.api.getRecentMovies().enqueue(object : Callback<MoviesList> {
            override fun onResponse(
                call: Call<MoviesList>,
                response: Response<MoviesList>
            ) {
                if (response.body() != null) {
                    recentMoviesLiveData.value = response.body()!!.results
                }
            }

            override fun onFailure(call: Call<MoviesList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }
        })
    }

    fun getTopRatedMovies() {
        RetrofitInstance.api.getTopRatedMovies().enqueue(object : Callback<MoviesList> {
            override fun onResponse(
                call: Call<MoviesList>,
                response: Response<MoviesList>
            ) {
                if (response.body() != null) {
                    topRatedMoviesLiveData.value = response.body()!!.results
                }
            }

            override fun onFailure(call: Call<MoviesList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }
        })
    }

    fun getTopRatedTvShows() {
        RetrofitInstance.api.getTopRatedTvShows().enqueue(object : Callback<TvShowsList> {
            override fun onResponse(
                call: Call<TvShowsList>,
                response: Response<TvShowsList>
            ) {
                if (response.body() != null) {
                    topRatedTvShowsLiveData.value = response.body()!!.results
                }
            }

            override fun onFailure(call: Call<TvShowsList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }
        })
    }

    fun observePopularMoviesLiveData(): LiveData<List<MoviesResult>> {
        return popularMoviesLiveData
    }

    fun observePopularTvShowsLiveData(): LiveData<List<TvShowsResult>> {
        return popularTvShowsLiveData
    }

    fun observeRecentMoviesLiveData(): LiveData<List<MoviesResult>> {
        return recentMoviesLiveData
    }

    fun observeTopRatedMoviesLiveData(): LiveData<List<MoviesResult>> {
        return topRatedMoviesLiveData
    }

    fun observeTopRatedTvShowsLiveData(): LiveData<List<TvShowsResult>> {
        return topRatedTvShowsLiveData
    }
}