package com.example.cotacaofacil.presentation.ui.fragment.buyer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cotacaofacil.databinding.FragmentPriceBuyerBinding

class PriceBuyerFragment : Fragment() {
    private lateinit var binding: FragmentPriceBuyerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPriceBuyerBinding.inflate(inflater, container, false)
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