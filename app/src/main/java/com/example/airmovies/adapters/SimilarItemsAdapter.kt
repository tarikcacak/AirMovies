package com.example.airmovies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.airmovies.databinding.PopularMoviesBinding
import com.example.airmovies.model.movie.MoviesResult
import com.example.airmovies.model.tv.TvShowsResult

class SimilarItemsAdapter() : RecyclerView.Adapter<SimilarItemsAdapter.SimilarItemsViewHolder>() {

    private var isMovie: String = "0"
    private var movieList = ArrayList<MoviesResult>()
    private var tvShowList = ArrayList<TvShowsResult>()

    fun setSimilarMovies(movieList: ArrayList<MoviesResult>) {
        this.movieList = movieList
        isMovie = "0"
        notifyDataSetChanged()
    }

    fun setSimilarTv(tvShowList: ArrayList<TvShowsResult>) {
        this.tvShowList = tvShowList
        isMovie = "1"
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarItemsViewHolder {
        return SimilarItemsViewHolder(PopularMoviesBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: SimilarItemsViewHolder, position: Int) {
        if (isMovie == "0") {
            Glide.with(holder.itemView)
                .load("https://image.tmdb.org/t/p/w500" + movieList!![position].posterPath)
                .into(holder.binding.ivPopularMovie)

            holder.binding.tvPopularMovie.text = movieList!![position].title
            holder.binding.rbPopularMovie.rating = movieList!![position].voteAverage.toFloat()
        } else {
            Glide.with(holder.itemView)
                .load("https://image.tmdb.org/t/p/w500" + tvShowList!![position].posterPath)
                .into(holder.binding.ivPopularMovie)

            holder.binding.tvPopularMovie.text = tvShowList!![position].name
            holder.binding.rbPopularMovie.rating = tvShowList!![position].voteAverage.toFloat()
        }
    }

    override fun getItemCount(): Int {
        if (isMovie == "0") {
            return movieList.size
        } else {
            return tvShowList.size
        }
    }

    class SimilarItemsViewHolder(var binding: PopularMoviesBinding): RecyclerView.ViewHolder(binding.root)
}