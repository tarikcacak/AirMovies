package com.example.airmovies.fragments.homefragments

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.airmovies.R
import com.example.airmovies.activities.LoginActivity
import com.example.airmovies.adapters.MovieWatchlistAdapter
import com.example.airmovies.adapters.TvWatchlistAdapter
import com.example.airmovies.databinding.FragmentProfileBinding
import com.example.airmovies.dialog.setupBottomSheetDialog
import com.example.airmovies.viewmodels.ProfileViewModel
import com.example.airmovies.viewmodels.WatchlistViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var profilePictureUri: Uri? = null
    private val viewModel: ProfileViewModel by activityViewModels()
    private val watchlistViewModel: WatchlistViewModel by activityViewModels()
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
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData()
        observeUsername()
        observeImage()
        observeEmail()
        pickImage()
        onLogOutClick()
        viewModel.errorStateEdit.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show()
        }
        viewModel.loadingStateEdit.observe(viewLifecycleOwner) {
        }

        binding.pbMovieWatchlist.visibility = View.VISIBLE
        binding.pbTvWatchlist.visibility = View.VISIBLE

        prepareMovieWatchlistRecyclerView()
        prepareTvWatchlistRecyclerView()

        watchlistViewModel.getWatchlistMovie()
        watchlistViewModel.getWatchlistTv()

        observeMovieWatchlist()
        observeTvWatchlist()

        onMovieClickListener()
        onTvClickListener()
        onLongClickListener()

        binding.btnResetPass.setOnClickListener {
            setupBottomSheetDialog { email ->
                if (!email.isNullOrEmpty()) {
                    viewModel.resetPassword(email)
                    Toast.makeText(requireContext(), "Reset link is sent to your email", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "You didn't give the email", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun observeUsername() {
        viewModel.observeUsernameLiveData().observe(viewLifecycleOwner
        ) { username ->
            binding.tvUsername.text = username
        }
    }

    private fun observeImage() {
        viewModel.observeImageLiveData().observe(viewLifecycleOwner
        ) { image ->
            Glide.with(this@ProfileFragment)
                .load(image)
                .into(binding.imgProfile)
        }
    }

    private fun observeEmail() {
        viewModel.observeEmailLiveData().observe(viewLifecycleOwner
        ) { email ->
            binding.tvEmail.text = "Email: $email"
        }
    }

    private val someActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val uri = data?.data
            profilePictureUri = uri
            viewModel.saveImage(profilePictureUri!!)
            Log.d("test123", uri.toString())
            Glide.with(this@ProfileFragment)
                .load(uri)
                .into(binding.imgProfile)
        }
    }

    private fun pickImage() {
        binding.imgButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/jpeg"
            someActivityResultLauncher.launch(intent)
        }
    }

    private fun onLogOutClick() {
        binding.btnLogut.setOnClickListener {
            viewModel.logOut()
            Intent(requireActivity(), LoginActivity::class.java).also { intent ->
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeMovieWatchlist() {
        watchlistViewModel.observeMovieListLiveData().observe(viewLifecycleOwner
        ) { movieWatchlist ->
            if (movieWatchlist.isNotEmpty()) {
                movieWatchlistAdapter.setMovies(movieWatchlist)
                binding.pbMovieWatchlist.visibility = View.GONE
            } else {
                binding.pbMovieWatchlist.visibility = View.GONE
                binding.tvEmptyMovies.visibility = View.VISIBLE
            }
        }

        watchlistViewModel.observeMovieErrorStateLiveData().observe(viewLifecycleOwner
        ) { error ->
            Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show()
        }

        watchlistViewModel.observeMovieLoadingStateLiveData().observe(viewLifecycleOwner) { }
    }

    private fun observeTvWatchlist() {
        watchlistViewModel.observeTvListLiveData().observe(viewLifecycleOwner
        ) { tvWatchlist ->
            if (tvWatchlist.isNotEmpty()) {
                tvWatchlistAdapter.setTv(tvWatchlist)
                binding.pbTvWatchlist.visibility = View.GONE
            } else {
                binding.pbTvWatchlist.visibility = View.GONE
                binding.tvEmptyTv.visibility = View.VISIBLE
            }

            watchlistViewModel.observeTvErrorStateLiveData().observe(viewLifecycleOwner
            ) { error ->
                Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show()
            }

            watchlistViewModel.observeTvLoadingStateLiveData().observe(viewLifecycleOwner) { }
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
            findNavController().navigate(R.id.action_comingSoonFragment_to_movieDetailsFragment, bundle)
        }
    }

    private fun onTvClickListener() {
        tvWatchlistAdapter.setOnWatchlistTvClickListener { tv ->
            val bundle = Bundle().apply {
                putString("isMovie", "1")
                putString("idTv", tv.tvId.toString())
            }
            findNavController().navigate(R.id.action_comingSoonFragment_to_movieDetailsFragment, bundle)
        }
    }

    private fun onLongClickListener() {
        movieWatchlistAdapter.setOnLongWatchlistMovieClickListener { movie ->
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setMessage("Do you want to remove this movie from the watchlist?")
            dialogBuilder.setCancelable(false)
            dialogBuilder.setTitle("${movie.title}")
            dialogBuilder.setPositiveButton("Yes") { dialog, which ->
                watchlistViewModel.deleteMovie(movie)
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
                watchlistViewModel.deleteTv(tv)
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