package com.example.airmovies.fragments.discoverfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.airmovies.adapters.TrendingTvShowsAdapter
import com.example.airmovies.databinding.FragmentTvShowDiscoverBinding
import com.example.airmovies.model.tv.TvShowsResult
import com.example.airmovies.viewmodels.TvShowTrendingViewModel

class TvShowTrendingFragment : BaseDiscoverFragment() {

    private var _binding: FragmentTvShowDiscoverBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TvShowTrendingViewModel by activityViewModels()
    private lateinit var trendingTvShowsAdapter: TrendingTvShowsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trendingTvShowsAdapter = TrendingTvShowsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTvShowDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pbTvShows.visibility = View.VISIBLE
        prepareTrendingTvShowRecycler()
        viewModel.getTrendingTvShows()
        observeTrendingTvShows()

    }

    private fun prepareTrendingTvShowRecycler() {
        binding.recViewTvShowDiscover.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = trendingTvShowsAdapter
        }
    }

    private fun observeTrendingTvShows() {
        viewModel.observeTrendingTvShowLiveData().observe(viewLifecycleOwner
        ) { tvShowList ->
            trendingTvShowsAdapter.setTrendingTvShows(tvShowList = tvShowList as ArrayList<TvShowsResult>)
            binding.pbTvShows.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}