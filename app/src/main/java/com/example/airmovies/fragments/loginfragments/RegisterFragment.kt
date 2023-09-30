package com.example.airmovies.fragments.loginfragments

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.airmovies.R
import com.example.airmovies.data.User
import com.example.airmovies.databinding.FragmentRegisterBinding
import com.example.airmovies.util.RegisterValidation
import com.example.airmovies.util.Resource
import com.example.airmovies.util.validateEmail
import com.example.airmovies.viewmodels.RegisterViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val TAG = "RegisterFragment"
@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_registerFragment_to_optionFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvHasAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.apply {
            rgisterRegisterButton.setOnClickListener {
                val username = textInputEditTextUsernameR.text.toString().trim()
                val email = textInputEditTextEmailR.text.toString().trim()
                val user = User(
                    username,
                    email,
                )
                val password = textInputEditTextPasswordR.text.toString().trim()
                val passwordConf = textInputEditTextPasswordConfR.text.toString().trim()
                if (password == passwordConf) {
                    viewModel.createAccountWithEmailAndPassword(user, password)
                } else {
                    binding.textInputLayoutRegisterPasswordConf.helperText = "Passwords not matching!"
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.register.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.registerProgressBar.visibility = View.VISIBLE
                    }
                    is Resource.Error -> {
                        Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_LONG)
                        Log.d(TAG, it.message.toString())
                        binding.registerProgressBar.visibility = View.GONE
                    }
                    is Resource.Success -> {
                        binding.registerProgressBar.visibility = View.GONE
                        Log.d(TAG, "Successfuly registered!")
                        Snackbar.make(binding.root, "Successfuly registered!", Snackbar.LENGTH_LONG)
                    }
                    else -> Unit
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.validation.collect { validation ->
                if (validation.username is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.textInputEditTextUsernameR.apply {
                            requestFocus()
                            binding.textInputLayoutLoginRegisterUsername.helperText = validation.username.message
                        }
                    }
                }

                if (validation.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.textInputEditTextEmailR.apply {
                            requestFocus()
                            binding.textInputLayoutRegisterEmail.helperText = validation.email.message
                        }
                    }
                }

                if (validation.password is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.textInputEditTextPasswordR.apply {
                            requestFocus()
                            binding.textInputLayoutRegisterPassword.helperText = validation.password.message
                        }
                    }
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}