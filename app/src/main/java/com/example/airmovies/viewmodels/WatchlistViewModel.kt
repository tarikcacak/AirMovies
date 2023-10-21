package com.example.airmovies.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airmovies.data.movie.MovieFirebase
import com.example.airmovies.data.movie.MovieWatchlist
import com.example.airmovies.data.tv.TvFirebase
import com.example.airmovies.data.tv.TvWatchlist
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.example.airmovies.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): ViewModel() {

    private val currentUid = firebaseAuth.currentUser?.uid.toString()

    private val loadingStateMovieLiveData = MutableLiveData<Boolean>()
    private val errorStateMovieLiveData = MutableLiveData<String>()
    private var movieListLiveData = MutableLiveData<ArrayList<MovieWatchlist>>()

    private val loadingStateTvLiveData = MutableLiveData<Boolean>()
    private val errorStateTvLiveData = MutableLiveData<String>()
    private var tvListLiveData = MutableLiveData<ArrayList<TvWatchlist>>()

    private var movieDeleteLiveData = MutableLiveData<Resource<MovieWatchlist>>()
    private var tvDeleteLiveData = MutableLiveData<Resource<TvWatchlist>>()

    fun getWatchlistMovie() {

        loadingStateMovieLiveData.value = true

        firestore.collection("watchlist").whereEqualTo("uid", currentUid)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    loadingStateMovieLiveData.value = false
                    errorStateMovieLiveData.value = exception.localizedMessage
                } else {
                    if (!snapshot!!.isEmpty) {
                        val documentList = snapshot.documents
                        for (document in documentList) {
                            val movieList: MovieFirebase? = document.toObject(MovieFirebase::class.java)
                            movieListLiveData.value = movieList?.movie

                            Log.d("dataMovieRead", movieListLiveData.value.toString())
                        }
                    }
                }
            }
    }

    fun getWatchlistTv() {
        loadingStateTvLiveData.value = true

        firestore.collection("watchlist").whereEqualTo("uid", currentUid)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    loadingStateTvLiveData.value = false
                    errorStateTvLiveData.value = exception.localizedMessage
                } else {
                    if (!snapshot!!.isEmpty) {
                        val documentList = snapshot.documents
                        for (document in documentList) {
                            val tvList: TvFirebase? = document.toObject(TvFirebase::class.java)
                            tvListLiveData.value = tvList?.tv

                            Log.d("dataTvRead", tvListLiveData.value.toString())
                        }
                    }
                }
            }
    }

    fun deleteMovie(movieWatchlist: MovieWatchlist) {
        val map = mutableMapOf<String, Any>()
        map["movieId"] = movieWatchlist.movieId!!
        map["posterPath"] = movieWatchlist.posterPath!!
        map["title"] = movieWatchlist.title!!
        map["voteAverage"] = movieWatchlist.voteAverage!!

        firestore.collection("watchlist")
            .document(currentUid)
            .update("movie", FieldValue.arrayRemove(map))
            .addOnSuccessListener {
                movieDeleteLiveData.value = Resource.Success(movieWatchlist)
                Log.d("success",map.toString())
            }
            .addOnFailureListener {
                movieDeleteLiveData.value = Resource.Error(it.message.toString())
                Log.d("error",map.toString())
            }
    }

    fun deleteTv(tvWatchlist: TvWatchlist) {
        val map = mutableMapOf<String, Any>()
        map["tvId"] = tvWatchlist.tvId!!
        map["posterPath"] = tvWatchlist.posterPath!!
        map["title"] = tvWatchlist.title!!
        map["voteAverage"] = tvWatchlist.voteAverage!!

        firestore.collection("watchlist")
            .document(currentUid)
            .update("tv", FieldValue.arrayRemove(map))
            .addOnSuccessListener {
                tvDeleteLiveData.value = Resource.Success(tvWatchlist)
                Log.d("success",map.toString())
            }
            .addOnFailureListener {
                tvDeleteLiveData.value = Resource.Error(it.message.toString())
                Log.d("error", map.toString())
            }
    }

    fun observeMovieLoadingStateLiveData(): LiveData<Boolean> {
        return loadingStateMovieLiveData
    }

    fun observeMovieErrorStateLiveData(): LiveData<String> {
        return errorStateMovieLiveData
    }

    fun observeMovieListLiveData(): LiveData<ArrayList<MovieWatchlist>> {
        return movieListLiveData
    }

    fun observeTvLoadingStateLiveData(): LiveData<Boolean> {
        return loadingStateTvLiveData
    }

    fun observeTvErrorStateLiveData(): LiveData<String> {
        return errorStateTvLiveData
    }

    fun observeTvListLiveData(): LiveData<ArrayList<TvWatchlist>> {
        return tvListLiveData
    }

    fun observeMovieDeleteLiveData(): LiveData<Resource<MovieWatchlist>> {
        return movieDeleteLiveData
    }

    fun observeTvDeleteLiveData(): LiveData<Resource<TvWatchlist>> {
        return tvDeleteLiveData
    }
}
