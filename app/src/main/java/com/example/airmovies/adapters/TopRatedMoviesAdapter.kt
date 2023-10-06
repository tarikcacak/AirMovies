package com.example.airmovies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.airmovies.databinding.PopularMoviesBinding
import com.example.airmovies.model.movie.TopRatedMoviesResult

class TopRatedMoviesAdapter() : RecyclerView.Adapter<TopRatedMoviesAdapter.TopRatedMoviesViewHolder>() {

    private var moviesList = ArrayList<TopRatedMoviesResult>()

    fun setTopRatedMovies(moviesList: ArrayList<TopRatedMoviesResult>) {
        this.moviesList = moviesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRatedMoviesViewHolder {
        return TopRatedMoviesViewHolder(PopularMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TopRatedMoviesViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load("https://image.tmdb.org/t/p/w500" + moviesList[position]!!.posterPath)
            .into(holder.binding.ivPopularMovie)

        holder.binding.tvPopularMovie.text = moviesList[position].title
        holder.binding.rbPopularMovie.rating = moviesList[position].voteAverage.toFloat()
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    class TopRatedMoviesViewHolder(var binding: PopularMoviesBinding): RecyclerView.ViewHolder(binding.root)
}