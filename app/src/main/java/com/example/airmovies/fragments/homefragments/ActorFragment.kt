package com.example.airmovies.fragments.homefragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.airmovies.R
import com.example.airmovies.adapters.ActorFilmographyAdapter
import com.example.airmovies.databinding.FragmentActorBinding
import com.example.airmovies.model.actor.ActorMovieList
import com.example.airmovies.model.actor.ActorMovieResult
import com.example.airmovies.model.movie.MoviesResult
import com.example.airmovies.viewmodels.ActorViewModel

class ActorFragment : Fragment() {

    private var _binding: FragmentActorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ActorViewModel by activityViewModels()

    private lateinit var actorFilmographyAdapter: ActorFilmographyAdapter
    private lateinit var idActor: String
    private var isClicked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actorFilmographyAdapter = ActorFilmographyAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentActorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getOnClickData()
        observeActorDetails()
        prepareActorFilmographyRecyclerView()
        observeActorFilmography()
        setUpClickListeners()
        onFilmographyClickListener()

    }

    private fun getOnClickData() {
        val args = this.arguments
        idActor = args?.getString("idActor").toString()
        viewModel.getActorDetails(idActor)
        viewModel.getActorFilmography(idActor)
    }

    private fun observeActorDetails() {
        viewModel.observeActorDetailsLiveData().observe(viewLifecycleOwner
        ) { actor ->
            Glide.with(this@ActorFragment)
                .load("https://image.tmdb.org/t/p/w500" + actor!!.profilePath)
                .into(binding.imgPoster)

            binding.tvActorName.text = actor!!.name
            binding.tvBirthDate.text = "Born: " + actor!!.birthday
            binding.tvOverview.text = actor!!.biography
        }
    }

    private fun prepareActorFilmographyRecyclerView() {
        binding.recViewActorFilmography.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = actorFilmographyAdapter
        }
    }

    private fun observeActorFilmography() {
        viewModel.observeActorFilmographyLiveData().observe(viewLifecycleOwner
        ) { movieList ->
            actorFilmographyAdapter.setFilmography(movieList = movieList as ArrayList<ActorMovieResult>)
        }
    }

    private fun setUpClickListeners() = binding.apply {

        tvOverview.setOnClickListener {
            if (!isClicked) {
                isClicked = true
                tvOverview.maxLines = 100
            } else {
                isClicked = false
                tvOverview.maxLines = 4
            }
        }
    }

    private fun onFilmographyClickListener() {
        actorFilmographyAdapter.setUpOnFilmographyClickListener { movie ->
            val bundle = Bundle().apply {
                putString("isMovie", "0")
                putString("idMovie", movie.id.toString())
            }
            findNavController().navigate(R.id.action_actorFragment_to_movieDetailsFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}