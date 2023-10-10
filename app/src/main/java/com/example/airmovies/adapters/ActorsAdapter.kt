package com.example.airmovies.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.airmovies.databinding.ActorItemBinding
import com.example.airmovies.model.movie.MovieCast
import com.example.airmovies.model.tv.TvShowCast

class ActorsAdapter(
    private var isMoive: String = "1"
) : RecyclerView.Adapter<ActorsAdapter.ActorsViewHolder>() {

    private var actorsListMovie = ArrayList<MovieCast>()
    private var actorsListTv = ArrayList<TvShowCast>()

    fun setActorsMovie(actorsListMovie: ArrayList<MovieCast>) {
        this.actorsListMovie = actorsListMovie
        notifyDataSetChanged()
    }

    fun setActorsTv(actorsListTv: ArrayList<TvShowCast>) {
        this.actorsListTv = actorsListTv
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorsViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ActorsViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class ActorsViewHolder(var binding: ActorItemBinding): RecyclerView.ViewHolder(binding.root)
}