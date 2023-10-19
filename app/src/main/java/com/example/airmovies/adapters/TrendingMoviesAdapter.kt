package com.example.airmovies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.airmovies.databinding.TrendingItemBinding
import com.example.airmovies.model.movie.MoviesResult

class TrendingMoviesAdapter() : RecyclerView.Adapter<TrendingMoviesAdapter.TrendingMoviesViewHolder>() {

    var onItemClick: ((MoviesResult) -> Unit)? = null
    private var movieList = ArrayList<MoviesResult>()

    fun setTrendingMovies(movieList: ArrayList<MoviesResult>) {
        this.movieList = movieList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingMoviesViewHolder {
        return TrendingMoviesViewHolder(TrendingItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: TrendingMoviesViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load("https://image.tmdb.org/t/p/w500" + movieList!![position].posterPath)
            .into(holder.binding.ivDiscover)

        holder.binding.tvDiscover.text = movieList!![position].title
        holder.itemView.setOnClickListener {
            onItemClick!!.invoke(movieList!![position])
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    fun setUpOnDiscoverMoviesClickListener(movie: (MoviesResult) -> Unit) {
        onItemClick = movie
    }

    class TrendingMoviesViewHolder(var binding: TrendingItemBinding): RecyclerView.ViewHolder(binding.root)
}