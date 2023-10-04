package com.example.airmovies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.airmovies.databinding.PopularMoviesBinding
import com.example.airmovies.model.movie.PopularMoviesResult

class PopularMoviesAdapter() : RecyclerView.Adapter<PopularMoviesAdapter.PopularMoviesViewHolder>() {

    private var movieList = ArrayList<PopularMoviesResult>()

    fun setMovies(movieList: ArrayList<PopularMoviesResult>) {
        this.movieList = movieList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMoviesViewHolder {
        return PopularMoviesViewHolder(PopularMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PopularMoviesViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(movieList[position].posterPath)
            .into(holder.binding.ivPopularMovie)

        holder.binding.tvPopularMovie.text = movieList[position].title
        holder.binding.rbPopularMovie.rating = movieList[position].voteAverage.toFloat()
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    class PopularMoviesViewHolder(var binding: PopularMoviesBinding): RecyclerView.ViewHolder(binding.root)
}