package com.example.airmovies.fragments.homefragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.airmovies.R
import com.example.airmovies.adapters.MovieWatchlistAdapter
import com.example.airmovies.adapters.PopularMoviesAdapter
import com.example.airmovies.adapters.PopularTvShowsAdapter
import com.example.airmovies.adapters.TvWatchlistAdapter
import com.example.airmovies.databinding.FragmentActorBinding
import com.example.airmovies.databinding.FragmentWatchlistBinding
import com.example.airmovies.viewmodels.WatchlistViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatchlistFragment : Fragment() {

    private var _binding: FragmentWatchlistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WatchlistViewModel by activityViewModels()
    private lateinit var movieWatchlistAdapter: MovieWatchlistAdapter
    private lateinit var tvWatchlistAdapter: TvWatchlistAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieWatchlistAdapter = MovieWatchlistAdapter()
        tvWatchlistAdapter = TvWatchlistAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pbMovieWatchlist.visibility = View.VISIBLE
        binding.pbTvWatchlist.visibility = View.VISIBLE

        prepareMovieWatchlistRecyclerView()
        prepareTvWatchlistRecyclerView()

        viewModel.getWatchlistMovie()
        viewModel.getWatchlistTv()

        observeMovieWatchlist()
        observeTvWatchlist()

        onMovieClickListener()
        onTvClickListener()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel
            }
        }
    }

    private fun observeMovieWatchlist() {
        viewModel.observeMovieListLiveData().observe(viewLifecycleOwner
        ) { movieWatchlist ->
            if (movieWatchlist.isNotEmpty()) {
                movieWatchlistAdapter.setMovies(movieWatchlist)
                binding.pbMovieWatchlist.visibility = View.GONE
            } else {
                binding.pbMovieWatchlist.visibility = View.GONE
                binding.tvEmptyMovies.visibility = View.VISIBLE
            }
        }

        viewModel.observeMovieErrorStateLiveData().observe(viewLifecycleOwner
        ) { error ->
            Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show()
        }

        viewModel.observeMovieLoadingStateLiveData().observe(viewLifecycleOwner) { }
    }

    private fun observeTvWatchlist() {
        viewModel.observeTvListLiveData().observe(viewLifecycleOwner
        ) { tvWatchlist ->
            if (tvWatchlist.isNotEmpty()) {
                tvWatchlistAdapter.setTv(tvWatchlist)
                binding.pbTvWatchlist.visibility = View.GONE
            } else {
                binding.pbTvWatchlist.visibility = View.GONE
                binding.tvEmptyTv.visibility = View.VISIBLE
            }

            viewModel.observeTvErrorStateLiveData().observe(viewLifecycleOwner
            ) { error ->
                Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show()
            }

            viewModel.observeTvLoadingStateLiveData().observe(viewLifecycleOwner) { }
        }
    }

    private fun prepareMovieWatchlistRecyclerView() {
        binding.recViewWatchlistMovies.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = movieWatchlistAdapter
        }
    }

    private fun prepareTvWatchlistRecyclerView() {
        binding.recViewWatchlistTvShows.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = tvWatchlistAdapter
        }
    }

    private fun onMovieClickListener() {
        movieWatchlistAdapter.setOnWatchlistMovieClickListener { movie ->
            val bundle = Bundle().apply {
                putString("isMovie", "0")
                putString("idMovie", movie.movieId.toString())
            }
            findNavController().navigate(R.id.action_watchlistFragment_to_movieDetailsFragment, bundle)
        }
    }

    private fun onTvClickListener() {
        tvWatchlistAdapter.setOnWatchlistTvClickListener { tv ->
            val bundle = Bundle().apply {
                putString("isMovie", "1")
                putString("idTv", tv.tvId.toString())
            }
            findNavController().navigate(R.id.action_watchlistFragment_to_movieDetailsFragment, bundle)
        }
    }

}