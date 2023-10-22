package com.example.airmovies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.airmovies.databinding.ComingSoonMovieItemBinding
import com.example.airmovies.model.tv.TvShowsResult

class UpcomingTvShowAdapter() : RecyclerView.Adapter<UpcomingTvShowAdapter.UpcomingTvShowViewHolder>() {

    var onItemClick: ((TvShowsResult) -> Unit)? = null
    private var tvShowsList = ArrayList<TvShowsResult>()

    fun getTvShows(tvShowsList: ArrayList<TvShowsResult>) {
        this.tvShowsList = tvShowsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingTvShowViewHolder {
        return UpcomingTvShowViewHolder(ComingSoonMovieItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: UpcomingTvShowViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load("https://image.tmdb.org/t/p/w500/" + tvShowsList[position].posterPath)
            .into(holder.binding.imgComingSoon)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(tvShowsList[position])
        }
    }

    override fun getItemCount(): Int {
        return tvShowsList.size
    }

    fun setUpOnUpcomingTvShowItemClick(tv: (TvShowsResult) -> Unit) {
        onItemClick = tv
    }

    class UpcomingTvShowViewHolder(val binding: ComingSoonMovieItemBinding): RecyclerView.ViewHolder(binding.root)
}