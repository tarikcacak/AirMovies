package com.example.airmovies.fragments.homefragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.airmovies.adapters.ActorsAdapter
import com.example.airmovies.adapters.SimilarItemsAdapter
import com.example.airmovies.data.movie.MovieWatchlist
import com.example.airmovies.data.tv.TvWatchlist
import com.example.airmovies.databinding.FragmentMovieDetailsBinding
import com.example.airmovies.model.movie.MovieCast
import com.example.airmovies.model.movie.MoviesResult
import com.example.airmovies.model.tv.TvShowCast
import com.example.airmovies.model.tv.TvShowsResult
import com.example.airmovies.util.Resource
import com.example.airmovies.viewmodels.MovieDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieDetailsViewModel by activityViewModels()

    private lateinit var idMovie: String
    private lateinit var idTv: String
    private lateinit var isMovie: String
    private var isClicked: Boolean = false
    private var isMovieHelp: Boolean = false
    private var toastHelper: Boolean = false
    private var toastHelperSecond: Boolean = false

    private lateinit var actorsAdapter: ActorsAdapter
    private lateinit var similarItemsAdapter: SimilarItemsAdapter

    private lateinit var posterPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actorsAdapter = ActorsAdapter()
        similarItemsAdapter = SimilarItemsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toastHelper = false
        toastHelperSecond = false

        getOnClickData()
        addToWatchlist()

        collectWatchlistMovie()
        collectWatchlistTv()

        observeMovieExists()
        observeTvExists()

        if (isMovie == "0") {
            binding.pbActors.visibility = View.VISIBLE
            binding.pbSimilar.visibility = View.VISIBLE
            observeMovieCreditsLiveData()
            observeSimilarMoviesLiveData()
            observeItemDetailsLiveData()
        } else {
            binding.pbActors.visibility = View.VISIBLE
            binding.pbSimilar.visibility = View.VISIBLE
            observeTvCreditsLiveData()
            observeSimilarTvLiveData()
            observeItemDetailsLiveData()
        }

        prepareActorsRecyclerView()
        prepareSimilarItemsRecyclerView()
        setUpClickListeners()
    }

    private fun getOnClickData() {
        val args = this.arguments
        idMovie = args?.getString("idMovie").toString()
        idTv = args?.getString("idTv").toString()
        isMovie = args?.getString("isMovie").toString()

        if (isMovie == "0") {
            viewModel.getMovieDetails(idMovie)
            viewModel.getMovieCredits(idMovie)
            viewModel.getSimilarMovies(idMovie)
        } else {
            viewModel.getTvDetails(idTv)
            viewModel.getTvCredits(idTv)
            viewModel.getSimilarTv(idTv)
        }
    }

    private fun addToWatchlist() {
        binding.fabFavorites.setOnClickListener {

            toastHelper = true
            toastHelperSecond = true

            if (isMovie == "0") {
                val movieWatchlist = MovieWatchlist(
                    idMovie,
                    posterPath,
                    binding.tvTitle.text.toString(),
                    binding.rbMovieRating.rating.toString()
                )
                viewModel.saveMovie(movieWatchlist)
            }
            if (isMovie == "1") {
                val tvWatchlist = TvWatchlist(
                    idTv,
                    posterPath,
                    binding.tvTitle.text.toString(),
                    binding.rbMovieRating.rating.toString()
                )
                viewModel.saveTv(tvWatchlist)
            }
        }
    }

    private fun collectWatchlistMovie() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.watchlistMovie.collect {
                when(it){
                    is Resource.Loading ->{
                    }
                    is Resource.Error ->{
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success ->{
                        if (toastHelper)
                        Toast.makeText(requireContext(),"Successfully saved movie", Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun collectWatchlistTv() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.watchlistTv.collect {
                when(it){
                    is Resource.Loading ->{
                    }
                    is Resource.Error ->{
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success ->{
                        if (toastHelper)
                            Toast.makeText(requireContext(),"Successfully saved tv show", Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun observeMovieExists() {
        viewModel.observeMovieExistsLiveData().observe(viewLifecycleOwner
        ) { exists ->
            if (exists == true) {
                if (toastHelperSecond)
                    Toast.makeText(context, "Movie already in you watchlist", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeTvExists() {
        viewModel.observeTvExistsLiveData().observe(viewLifecycleOwner
        ) { exists ->
            if (exists == true) {
                if (toastHelperSecond)
                    Toast.makeText(context, "Tv show already in you watchlist", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun prepareActorsRecyclerView() {
        binding.recViewActors.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = actorsAdapter
        }
    }

    private fun prepareSimilarItemsRecyclerView() {
        binding.recViewSimilarMovies.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = similarItemsAdapter
        }
    }

    private fun observeMovieCreditsLiveData() {
        viewModel.observeMovieCreditsLiveData().observe(viewLifecycleOwner
        ) { actorsListMovie ->
            actorsAdapter.setActorsMovie(actorsListMovie = actorsListMovie as ArrayList<MovieCast>)
            binding.pbActors.visibility = View.GONE
        }
    }

    private fun observeSimilarMoviesLiveData() {
        viewModel.observeSimilarMoviesLiveData().observe(viewLifecycleOwner
        ) { movieList ->
            similarItemsAdapter.setSimilarMovies(movieList = movieList as ArrayList<MoviesResult>)
            binding.pbSimilar.visibility = View.GONE
        }
    }

    private fun observeTvCreditsLiveData() {
        viewModel.observeTvCreditsLiveData().observe(viewLifecycleOwner
        ) { actorsListTv ->
            actorsAdapter.setActorsTv(actorsListTv = actorsListTv as ArrayList<TvShowCast>)
            binding.pbActors.visibility = View.GONE
        }
    }

    private fun observeSimilarTvLiveData() {
        viewModel.observeSimilarTvLiveData().observe(viewLifecycleOwner
        ) { tvShowList ->
            similarItemsAdapter.setSimilarTv(tvShowList = tvShowList as ArrayList<TvShowsResult>)
            binding.pbSimilar.visibility = View.GONE
        }
    }

    private fun observeItemDetailsLiveData() {
        if (isMovie == "0") {
            viewModel.observeMovieDetailsLiveData().observe(viewLifecycleOwner
            ) { movie ->
                Glide.with(this@MovieDetailsFragment)
                    .load("https://image.tmdb.org/t/p/w500" + movie!!.posterPath)
                    .into(binding.imgPoster)

                binding.tvTitle.text = movie!!.title
                binding.rbMovieRating.rating = movie!!.voteAverage.toFloat()
                binding.tvOverview.text = movie!!.overview
                posterPath = movie!!.posterPath
            }
        } else {
            viewModel.observeTvDetailsLiveData().observe(viewLifecycleOwner
            ) { tv ->
                Glide.with(this@MovieDetailsFragment)
                    .load("https://image.tmdb.org/t/p/w500" + tv!!.posterPath)
                    .into(binding.imgPoster)

                binding.tvTitle.text = tv!!.name
                binding.rbMovieRating.rating = tv!!.voteAverage.toFloat()
                binding.tvOverview.text = tv!!.overview
                posterPath = tv!!.posterPath
            }
        }
    }



    private fun setUpClickListeners() = binding.apply {

        tvOverview.setOnClickListener {
            if (!isClicked) {
                isClicked = true
                tvOverview.maxLines = 100
            } else {
                isClicked = false
                tvOverview.maxLines = 4
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}