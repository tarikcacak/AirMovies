package com.example.airmovies.fragments.homefragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.airmovies.R
import com.example.airmovies.adapters.DiscoverViewPagerAdapter
import com.example.airmovies.databinding.FragmentDiscoverBinding
import com.example.airmovies.fragments.discoverfragments.ActorTrendingFragment
import com.example.airmovies.fragments.discoverfragments.MovieTrendingFragment
import com.example.airmovies.fragments.discoverfragments.TvShowTrendingFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DiscoverFragment : Fragment() {

    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!
    private var onTabSelectedListener: TabLayout.OnTabSelectedListener? = null
    private var category = 0

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
            MovieTrendingFragment(),
            TvShowTrendingFragment(),
            ActorTrendingFragment()
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

        onTabSelectedListener()

        binding.searchIcon.setOnClickListener {
            if (category == 0) {
                val bundle = Bundle().apply {
                    putString("category", "0")
                }
                findNavController().navigate(R.id.action_discoverFragment_to_searchFragment, bundle)
            }
            if (category == 1) {
                val bundle = Bundle().apply {
                    putString("category", "1")
                }
                findNavController().navigate(R.id.action_discoverFragment_to_searchFragment, bundle)
            }
            if (category == 2) {
                val bundle = Bundle().apply {
                    putString("category", "2")
                }
                findNavController().navigate(R.id.action_discoverFragment_to_searchFragment, bundle)
            }
        }
    }

    private fun onTabSelectedListener() {
        onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position) {
                    0 -> {
                        category = 0
                        Log.d("test", category.toString())
                    }
                    1 -> {
                        category = 1
                        Log.d("test", category.toString())
                    }
                    2 -> {
                        category = 2
                        Log.d("test", category.toString())
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }

            override fun onTabReselected(tab: TabLayout.Tab?) { }
        }
        binding.tabLayout.addOnTabSelectedListener(onTabSelectedListener!!)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}