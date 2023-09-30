package com.example.airmovies.fragments.loginfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.airmovies.R
import com.example.airmovies.data.User
import com.example.airmovies.databinding.FragmentRegisterBinding
import com.example.airmovies.util.RegisterValidation
import com.example.airmovies.util.Resource
import com.example.airmovies.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment() {


    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val  viewModel: RegisterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rgisterRegisterButton.setOnClickListener{
                val password = textInputEditTextPasswordR.text.toString()
                val passwordConf = textInputEditTextPasswordConfR.text.toString()
                if (password == passwordConf) {
                    val user = User(
                        textInputEditTextUsernameR.text.toString().trim(),
                        textInputEditTextEmailR.text.toString().trim(),
                        textInputEditTextPasswordR.text.toString()
                    )
                    viewModel.createAccountWithEmailAndPassword(user)
                } else {
                    binding.textInputLayoutRegisterPasswordConf.helperText = "Password not matching!"
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.register.collect{
                when(it){
                    is Resource.Loading ->{
                        binding.registerProgressBar.visibility = View.VISIBLE
                    }

                    is Resource.Error ->{
                        Toast.makeText(requireContext(),it.message.toString(), Toast.LENGTH_SHORT).show()
                        binding.registerProgressBar.visibility = View.GONE
                    }

                    is Resource.Success ->{
                        binding.registerProgressBar.visibility = View.GONE
                        Toast.makeText(requireContext(),"Succesfully register", Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit

                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.validation.collect{ validation ->
                if (validation.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.textInputEditTextEmailR.apply {
                            requestFocus()
                            binding.textInputLayoutRegisterEmail.helperText = validation.email.message
                        }
                    }
                }

                if (validation.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.textInputEditTextPasswordR.apply {
                            requestFocus()
                            binding.textInputLayoutRegisterPassword.helperText = validation.password.message
                        }
                    }
                }

                if (validation.username is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.textInputEditTextUsernameR.apply {
                            requestFocus()
                            binding.textInputLayoutLoginRegisterUsername.helperText = validation.username.message
                        }
                    }
                }
            }
        }

        binding.tvHasAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}