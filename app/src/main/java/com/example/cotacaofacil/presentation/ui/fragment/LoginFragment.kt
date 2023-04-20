package com.example.cotacaofacil.presentation.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cotacaofacil.R
import com.example.cotacaofacil.databinding.FragmentLoginBinding
import com.example.cotacaofacil.presentation.ui.activity.HomeBuyerActivity
import com.example.cotacaofacil.presentation.ui.activity.HomeProviderActivity
import com.example.cotacaofacil.presentation.ui.activity.HomeProviderActivity.Companion.USER
import com.example.cotacaofacil.presentation.viewmodel.login.LoginViewModel
import com.example.cotacaofacil.presentation.viewmodel.login.model.LoginEvent
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {

    private val viewModel by viewModel<LoginViewModel>()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            viewModel.tapOnRegister()
        }

        binding.btnLogin.setOnClickListener {
            runBlocking {
                viewModel.tapOnLogin(
                    binding.edtLogin.text.toString(),
                    binding.edtPassword.text.toString()
                )
            }
        }
    }

    private fun setupObservers() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            binding.pbLoading.isVisible = state.isLoading
            binding.tvMessageError.text = state.messageError
        }

        viewModel.eventLiveData.observe(viewLifecycleOwner) { event ->
            when (event) {
                is LoginEvent.SuccessLoginBuyer -> {
                    binding.edtPassword.text?.clear()
                    val intent = Intent(requireContext(), HomeBuyerActivity::class.java)
                    intent.putExtra(HomeBuyerActivity.USER, event.user)
                    context?.startActivity(intent)
                }
                is LoginEvent.SuccessLoginProvider -> {
                    binding.edtPassword.text?.clear()
                    val intent = Intent(requireContext(), HomeProviderActivity::class.java)
                    intent.putExtra(HomeBuyerActivity.USER, event.user)
                    activity?.startActivity(intent)
                }
                LoginEvent.GoToRegister -> {
                    binding.edtLogin.text?.clear()
                    binding.edtPassword.text?.clear()
                    findNavController().navigate(R.id.registerFragment)
                }
            }
        }
    }



}