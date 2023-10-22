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
import com.example.airmovies.adapters.MovieWatchlistAdapter
import com.example.airmovies.adapters.TvWatchlistAdapter
import com.example.airmovies.adapters.UpcomingMoviesAdapter
import com.example.airmovies.adapters.UpcomingTvShowAdapter
import com.example.airmovies.databinding.FragmentComingSoonBinding
import com.example.airmovies.model.movie.MoviesResult
import com.example.airmovies.model.tv.TvShowsResult
import com.example.airmovies.viewmodels.ComingSoonViewModel
import com.example.airmovies.viewmodels.WatchlistViewModel
import com.google.android.material.carousel.CarouselLayoutManager

class ComingSoonFragment : Fragment() {

    private var _binding: FragmentComingSoonBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ComingSoonViewModel by activityViewModels()
    private lateinit var upcomingMoviesAdapter: UpcomingMoviesAdapter
    private lateinit var upcomingTvShowAdapter: UpcomingTvShowAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        upcomingMoviesAdapter = UpcomingMoviesAdapter()
        upcomingTvShowAdapter = UpcomingTvShowAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentComingSoonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUpcomingMovies()
        viewModel.getUpcomingTvShows()

        prepareUpcomingMoviesRecyclerView()
        observeUpcomingMovies()

        prepareUpcomingTvShowsRecyclerView()
        observeUpcomingTvShows()

        onItemClickListener()

    }

    private fun prepareUpcomingMoviesRecyclerView() {
        binding.recViewComingSoonMovies.apply {
            adapter = upcomingMoviesAdapter
            set3DItem(true)
            setAlpha(true)
            setInfinite(true)
        }
    }

    private fun observeUpcomingMovies() {
        viewModel.observeUpcomingMoviesLiveData().observe(viewLifecycleOwner
        ) { movieList ->
            upcomingMoviesAdapter.getMovies(moviesList = movieList as ArrayList<MoviesResult>)

        }
    }

    private fun prepareUpcomingTvShowsRecyclerView() {
        binding.recViewComingSoonTvShows.apply {
            adapter = upcomingTvShowAdapter
            set3DItem(true)
            setAlpha(true)
            setInfinite(true)
        }
    }

    private fun observeUpcomingTvShows() {
        viewModel.observeUpcomingTvShowsLiveData().observe(viewLifecycleOwner
        ) { tvShowList ->
            upcomingTvShowAdapter.getTvShows(tvShowsList = tvShowList as ArrayList<TvShowsResult>)
        }
    }

    private fun onItemClickListener() {
        upcomingMoviesAdapter.setOnUpcomingMovieItemClick { movie ->
            val bundle = Bundle().apply {
                putString("isMovie", "0")
                putString("idMovie", movie.id.toString())
            }
            findNavController().navigate(R.id.action_comingSoonFragment_to_movieDetailsFragment, bundle)
        }

        upcomingTvShowAdapter.setUpOnUpcomingTvShowItemClick { tv ->
            val bundle = Bundle().apply {
                putString("isMovie", "1")
                putString("idTv", tv.id.toString())
            }
            findNavController().navigate(R.id.action_comingSoonFragment_to_movieDetailsFragment, bundle)
        }
    }
}