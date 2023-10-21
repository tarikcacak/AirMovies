package com.example.airmovies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.airmovies.databinding.PopularMoviesBinding
import com.example.airmovies.model.actor.ActorMoviesResult

class ActorFilmographyAdapter() : RecyclerView.Adapter<ActorFilmographyAdapter.ActorFilmographyViewHolder>() {

    var onItemClick: ((ActorMoviesResult) -> Unit)? = null
    private var movieList = ArrayList<ActorMoviesResult>()

    fun setFilmography(movieList: ArrayList<ActorMoviesResult>) {
        this.movieList = movieList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorFilmographyViewHolder {
        return ActorFilmographyViewHolder(PopularMoviesBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ActorFilmographyViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load("https://image.tmdb.org/t/p/w500" + movieList!![position].posterPath)
            .into(holder.binding.ivPopularMovie)

        holder.binding.tvPopularMovie.text = movieList!![position].title
        holder.binding.rbPopularMovie.rating = (movieList!![position].voteAverage.toFloat() / 2.0).toFloat()
        holder.itemView.setOnClickListener {
            onItemClick!!.invoke(movieList!![position])
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    fun setUpOnFilmographyClickListener(movie: (ActorMoviesResult) -> Unit) {
        onItemClick = movie
    }

    class ActorFilmographyViewHolder(var binding: PopularMoviesBinding): RecyclerView.ViewHolder(binding.root)
}