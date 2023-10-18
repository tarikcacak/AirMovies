package com.example.airmovies.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airmovies.model.actor.PopularActorList
import com.example.airmovies.model.actor.PopularActorResult
import com.example.airmovies.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActorTrendingViewModel : ViewModel() {

    private val trendingActorLiveData = MutableLiveData<List<PopularActorResult>>()

    fun getTrendingShows() {
        RetrofitInstance.api.getTrendingActors().enqueue(object : Callback<PopularActorList> {
            override fun onResponse(
                call: Call<PopularActorList>,
                response: Response<PopularActorList>
            ) {
                if (response.body() != null) {
                    trendingActorLiveData.value = response.body()!!.results
                }
            }

            override fun onFailure(call: Call<PopularActorList>, t: Throwable) {
                Log.e("ActorTrendingViewModel", t.message.toString())
            }
        })
    }

    fun observeTrendingActorLiveData(): LiveData<List<PopularActorResult>> {
        return trendingActorLiveData
    }

}