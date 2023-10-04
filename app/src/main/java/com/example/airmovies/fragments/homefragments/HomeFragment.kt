package com.example.airmovies.fragments.homefragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.airmovies.R
import com.example.airmovies.adapters.PopularMoviesAdapter
import com.example.airmovies.databinding.FragmentHomeBinding
import com.example.airmovies.model.movie.PopularMoviesResult
import com.example.airmovies.viewmodels.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var popularMoviesAdapter: PopularMoviesAdapter

    companion object {
        const val MOVIE_ID = "package com.example.airmovies.fragments.homefragments.movieId"
        const val MOVIE_NAME = "package com.example.airmovies.fragments.homefragments.movieName"
        const val MOVIE_IMG = "package com.example.airmovies.fragments.homefragments.movieImg"
        const val GENRE = "package com.example.airmovies.fragments.homefragments.movieGenre"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        popularMoviesAdapter = PopularMoviesAdapter()
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
        viewModel.getPopularMovies()
        observePopularMovies()

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
        }
    }
}