package com.example.airmovies.fragments.homefragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.airmovies.R
import com.example.airmovies.data.movie.MovieWatchlist
import com.example.airmovies.data.tv.TvWatchlist
import com.example.airmovies.databinding.FragmentHomeBinding
import com.example.airmovies.databinding.FragmentMovieDetailsBinding
import com.example.airmovies.util.Resource
import com.example.airmovies.viewmodels.MovieDetailsViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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

    private lateinit var posterPath: String

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

        }
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
        binding.fabAddWatchlist.setOnClickListener {

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

    private fun observeMovieCreditsLiveData() {
        viewModel.observeMovieCreditsLiveData().observe(viewLifecycleOwner
        ) {

        }
    }

    private fun setUpClickListeners() = binding.apply {

        tvOverview.setOnClickListener {
            if (!isClicked) {
                // Expand it
                isClicked = true
                tvOverview.maxLines = 100
            } else {
                // Collapse it
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