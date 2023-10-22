package com.example.airmovies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.airmovies.databinding.TrendingItemBinding
import com.example.airmovies.model.actor.PopularActorResult
import com.example.airmovies.model.movie.MoviesResult

class TrendingActorAdapter() : RecyclerView.Adapter<TrendingActorAdapter.TrendingActorViewHolder>() {

    var onItemClick: ((PopularActorResult) -> Unit)? = null
    private var actorList = ArrayList<PopularActorResult>()

    fun setTrendingActors(actorList: ArrayList<PopularActorResult>) {
        this.actorList = actorList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingActorViewHolder {
        return TrendingActorViewHolder(TrendingItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: TrendingActorViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load("https://image.tmdb.org/t/p/w500" + actorList!![position].profilePath)
            .into(holder.binding.ivDiscover)

        holder.binding.tvDiscover.text = actorList!![position].name
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(actorList[position])
        }
    }

    override fun getItemCount(): Int {
        return actorList.size
    }

    fun setUpOnDiscoverActorClickListener(actor: (PopularActorResult) -> Unit) {
        onItemClick = actor
    }

    class TrendingActorViewHolder(var binding: TrendingItemBinding): RecyclerView.ViewHolder(binding.root)
}