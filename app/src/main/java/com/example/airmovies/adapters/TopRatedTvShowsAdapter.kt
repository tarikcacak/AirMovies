package com.example.airmovies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.airmovies.databinding.PopularMoviesBinding
import com.example.airmovies.model.tv.TvShowsResult

class TopRatedTvShowsAdapter() : RecyclerView.Adapter<TopRatedTvShowsAdapter.TopRatedTvShowsViewHolder>() {

    var onItemClick: ((TvShowsResult) -> Unit)? = null
    private var tvShowsList = ArrayList<TvShowsResult>()

    fun setTopRatedTvShows(tvShowsList: ArrayList<TvShowsResult>) {
        this.tvShowsList = tvShowsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRatedTvShowsViewHolder {
        return TopRatedTvShowsViewHolder(PopularMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TopRatedTvShowsViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load("https://image.tmdb.org/t/p/w500" + tvShowsList!![position].posterPath)
            .into(holder.binding.ivPopularMovie)

        holder.binding.tvPopularMovie.text = tvShowsList[position].name
        holder.binding.rbPopularMovie.rating = (tvShowsList[position].voteAverage.toFloat() / 2.0).toFloat()
        holder.itemView.setOnClickListener {
            onItemClick!!.invoke(tvShowsList!![position])
        }
    }

    override fun getItemCount(): Int {
        return tvShowsList.size
    }

    fun setOnTopRatedTvShowItemClickListener(movie: (TvShowsResult) -> Unit) {
        onItemClick = movie
    }

    class TopRatedTvShowsViewHolder(var binding: PopularMoviesBinding): RecyclerView.ViewHolder(binding.root)
}