package com.example.airmovies.fragments.homefragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.airmovies.adapters.PopularMoviesAdapter
import com.example.airmovies.adapters.PopularTvShowsAdapter
import com.example.airmovies.adapters.RecentMoviesAdapter
import com.example.airmovies.adapters.TopRatedMoviesAdapter
import com.example.airmovies.databinding.FragmentHomeBinding
import com.example.airmovies.model.movie.PopularMoviesResult
import com.example.airmovies.model.movie.RecentMoviesResult
import com.example.airmovies.model.movie.TopRatedMoviesResult
import com.example.airmovies.model.tv.PopularTvShowsResult
import com.example.airmovies.viewmodels.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var popularMoviesAdapter: PopularMoviesAdapter
    private lateinit var popularTvShowsAdapter: PopularTvShowsAdapter
    private lateinit var recentMoviesAdapter: RecentMoviesAdapter
    private lateinit var topRatedMoviesAdapter: TopRatedMoviesAdapter

    companion object {
        const val MOVIE_ID = "package com.example.airmovies.fragments.homefragments.movieId"
        const val MOVIE_NAME = "package com.example.airmovies.fragments.homefragments.movieName"
        const val MOVIE_IMG = "package com.example.airmovies.fragments.homefragments.movieImg"
        const val GENRE = "package com.example.airmovies.fragments.homefragments.movieGenre"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        popularMoviesAdapter = PopularMoviesAdapter()
        popularTvShowsAdapter = PopularTvShowsAdapter()
        recentMoviesAdapter = RecentMoviesAdapter()
        topRatedMoviesAdapter = TopRatedMoviesAdapter()
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
            popularMoviesAdapter.setMovies(movieList = movieList as ArrayList<PopularMoviesResult>)
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
            popularTvShowsAdapter.setTvShows(tvShowList = tvShowsList as ArrayList<PopularTvShowsResult>)
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
            recentMoviesAdapter.setRecentMovies(moviesList = moviesList as ArrayList<RecentMoviesResult>)
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
            topRatedMoviesAdapter.setTopRatedMovies(moviesList = moviesList as ArrayList<TopRatedMoviesResult>)
            binding.pbTopRatedMovies.visibility = View.GONE
        }
    }
}