package com.example.airmovies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.airmovies.databinding.PopularMoviesBinding
import com.example.airmovies.model.movie.RecentMoviesResult

class RecentMoviesAdapter() : RecyclerView.Adapter<RecentMoviesAdapter.RecentMoviesViewHolder>() {

    private var moviesList = ArrayList<RecentMoviesResult>()

    fun setRecentMovies(moviesList: ArrayList<RecentMoviesResult>) {
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
        holder.binding.rbPopularMovie.rating = moviesList[position].voteAverage.toFloat()
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    class RecentMoviesViewHolder(var binding: PopularMoviesBinding): RecyclerView.ViewHolder(binding.root)
}