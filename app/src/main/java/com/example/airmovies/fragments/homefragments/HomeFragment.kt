package com.example.airmovies.fragments.homefragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.airmovies.R
import com.example.airmovies.adapters.PopularMoviesAdapter
import com.example.airmovies.databinding.FragmentHomeBinding
import com.example.airmovies.viewmodels.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var popularMoviesAdapter: PopularMoviesAdapter

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
}