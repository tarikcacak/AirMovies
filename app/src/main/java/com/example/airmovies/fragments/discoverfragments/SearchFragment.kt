package com.example.airmovies.fragments.discoverfragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.airmovies.R
import com.example.airmovies.adapters.TrendingActorAdapter
import com.example.airmovies.adapters.TrendingMoviesAdapter
import com.example.airmovies.adapters.TrendingTvShowsAdapter
import com.example.airmovies.databinding.FragmentSearchBinding
import com.example.airmovies.model.actor.PopularActorResult
import com.example.airmovies.model.movie.MoviesResult
import com.example.airmovies.model.tv.TvShowsResult
import com.example.airmovies.viewmodels.SearchViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var category: String
    private val viewModel: SearchViewModel by activityViewModels()
    private lateinit var searchMoviesAdapter: TrendingMoviesAdapter
    private lateinit var searchTvShowsAdapter: TrendingTvShowsAdapter
    private lateinit var searchActorsAdapter: TrendingActorAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchMoviesAdapter = TrendingMoviesAdapter()
        searchTvShowsAdapter = TrendingTvShowsAdapter()
        searchActorsAdapter = TrendingActorAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSearchData()

        onMovieClickListener()
        onTvShowClickListener()
        onActorClickListener()
    }

    private fun getSearchData() {
        val args = this.arguments
        category = args?.getString("category").toString()
        Log.d("test", category)
        var searchJob: Job? = null
        if (category == "0") {
            binding.etSearch.addTextChangedListener { searchQuery ->
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    delay(500)
                    viewModel.getSearchMovies(searchQuery.toString())
                    prepareMovieSearchRecyclerView()
                    observeSearchMoviesLiveData()
                }
            }
        } else if (category == "1") {
            binding.etSearch.addTextChangedListener { searchQuery ->
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    delay(500)
                    viewModel.getSearchTvShows(searchQuery.toString())
                    prepareTvShowSearchRecyclerView()
                    observeSearchTvShowsLiveData()
                }
            }
        } else if (category == "2") {
            binding.etSearch.addTextChangedListener { searchQuery ->
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    delay(500)
                    viewModel.getSearchActors(searchQuery.toString())
                    prepareActorSearchRecyclerView()
                    observeSearchActorsLiveData()
                }
            }
        }
    }

    private fun observeSearchMoviesLiveData() {
        viewModel.observeSearchMoviesLiveData().observe(viewLifecycleOwner
        ) { movieList ->
            searchMoviesAdapter.setTrendingMovies(movieList = movieList as ArrayList<MoviesResult>)
        }
    }

    private fun prepareMovieSearchRecyclerView() {
        binding.recViewSearch.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = searchMoviesAdapter
        }
    }

    private fun observeSearchTvShowsLiveData() {
        viewModel.observeSearchTvShowsLiveData().observe(viewLifecycleOwner
        ) { tvShowList ->
            searchTvShowsAdapter.setTrendingTvShows(tvShowList = tvShowList as ArrayList<TvShowsResult>)
        }
    }

    private fun prepareTvShowSearchRecyclerView() {
        binding.recViewSearch.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = searchTvShowsAdapter
        }
    }

    private fun observeSearchActorsLiveData() {
        viewModel.observeSearchActorsLiveData().observe(viewLifecycleOwner
        ) { actorList ->
            searchActorsAdapter.setTrendingActors(actorList = actorList as ArrayList<PopularActorResult>)
        }
    }

    private fun prepareActorSearchRecyclerView() {
        binding.recViewSearch.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = searchActorsAdapter
        }
    }

    private fun onMovieClickListener() {
        searchMoviesAdapter.setUpOnDiscoverMoviesClickListener { movie ->
            val bundle = Bundle().apply {
                putString("isMovie", "0")
                putString("idMovie", movie.id.toString())
            }
            findNavController().navigate(R.id.action_searchFragment_to_movieDetailsFragment, bundle)
        }
    }

    private fun onTvShowClickListener() {
        searchTvShowsAdapter.setUpOnDiscoveryTvShowClickListener { tvShow ->
            val bundle = Bundle().apply {
                putString("isTv", "1")
                putString("idTv", tvShow.id.toString())
            }
            findNavController().navigate(R.id.action_searchFragment_to_movieDetailsFragment, bundle)
        }
    }

    private fun onActorClickListener() {
        searchActorsAdapter.setUpOnDiscoverActorClickListener { actor ->
            val bundle = Bundle().apply {
                putString("idActor", actor.id.toString())
            }
            findNavController().navigate(R.id.action_searchFragment_to_actorFragment, bundle)
        }
    }
}