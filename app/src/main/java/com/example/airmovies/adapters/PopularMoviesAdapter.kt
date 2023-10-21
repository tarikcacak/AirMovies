package com.example.airmovies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.airmovies.databinding.PopularMoviesBinding
import com.example.airmovies.model.movie.MoviesResult

class PopularMoviesAdapter() : RecyclerView.Adapter<PopularMoviesAdapter.PopularMoviesViewHolder>() {

    var onItemClick: ((MoviesResult) -> Unit)? = null
    private var movieList = ArrayList<MoviesResult>()

    fun setMovies(movieList: ArrayList<MoviesResult>) {
        this.movieList = movieList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMoviesViewHolder {
        return PopularMoviesViewHolder(PopularMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PopularMoviesViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load("https://image.tmdb.org/t/p/w500" + movieList!![position].posterPath)
            .into(holder.binding.ivPopularMovie)

        holder.binding.tvPopularMovie.text = movieList[position].title
        holder.binding.rbPopularMovie.rating = (movieList[position].voteAverage.toFloat() / 2.0).toFloat()
        holder.itemView.setOnClickListener {
            onItemClick!!.invoke(movieList!![position])
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    fun setOnPopularMovieItemClickListener(movie: (MoviesResult) -> Unit) {
        onItemClick = movie
    }

    class PopularMoviesViewHolder(var binding: PopularMoviesBinding): RecyclerView.ViewHolder(binding.root)
}