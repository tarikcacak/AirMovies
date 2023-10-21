package com.example.airmovies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.airmovies.data.tv.TvWatchlist
import com.example.airmovies.databinding.PopularMoviesBinding

class TvWatchlistAdapter() : RecyclerView.Adapter<TvWatchlistAdapter.TvWatchlistViewHolder>() {

    var onItemClick: ((TvWatchlist) -> Unit)? = null
    var onLongItemClick: ((TvWatchlist) -> Unit)? = null
    private var tvWatchlist = ArrayList<TvWatchlist>()

    fun setTv(tvWatchlist: ArrayList<TvWatchlist>) {
        this.tvWatchlist = tvWatchlist
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvWatchlistViewHolder {
        return TvWatchlistViewHolder(PopularMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TvWatchlistViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load("https://image.tmdb.org/t/p/w500" + tvWatchlist!![position].posterPath)
            .into(holder.binding.ivPopularMovie)

        holder.binding.tvPopularMovie.text = tvWatchlist!![position].title
        holder.binding.rbPopularMovie.rating = (tvWatchlist!![position].voteAverage!!.toFloat() / 2.0).toFloat()
        holder.itemView.setOnClickListener {
            onItemClick!!.invoke(tvWatchlist!![position])
        }
        holder.itemView.setOnLongClickListener {
            onLongItemClick!!.invoke(tvWatchlist[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return tvWatchlist.size
    }

    fun setOnWatchlistTvClickListener(tv: (TvWatchlist) -> Unit) {
        onItemClick = tv
    }

    fun setOnLongWatchlistTvClickListener(tv: (TvWatchlist) -> Unit) {
        onLongItemClick = tv
    }

    class TvWatchlistViewHolder(var binding: PopularMoviesBinding): RecyclerView.ViewHolder(binding.root)
}