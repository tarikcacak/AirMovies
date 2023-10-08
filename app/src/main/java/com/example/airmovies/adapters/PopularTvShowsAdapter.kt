package com.example.airmovies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.airmovies.databinding.PopularMoviesBinding
import com.example.airmovies.model.movie.MoviesResult
import com.example.airmovies.model.tv.TvShowsResult

class PopularTvShowsAdapter() : RecyclerView.Adapter<PopularTvShowsAdapter.PopularTvShowsViewHolder>() {

    var onItemClick: ((TvShowsResult) -> Unit)? = null
    private var tvShowList = ArrayList<TvShowsResult>()

    fun setTvShows(tvShowList: ArrayList<TvShowsResult>) {
        this.tvShowList = tvShowList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularTvShowsViewHolder {
        return PopularTvShowsViewHolder(PopularMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PopularTvShowsViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load("https://image.tmdb.org/t/p/w500" +  tvShowList!![position].posterPath)
            .into(holder.binding.ivPopularMovie)

        holder.binding.tvPopularMovie.text = tvShowList[position].name
        holder.binding.rbPopularMovie.rating = tvShowList[position].voteAverage.toFloat()
        holder.itemView.setOnClickListener {
            onItemClick!!.invoke(tvShowList!![position])
        }
    }

    override fun getItemCount(): Int {
        return tvShowList.size
    }

    fun setOnPopularTvShowItemClickListener(movie: (TvShowsResult) -> Unit) {
        onItemClick = movie
    }

    class PopularTvShowsViewHolder(var binding: PopularMoviesBinding): RecyclerView.ViewHolder(binding.root)
}