package com.example.airmovies.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airmovies.data.movie.MovieWatchlist
import com.example.airmovies.data.tv.TvWatchlist
import com.example.airmovies.model.movie.MovieCast
import com.example.airmovies.model.movie.MovieCredits
import com.example.airmovies.model.movie.MovieDetails
import com.example.airmovies.model.movie.MoviesList
import com.example.airmovies.model.movie.MoviesResult
import com.example.airmovies.model.tv.TvShowCast
import com.example.airmovies.model.tv.TvShowCredits
import com.example.airmovies.model.tv.TvShowDetails
import com.example.airmovies.retrofit.RetrofitInstance
import com.example.airmovies.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
    ): ViewModel() {

    private val movieCreditsLiveData = MutableLiveData<List<MovieCast>>()
    private val similarMoviesLiveData = MutableLiveData<List<MoviesResult>>()
    private val movieDetailsLiveData = MutableLiveData<MovieDetails>()

    private val tvCreditsLiveData = MutableLiveData<TvShowCredits>()
    private val similarTvLiveData = MutableLiveData<List<MoviesResult>>()
    private val tvDetailsLiveData = MutableLiveData<TvShowDetails>()

    private val _watchlistMovie = MutableStateFlow<Resource<MovieWatchlist>>(Resource.Unspecified())
    val watchlistMovie: Flow<Resource<MovieWatchlist>> = _watchlistMovie

    private val _watchlistTv = MutableStateFlow<Resource<TvWatchlist>>(Resource.Unspecified())
    val watchlistTv: Flow<Resource<TvWatchlist>> = _watchlistTv

    private var movieExistLiveData = MutableLiveData<Boolean>()
    private var tvExistLiveData = MutableLiveData<Boolean>()

    val currentUid = firebaseAuth.currentUser?.uid.toString()

    private var movieArrayList: ArrayList<MovieWatchlist>? = null
    private var tvArrayList: ArrayList<TvWatchlist>? = null

    fun getMovieCredits(movieId: String) {
        RetrofitInstance.api.getMovieCredits(movieId).enqueue(object : Callback<MovieCredits> {
            override fun onResponse(call: Call<MovieCredits>, response: Response<MovieCredits>) {
                if (response.body() != null) {
                    movieCreditsLiveData.value = response.body()!!.cast
                }
            }

            override fun onFailure(call: Call<MovieCredits>, t: Throwable) {
                Log.e("MovieDetailsViewModel", t.message.toString())
            }
        })
    }

    fun getSimilarMovies(movieId: String) {
        RetrofitInstance.api.getSimilarMovies(movieId).enqueue(object : Callback<MoviesList> {
            override fun onResponse(call: Call<MoviesList>, response: Response<MoviesList>) {
                if (response.body() != null) {
                    similarMoviesLiveData.value = response.body()!!.results
                }
            }

            override fun onFailure(call: Call<MoviesList>, t: Throwable) {
                Log.e("MovieDetailsViewModel", t.message.toString())
            }
        })
    }

    fun getMovieDetailsLiveData(movieId: String) {
        RetrofitInstance.api.getMovieDetails(movieId).enqueue(object : Callback<MovieDetails> {
            override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
                if (response.body() != null) {
                    movieDetailsLiveData.value = response.body()
                }
            }

            override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                Log.e("MovieDetailsViewModel", t.message.toString())
            }
        })
    }

    fun observeMovieCreditsLiveData(): LiveData<List<MovieCast>> {
        return movieCreditsLiveData
    }

    fun observeSimilarMoviesLiveData(): LiveData<List<MoviesResult>> {
        return similarMoviesLiveData
    }

    fun observeMovieDetailsLiveDats(): LiveData<MovieDetails> {
        return movieDetailsLiveData
    }
}