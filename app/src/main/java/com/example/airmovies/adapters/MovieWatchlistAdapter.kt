package com.example.airmovies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.bumptech.glide.Glide
import com.example.airmovies.data.movie.MovieWatchlist
import com.example.airmovies.databinding.PopularMoviesBinding

class MovieWatchlistAdapter() : RecyclerView.Adapter<MovieWatchlistAdapter.MovieWatchlistViewHolder>() {

    var onItemClick: ((MovieWatchlist) -> Unit)? = null
    var onLongItemClick: ((MovieWatchlist) -> Unit)? = null
    private var movieWatchlist = ArrayList<MovieWatchlist>()

    fun setMovies(movieWatchlist: ArrayList<MovieWatchlist>) {
        this.movieWatchlist = movieWatchlist
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieWatchlistViewHolder {
        return MovieWatchlistViewHolder(PopularMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MovieWatchlistViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load("https://image.tmdb.org/t/p/w500" + movieWatchlist!![position].posterPath)
            .into(holder.binding.ivPopularMovie)

        holder.binding.tvPopularMovie.text = movieWatchlist!![position].title
        holder.binding.rbPopularMovie.rating = (movieWatchlist!![position].voteAverage!!.toFloat() / 2.0).toFloat()
        holder.itemView.setOnClickListener {
            onItemClick!!.invoke(movieWatchlist!![position])
        }
        holder.itemView.setOnLongClickListener {
            onLongItemClick!!.invoke(movieWatchlist[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return movieWatchlist.size
    }

    fun setOnWatchlistMovieClickListener(movie: (MovieWatchlist) -> Unit) {
        onItemClick = movie
    }

    fun setOnLongWatchlistMovieClickListener(movie: (MovieWatchlist) -> Unit) {
        onLongItemClick = movie
    }

    class MovieWatchlistViewHolder(var binding: PopularMoviesBinding): RecyclerView.ViewHolder(binding.root)
}