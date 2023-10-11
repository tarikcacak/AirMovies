package com.example.airmovies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.airmovies.databinding.ActorItemBinding
import com.example.airmovies.model.movie.MovieCast
import com.example.airmovies.model.tv.TvShowCast
import com.example.airmovies.viewmodels.MovieDetailsViewModel

class ActorsAdapter() : RecyclerView.Adapter<ActorsAdapter.ActorsViewHolder>() {

    private var isMovie: String = "0"
    private var actorsListMovie = ArrayList<MovieCast>()
    private var actorsListTv = ArrayList<TvShowCast>()

    fun setActorsMovie(actorsListMovie: ArrayList<MovieCast>) {
        this.actorsListMovie = actorsListMovie
        isMovie = "0"
        notifyDataSetChanged()
    }

    fun setActorsTv(actorsListTv: ArrayList<TvShowCast>) {
        this.actorsListTv = actorsListTv
        isMovie = "1"
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorsViewHolder {
        return ActorsViewHolder(ActorItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ActorsViewHolder, position: Int) {
        if (isMovie == "0") {
            Glide.with(holder.itemView)
                .load("https://image.tmdb.org/t/p/w500" + actorsListMovie!![position].profilePath)
                .into(holder.binding.ivActorImage)

            holder.binding.tvCahracterName.text = actorsListMovie!![position].character
            holder.binding.tvActorName.text = actorsListMovie!![position].name
        } else {
            Glide.with(holder.itemView)
                .load("https://image.tmdb.org/t/p/w500" + actorsListTv!![position].profilePath)
                .into(holder.binding.ivActorImage)

            holder.binding.tvCahracterName.text = actorsListTv!![position].character
            holder.binding.tvActorName.text = actorsListTv!![position].name
        }
    }

    override fun getItemCount(): Int {
        if (isMovie == "0") {
            return actorsListMovie.size
        } else {
            return actorsListTv.size
        }
    }

    class ActorsViewHolder(var binding: ActorItemBinding): RecyclerView.ViewHolder(binding.root)
}