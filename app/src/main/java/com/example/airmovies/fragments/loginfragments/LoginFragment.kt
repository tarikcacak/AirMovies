package com.example.airmovies.fragments.loginfragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.airmovies.R
import com.example.airmovies.activities.HomeActivity
import com.example.airmovies.databinding.FragmentLoginBinding
import com.example.airmovies.databinding.FragmentRegisterBinding
import com.example.airmovies.dialog.setupBottomSheetDialog
import com.example.airmovies.util.RegisterValidation
import com.example.airmovies.util.Resource
import com.example.airmovies.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_loginFragment_to_optionFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvNoAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.loginloginButton.setOnClickListener {
            val email = binding.textInputEditTextEmail.text.toString().trim()
            val password = binding.textInputEditTextPassword.text.toString()
            viewModel.login(email, password)
        }

        binding.tvForgotPassword.setOnClickListener {
            setupBottomSheetDialog { email ->
                if (!email.isNullOrEmpty()) {
                    viewModel.resetPassword(email)
                    Toast.makeText(requireContext(), "Reset link is sent to your email", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "You didn't give the email", Toast.LENGTH_LONG).show()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.login.collect{
                when(it){
                    is Resource.Loading ->{
                        binding.loginProgressBar.visibility = View.VISIBLE
                    }

                    is Resource.Error ->{
                        Toast.makeText(requireContext(),it.message.toString(), Toast.LENGTH_SHORT).show()
                        binding.loginProgressBar.visibility = View.GONE
                    }

                    is Resource.Success ->{
                        binding.loginProgressBar.visibility = View.GONE
                        Toast.makeText(requireContext(),"Succesfully logged in!", Toast.LENGTH_SHORT).show()
                        Intent(requireActivity(), HomeActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    else -> Unit
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.validation.collect{ validation ->
                if (validation.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.textInputEditTextEmail.apply {
                            requestFocus()
                            binding.textInputLayoutLoginEmail.helperText = validation.email.message
                        }
                    }
                }

                if (validation.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.textInputEditTextPassword.apply {
                            requestFocus()
                            binding.textInputLayoutLoginPassword.helperText = validation.password.message
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