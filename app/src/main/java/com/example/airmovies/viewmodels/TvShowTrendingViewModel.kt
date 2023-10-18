package com.example.airmovies.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airmovies.model.tv.TvShowsList
import com.example.airmovies.model.tv.TvShowsResult
import com.example.airmovies.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TvShowTrendingViewModel : ViewModel() {

    private val trendingTvShowLiveData = MutableLiveData<List<TvShowsResult>>()

    fun getTrendingTvShows() {
        RetrofitInstance.api.getTrendingTvShows().enqueue(object : Callback<TvShowsList> {
            override fun onResponse(call: Call<TvShowsList>, response: Response<TvShowsList>) {
                if (response.body() != null) {
                    trendingTvShowLiveData.value = response.body()!!.results
                }
            }

            override fun onFailure(call: Call<TvShowsList>, t: Throwable) {
                Log.e("TvShowTrendingViewModel", t.message.toString())
            }
        })
    }

    fun observeTrendingTvShowLiveData(): LiveData<List<TvShowsResult>> {
        return trendingTvShowLiveData
    }

}