package com.example.airmovies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.airmovies.databinding.PopularMoviesBinding
import com.example.airmovies.model.tv.PopularTvShowsResult

class PopularTvShowsAdapter() : RecyclerView.Adapter<PopularTvShowsAdapter.PopularTvShowsViewHolder>() {

    private var tvShowList = ArrayList<PopularTvShowsResult>()

    fun setTvShows(tvShowList: ArrayList<PopularTvShowsResult>) {
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
    }

    override fun getItemCount(): Int {
        return tvShowList.size
    }

    class PopularTvShowsViewHolder(var binding: PopularMoviesBinding): RecyclerView.ViewHolder(binding.root)
}