package com.example.airmovies.fragments.loginfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.airmovies.R
import com.example.airmovies.databinding.FragmentOptionBinding

class OptionFragment : Fragment() {

    private lateinit var binding: FragmentOptionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOptionBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_optionFragment_to_loginFragment)
        }

        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_optionFragment_to_registerFragment)
        }
    }
}