package com.example.cotacaofacil.presentation.ui.fragment.provider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cotacaofacil.databinding.FragmentHomeProviderBinding
import com.example.cotacaofacil.presentation.viewmodel.login.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeProviderFragment : Fragment() {
    private val viewModel by viewModel<LoginViewModel>()
    private lateinit var binding: FragmentHomeProviderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeProviderBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
    }

    private fun setupObservers() {
    }

    private fun setupListeners() {
    }
}