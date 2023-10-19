package com.example.airmovies.fragments.discoverfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.airmovies.adapters.TrendingActorAdapter
import com.example.airmovies.databinding.FragmentActorDiscoverBinding
import com.example.airmovies.model.actor.PopularActorResult
import com.example.airmovies.model.tv.TvShowsResult
import com.example.airmovies.viewmodels.ActorTrendingViewModel

class ActorTrendingFragment : BaseDiscoverFragment() {

    private var _binding: FragmentActorDiscoverBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ActorTrendingViewModel by activityViewModels()
    private lateinit var trendingActorAdapter: TrendingActorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trendingActorAdapter = TrendingActorAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActorDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareTrendingActorRecyclerView()
        viewModel.getTrendingShows()
        observeTrendingActor()
    }

    fun prepareTrendingActorRecyclerView() {
        binding.recViewActorDiscover.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = trendingActorAdapter
        }
    }

    fun observeTrendingActor() {
        viewModel.observeTrendingActorLiveData().observe(viewLifecycleOwner
        ) { actorList ->
            trendingActorAdapter.setTrendingActors(actorList = actorList as ArrayList<PopularActorResult>)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}