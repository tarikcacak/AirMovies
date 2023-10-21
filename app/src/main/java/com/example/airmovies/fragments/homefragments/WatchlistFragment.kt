package com.example.airmovies.fragments.homefragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.airmovies.R
import com.example.airmovies.adapters.MovieWatchlistAdapter
import com.example.airmovies.adapters.TvWatchlistAdapter
import com.example.airmovies.databinding.FragmentWatchlistBinding
import com.example.airmovies.util.Resource
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
        onLongClickListener()
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

    private fun onLongClickListener() {
        movieWatchlistAdapter.setOnLongWatchlistMovieClickListener { movie ->
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setMessage("Do you want to remove this movie from the watchlist?")
            dialogBuilder.setCancelable(false)
            dialogBuilder.setTitle("${movie.title}")
            dialogBuilder.setPositiveButton("Yes") { dialog, which ->
                viewModel.deleteMovie(movie)
                dialog.dismiss()
            }
            dialogBuilder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = dialogBuilder.create()
            dialog.show()
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(resources.getColor(R.color.primaryColor))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(resources.getColor(R.color.primaryColor))
        }

        tvWatchlistAdapter.setOnLongWatchlistTvClickListener { tv ->
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setMessage("Do you want to remove this tv show from the watchlist?")
            dialogBuilder.setCancelable(false)
            dialogBuilder.setTitle("${tv.title}")
            dialogBuilder.setPositiveButton("Yes") { dialog, which ->
                viewModel.deleteTv(tv)
                dialog.dismiss()
            }
            dialogBuilder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = dialogBuilder.create()
            dialog.show()
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(resources.getColor(R.color.primaryColor))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(resources.getColor(R.color.primaryColor))
        }
    }
}