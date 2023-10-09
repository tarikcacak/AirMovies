package com.example.airmovies.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airmovies.data.movie.MovieFirebase
import com.example.airmovies.data.movie.MovieWatchlist
import com.example.airmovies.data.tv.TvFirebase
import com.example.airmovies.data.tv.TvWatchlist
import com.example.airmovies.model.movie.MovieCast
import com.example.airmovies.model.movie.MovieCredits
import com.example.airmovies.model.movie.MovieDetails
import com.example.airmovies.model.movie.MoviesList
import com.example.airmovies.model.movie.MoviesResult
import com.example.airmovies.model.tv.TvShowCast
import com.example.airmovies.model.tv.TvShowCredits
import com.example.airmovies.model.tv.TvShowDetails
import com.example.airmovies.model.tv.TvShowsList
import com.example.airmovies.model.tv.TvShowsResult
import com.example.airmovies.retrofit.RetrofitInstance
import com.example.airmovies.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
    ): ViewModel() {

    private val movieCreditsLiveData = MutableLiveData<List<MovieCast>>()
    private val similarMoviesLiveData = MutableLiveData<List<MoviesResult>>()
    private val movieDetailsLiveData = MutableLiveData<MovieDetails>()

    private val tvCreditsLiveData = MutableLiveData<List<TvShowCast>>()
    private val similarTvLiveData = MutableLiveData<List<TvShowsResult>>()
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

    fun getMovieDetails(movieId: String) {
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

    fun getTvCredits(seriesId: String) {
        RetrofitInstance.api.getTvShowCredits(seriesId).enqueue(object : Callback<TvShowCredits> {
            override fun onResponse(call: Call<TvShowCredits>, response: Response<TvShowCredits>) {
                if (response.body() != null) {
                    tvCreditsLiveData.value = response.body()!!.cast
                }
            }

            override fun onFailure(call: Call<TvShowCredits>, t: Throwable) {
                Log.e("MovieDetailsViewModel", t.message.toString())
            }
        })
    }

    fun getSimilarTv(seriesId: String) {
        RetrofitInstance.api.getSimilarTvShows(seriesId).enqueue(object : Callback<TvShowsList> {
            override fun onResponse(call: Call<TvShowsList>, response: Response<TvShowsList>) {
                if (response.body() != null) {
                    similarTvLiveData.value = response.body()!!.results
                }
            }

            override fun onFailure(call: Call<TvShowsList>, t: Throwable) {
                Log.e("MovieDetailsViewModel", t.message.toString())
            }
        })
    }

    fun getTvDetails(seriesId: String) {
        RetrofitInstance.api.getTvShowDetails(seriesId).enqueue(object : Callback<TvShowDetails> {
            override fun onResponse(call: Call<TvShowDetails>, response: Response<TvShowDetails>) {
                if (response.body() != null) {
                    tvDetailsLiveData.value = response.body()
                }
            }

            override fun onFailure(call: Call<TvShowDetails>, t: Throwable) {
                Log.e("MovieDetailsViewModel", t.message.toString())
            }
        })
    }

    fun saveMovie(movieWatchlist: MovieWatchlist) {

        if (checkIfMovieExists(movieWatchlist)) {

            movieExistLiveData.value = true

            Log.d("MovieDetailsViewModel", "this movie is already in your watchlist")

        } else {

            movieExistLiveData.value = false

            Log.d("MovieDetailsViewModel", "the movie is added to your watchlist")

            val map = mutableMapOf<String, Any>()
            map["movieId"] = movieWatchlist.movieId!!
            map["posterPath"] = movieWatchlist.posterPath!!
            map["title"] = movieWatchlist.title!!
            map["voteAverage"] = movieWatchlist.voteAverage!!

            firestore.collection("watchlist")
                .document(currentUid)
                .update("movie", FieldValue.arrayUnion(map))
                .addOnSuccessListener {
                    _watchlistMovie.value = Resource.Success(movieWatchlist)
                }
                .addOnFailureListener {
                    _watchlistMovie.value = Resource.Error(it.message.toString())
                }
        }
    }

    fun checkIfMovieExists(movieWatchlist: MovieWatchlist): Boolean {
       firestore.collection("watchlist").whereEqualTo("uid", currentUid)
           .addSnapshotListener{ snapshot, _ ->

               if (!snapshot!!.isEmpty) {
                   val documentList = snapshot.documents

                   for (document in documentList) {
                       val movieList: MovieFirebase? = document.toObject(MovieFirebase::class.java)

                       movieArrayList = movieList?.movie

                       Log.d("read1", movieArrayList.toString())

                   }
               }
           }
        if (movieArrayList.isNullOrEmpty()) {
            return false
        } else {

            for (movieLoopCheck in movieArrayList!!) {

                if (movieWatchlist.movieId == movieLoopCheck.movieId) {
                    return true
                }

            }

        }
        return false
    }

    fun saveTv(tvWatchlist: TvWatchlist) {

        if (checkIfTvExists(tvWatchlist)) {

            tvExistLiveData.value = true

            Log.d("MovieDetailsViewModel", "this tv show is already in your watchlist")

        } else {

            tvExistLiveData.value = false

            val map = mutableMapOf<String, Any>()
            map["tvId"] = tvWatchlist.tvId!!
            map["posterPath"] = tvWatchlist.posterPath!!
            map["title"] = tvWatchlist.title!!
            map["voteAverage"] = tvWatchlist.voteAverage!!

            firestore.collection("watchlist")
                .document(currentUid)
                .update("tv", FieldValue.arrayUnion(map))
                .addOnSuccessListener {
                    _watchlistTv.value = Resource.Success(tvWatchlist)
                }
                .addOnFailureListener {
                    _watchlistTv.value = Resource.Error(it.message.toString())
                }
        }
    }

    fun checkIfTvExists(tvWatchlist: TvWatchlist): Boolean {

        firestore.collection("watchlist").whereEqualTo("uid", currentUid)
            .addSnapshotListener { snapshot, _ ->

                if (!snapshot!!.isEmpty) {
                    val documentList = snapshot.documents

                    for (document in documentList) {
                        val tvList: TvFirebase? = document.toObject(TvFirebase::class.java)

                        tvArrayList = tvList?.tv

                        Log.d("read1", movieArrayList.toString())
                    }
                }
            }
        if (tvArrayList.isNullOrEmpty()) {
            return false
        } else {

            for (tvLoopCheck in tvArrayList!!) {
                if (tvWatchlist.tvId == tvLoopCheck.tvId) {
                    return true
                }
            }
        }
        return false
    }

    fun observeMovieCreditsLiveData(): LiveData<List<MovieCast>> {
        return movieCreditsLiveData
    }

    fun observeSimilarMoviesLiveData(): LiveData<List<MoviesResult>> {
        return similarMoviesLiveData
    }

    fun observeMovieDetailsLiveData(): LiveData<MovieDetails> {
        return movieDetailsLiveData
    }

    fun observeTvCreditsLiveData(): LiveData<List<TvShowCast>> {
        return tvCreditsLiveData
    }

    fun observeSimilarTvLiveData(): LiveData<List<TvShowsResult>> {
        return similarTvLiveData
    }

    fun observeTvDetailsLiveData(): LiveData<TvShowDetails> {
        return tvDetailsLiveData
    }

    fun observeMovieExistsLiveData(): LiveData<Boolean> {
        return movieExistLiveData
    }

    fun observeTvExistsLiveData(): LiveData<Boolean> {
        return tvExistLiveData
    }
}
