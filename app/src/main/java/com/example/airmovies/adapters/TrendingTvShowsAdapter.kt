package com.example.airmovies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.airmovies.databinding.TrendingItemBinding
import com.example.airmovies.model.tv.TvShowsResult

class TrendingTvShowsAdapter() : RecyclerView.Adapter<TrendingTvShowsAdapter.TrendingTvShowsViewHolder>() {

    var onItemClick: ((TvShowsResult) -> Unit)? = null
    private var tvShowList = ArrayList<TvShowsResult>()

    fun setTrendingTvShows(tvShowList: ArrayList<TvShowsResult>) {
        this.tvShowList = tvShowList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingTvShowsViewHolder {
        return TrendingTvShowsViewHolder(TrendingItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: TrendingTvShowsViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load("https://image.tmdb.org/t/p/w500" + tvShowList!![position].posterPath)
            .into(holder.binding.ivDiscover)

        holder.binding.tvDiscover.text = tvShowList!![position].name
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(tvShowList[position])
        }
    }

    override fun getItemCount(): Int {
        return tvShowList.size
    }

    fun setUpOnDiscoveryTvShowClickListener(tv: (TvShowsResult) -> Unit) {
        onItemClick = tv
    }

    class TrendingTvShowsViewHolder(var binding: TrendingItemBinding): RecyclerView.ViewHolder(binding.root)
}