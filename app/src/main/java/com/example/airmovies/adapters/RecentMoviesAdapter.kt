package com.example.airmovies.adapters

import android.graphics.Movie
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.airmovies.databinding.PopularMoviesBinding
import com.example.airmovies.model.movie.MoviesResult

class RecentMoviesAdapter() : RecyclerView.Adapter<RecentMoviesAdapter.RecentMoviesViewHolder>() {

    var onItemClick: ((MoviesResult) -> Unit)? = null
    private var moviesList = ArrayList<MoviesResult>()

    fun setRecentMovies(moviesList: ArrayList<MoviesResult>) {
        this.moviesList = moviesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentMoviesViewHolder {
        return RecentMoviesViewHolder(PopularMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecentMoviesViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load("https://image.tmdb.org/t/p/w500" +  moviesList!![position].posterPath)
            .into(holder.binding.ivPopularMovie)

        holder.binding.tvPopularMovie.text = moviesList[position].title
        holder.binding.rbPopularMovie.rating = (moviesList[position].voteAverage.toFloat() / 2.0).toFloat()
        holder.itemView.setOnClickListener {
            onItemClick!!.invoke(moviesList!![position])
        }
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    fun setOnRecentMovieItemClickListener(movie: (MoviesResult) -> Unit) {
        onItemClick = movie
    }

    class RecentMoviesViewHolder(var binding: PopularMoviesBinding): RecyclerView.ViewHolder(binding.root)
}