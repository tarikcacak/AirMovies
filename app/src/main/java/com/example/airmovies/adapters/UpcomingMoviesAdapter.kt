package com.example.airmovies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.airmovies.databinding.ComingSoonMovieItemBinding
import com.example.airmovies.model.movie.MoviesResult

class UpcomingMoviesAdapter() : RecyclerView.Adapter<UpcomingMoviesAdapter.UpcomingMoviesViewHolder>() {

    var onItemClick: ((MoviesResult) -> Unit)? = null
    private var moviesList = ArrayList<MoviesResult>()

    fun getMovies(moviesList: ArrayList<MoviesResult>) {
        this.moviesList = moviesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingMoviesViewHolder {
        return UpcomingMoviesViewHolder(ComingSoonMovieItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: UpcomingMoviesViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load("https://image.tmdb.org/t/p/w500/" + moviesList[position].posterPath)
            .into(holder.binding.imgComingSoon)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(moviesList[position])
        }
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    fun setOnUpcomingMovieItemClick(movie: (MoviesResult) -> Unit) {
        onItemClick = movie
    }

    class UpcomingMoviesViewHolder(val binding: ComingSoonMovieItemBinding): RecyclerView.ViewHolder(binding.root)
}