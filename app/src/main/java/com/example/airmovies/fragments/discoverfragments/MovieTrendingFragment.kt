package com.example.airmovies.fragments.discoverfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.airmovies.adapters.TrendingMoviesAdapter
import com.example.airmovies.databinding.FragmentMovieDiscoverBinding
import com.example.airmovies.model.movie.MoviesResult
import com.example.airmovies.viewmodels.MovieTrendingViewModel

class MovieTrendingFragment : BaseDiscoverFragment() {

    private var _binding: FragmentMovieDiscoverBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieTrendingViewModel by activityViewModels()
    private lateinit var trendingMoviesAdapter: TrendingMoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trendingMoviesAdapter = TrendingMoviesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareTrendingMoviesRecyclerView()
        viewModel.getTrendingMovies()
        observeTrendingMovies()
    }

    private fun prepareTrendingMoviesRecyclerView() {
        binding.recViewMovieDiscover.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = trendingMoviesAdapter
        }
    }

    private fun observeTrendingMovies() {
        viewModel.observeTrendingMoviesLiveData().observe(viewLifecycleOwner
        ) { movieList ->
            trendingMoviesAdapter.setTrendingMovies(movieList = movieList as ArrayList<MoviesResult>)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}