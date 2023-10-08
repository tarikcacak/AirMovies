package com.example.airmovies.fragments.homefragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.airmovies.R
import com.example.airmovies.adapters.PopularMoviesAdapter
import com.example.airmovies.adapters.PopularTvShowsAdapter
import com.example.airmovies.adapters.RecentMoviesAdapter
import com.example.airmovies.adapters.TopRatedMoviesAdapter
import com.example.airmovies.adapters.TopRatedTvShowsAdapter
import com.example.airmovies.databinding.FragmentHomeBinding
import com.example.airmovies.model.movie.MoviesResult
import com.example.airmovies.model.tv.TvShowsResult
import com.example.airmovies.viewmodels.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var popularMoviesAdapter: PopularMoviesAdapter
    private lateinit var popularTvShowsAdapter: PopularTvShowsAdapter
    private lateinit var recentMoviesAdapter: RecentMoviesAdapter
    private lateinit var topRatedMoviesAdapter: TopRatedMoviesAdapter
    private lateinit var topRatedTvShowsAdapter: TopRatedTvShowsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        popularMoviesAdapter = PopularMoviesAdapter()
        popularTvShowsAdapter = PopularTvShowsAdapter()
        recentMoviesAdapter = RecentMoviesAdapter()
        topRatedMoviesAdapter = TopRatedMoviesAdapter()
        topRatedTvShowsAdapter = TopRatedTvShowsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularMoviesRecyclerView()
        binding.pbPopularMovies.visibility = View.VISIBLE
        viewModel.getPopularMovies()
        observePopularMovies()

        preparePopularTvShowsRecyclerView()
        binding.pbPopularTvShows.visibility = View.VISIBLE
        viewModel.getPopularTvShows()
        observePopularTvShows()

        prepareRecentMoviesRecyclerView()
        binding.pbRecentMovies.visibility = View.VISIBLE
        viewModel.getRecentMovies()
        observeRecentMovies()

        prepareTopRatedMoviesRecyclerView()
        binding.pbTopRatedMovies.visibility = View.VISIBLE
        viewModel.getTopRatedMovies()
        observeTopRatedMovies()

        prepareTopRatedTvShowsRecyclerView()
        binding.pbTopRatedTvShows.visibility = View.VISIBLE
        viewModel.getTopRatedTvShows()
        observeTopRatedTvShoes()

        onMovieClickListener()
        onTvShowClickListener()

    }

    private fun preparePopularMoviesRecyclerView() {
        binding.recViewPopularMovies.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularMoviesAdapter
        }
    }

    private fun observePopularMovies() {
        viewModel.observePopularMoviesLiveData().observe(viewLifecycleOwner
        ) { movieList ->
            popularMoviesAdapter.setMovies(movieList = movieList as ArrayList<MoviesResult>)
            binding.pbPopularMovies.visibility = View.GONE
        }
    }

    private fun preparePopularTvShowsRecyclerView() {
        binding.recViewPopularTvShows.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularTvShowsAdapter
        }
    }

    private fun observePopularTvShows() {
        viewModel.observePopularTvShowsLiveData().observe(viewLifecycleOwner
        ) { tvShowsList ->
            popularTvShowsAdapter.setTvShows(tvShowList = tvShowsList as ArrayList<TvShowsResult>)
            binding.pbPopularTvShows.visibility = View.GONE
        }
    }

    private fun prepareRecentMoviesRecyclerView() {
        binding.recViewRecentMovies.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = recentMoviesAdapter
        }
    }

    private fun observeRecentMovies() {
        viewModel.observeRecentMoviesLiveData().observe(viewLifecycleOwner
        ) { moviesList ->
            recentMoviesAdapter.setRecentMovies(moviesList = moviesList as ArrayList<MoviesResult>)
            binding.pbRecentMovies.visibility = View.GONE
        }
    }

    private fun prepareTopRatedMoviesRecyclerView() {
        binding.recViewTopRatedMovies.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = topRatedMoviesAdapter
        }
    }

    private fun observeTopRatedMovies() {
        viewModel.observeTopRatedMoviesLiveData().observe(viewLifecycleOwner
        ) { moviesList ->
            topRatedMoviesAdapter.setTopRatedMovies(moviesList = moviesList as ArrayList<MoviesResult>)
            binding.pbTopRatedMovies.visibility = View.GONE
        }
    }

    private fun prepareTopRatedTvShowsRecyclerView() {
        binding.recViewTopRatedTvShows.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = topRatedTvShowsAdapter
        }
    }

    private fun observeTopRatedTvShoes() {
        viewModel.observeTopRatedTvShowsLiveData().observe(viewLifecycleOwner
        ) { tvShowsList ->
            topRatedTvShowsAdapter.setTopRatedTvShows(tvShowsList = tvShowsList as ArrayList<TvShowsResult>)
            binding.pbTopRatedTvShows.visibility = View.GONE
        }
    }

    private fun onMovieClickListener() {

        popularMoviesAdapter.setOnPopularMovieItemClickListener { movie ->
            val bundle = Bundle().apply {
                putString("isMovie", "0")
                putString("id", movie.id.toString())
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment, bundle)
        }

        recentMoviesAdapter.setOnRecentMovieItemClickListener { movie ->
            val bundle = Bundle().apply {
                putString("isMovie", "0")
                putString("id", movie.id.toString())
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment, bundle)
        }

        topRatedMoviesAdapter.setOnTopRatedMovieItemClickListener { movie ->
            val bundle = Bundle().apply {
                putString("isMovie", "0")
                putString("id", movie.id.toString())
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment, bundle)
        }
    }

    private fun onTvShowClickListener() {

        popularTvShowsAdapter.setOnPopularTvShowItemClickListener { tvShow ->
            val bundle = Bundle().apply {
                putString("isMovie", "1")
                putString("id", tvShow.id.toString())
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment, bundle)
        }

        topRatedTvShowsAdapter.setOnTopRatedTvShowItemClickListener { tvShow ->
            val bundle = Bundle().apply {
                putString("isMovie", "1")
                putString("id", tvShow.id.toString())
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailsFragment, bundle)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}