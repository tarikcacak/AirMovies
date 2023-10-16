package com.example.airmovies.fragments.homefragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.airmovies.adapters.DiscoverViewPagerAdapter
import com.example.airmovies.databinding.FragmentDiscoverBinding
import com.example.airmovies.fragments.discoverfragments.ActorDiscoverFragment
import com.example.airmovies.fragments.discoverfragments.MovieDiscoverFragment
import com.example.airmovies.fragments.discoverfragments.TvShowDiscoverFragment
import com.google.android.material.tabs.TabLayoutMediator

class DiscoverFragment : Fragment() {

    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val discoverFragments = arrayListOf<Fragment>(
            MovieDiscoverFragment(),
            TvShowDiscoverFragment(),
            ActorDiscoverFragment()
        )

        val viewPager2Adapter = DiscoverViewPagerAdapter(discoverFragments, childFragmentManager, lifecycle)
        binding.discoverViewPager.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout, binding.discoverViewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Movie"
                1 -> tab.text = "Tv Show"
                2 -> tab.text = "Actor"
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}