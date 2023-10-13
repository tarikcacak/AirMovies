package com.example.airmovies.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airmovies.model.actor.ActorDetails
import com.example.airmovies.model.actor.ActorMovieList
import com.example.airmovies.model.actor.ActorMovieResult
import com.example.airmovies.model.movie.MoviesList
import com.example.airmovies.model.movie.MoviesResult
import com.example.airmovies.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActorViewModel : ViewModel() {

    private val actorDetailsLiveData = MutableLiveData<ActorDetails>()
    private val actorFilmographyLiveData = MutableLiveData<List<ActorMovieResult>>()

    fun getActorDetails(actorId: String) {
        RetrofitInstance.api.getActorDetails(actorId).enqueue(object : Callback<ActorDetails> {
            override fun onResponse(call: Call<ActorDetails>, response: Response<ActorDetails>) {
                if (response.body() != null) {
                    actorDetailsLiveData.value = response.body()
                }
            }

            override fun onFailure(call: Call<ActorDetails>, t: Throwable) {
                Log.e("ActorsViewModel", t.message.toString())
            }
        })
    }

    fun getActorFilmography(actorId: String) {
        RetrofitInstance.api.getActorFilmography(actorId).enqueue(object : Callback<ActorMovieList> {
            override fun onResponse(
                call: Call<ActorMovieList>,
                response: Response<ActorMovieList>
            ) {
                if (response.body() != null) {
                    actorFilmographyLiveData.value = response.body()!!.cast
                }
            }

            override fun onFailure(call: Call<ActorMovieList>, t: Throwable) {
                Log.e("ActorsViewModel", t.message.toString())
            }
        })
    }

    fun observeActorDetailsLiveData(): LiveData<ActorDetails> {
        return actorDetailsLiveData
    }

    fun observeActorFilmographyLiveData(): LiveData<List<ActorMovieResult>> {
        return actorFilmographyLiveData
    }

}