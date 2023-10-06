package com.example.airmovies.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airmovies.model.movie.PopularMoviesList
import com.example.airmovies.model.movie.PopularMoviesResult
import com.example.airmovies.model.movie.RecentMoviesList
import com.example.airmovies.model.movie.RecentMoviesResult
import com.example.airmovies.model.movie.TopRatedMoviesList
import com.example.airmovies.model.movie.TopRatedMoviesResult
import com.example.airmovies.model.tv.PopularTvShowsList
import com.example.airmovies.model.tv.PopularTvShowsResult
import com.example.airmovies.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel: ViewModel() {
    private var popularMoviesLiveData = MutableLiveData<List<PopularMoviesResult>>()
    private var popularTvShowsLiveData = MutableLiveData<List<PopularTvShowsResult>>()
    private var recentMoviesLiveData = MutableLiveData<List<RecentMoviesResult>>()
    private var topRatedMoviesLiveData= MutableLiveData<List<TopRatedMoviesResult>>()

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
                Log.e("HomeViewModel", t.message.toString())
            }
        })
    }

    fun getPopularTvShows() {
        RetrofitInstance.api.getPopularTvShows().enqueue(object : Callback<PopularTvShowsList> {
            override fun onResponse(
                call: Call<PopularTvShowsList>,
                response: Response<PopularTvShowsList>
            ) {
                if (response.body() != null) {
                    popularTvShowsLiveData.value = response.body()?.results
                }
            }

            override fun onFailure(call: Call<PopularTvShowsList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }
        })
    }

    fun getRecentMovies() {
        RetrofitInstance.api.getRecentMovies().enqueue(object : Callback<RecentMoviesList> {
            override fun onResponse(
                call: Call<RecentMoviesList>,
                response: Response<RecentMoviesList>
            ) {
                if (response.body() != null) {
                    recentMoviesLiveData.value = response.body()!!.results
                }
            }

            override fun onFailure(call: Call<RecentMoviesList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }
        })
    }

    fun getTopRatedMovies() {
        RetrofitInstance.api.getTopRatedMovies().enqueue(object : Callback<TopRatedMoviesList> {
            override fun onResponse(
                call: Call<TopRatedMoviesList>,
                response: Response<TopRatedMoviesList>
            ) {
                if (response.body() != null) {
                    topRatedMoviesLiveData.value = response.body()!!.results
                }
            }

            override fun onFailure(call: Call<TopRatedMoviesList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }
        })
    }

    fun observePopularMoviesLiveData(): LiveData<List<PopularMoviesResult>> {
        return popularMoviesLiveData
    }

    fun observePopularTvShowsLiveData(): LiveData<List<PopularTvShowsResult>> {
        return popularTvShowsLiveData
    }

    fun observeRecentMoviesLiveData(): LiveData<List<RecentMoviesResult>> {
        return recentMoviesLiveData
    }

    fun observeTopRatedMoviesLiveData(): LiveData<List<TopRatedMoviesResult>> {
        return topRatedMoviesLiveData
    }
}