package com.example.cotacaofacil.presentation.ui.fragment.buyer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cotacaofacil.R
import com.example.cotacaofacil.databinding.FragmentPriceBuyerBinding
import com.example.cotacaofacil.presentation.ui.activity.CreatePriceActivity
import com.example.cotacaofacil.presentation.util.BottomNavigationListener
import com.example.cotacaofacil.presentation.viewmodel.buyer.price.PriceBuyerViewModel
import com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractPrice.PriceEvent
import org.koin.androidx.viewmodel.ext.android.viewModel

class PriceBuyerFragment : Fragment() {
    private lateinit var binding: FragmentPriceBuyerBinding
    private val viewModel by viewModel<PriceBuyerViewModel>()
    private var bottomNavigationListener: BottomNavigationListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPriceBuyerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomNavigationListener) {
            bottomNavigationListener = context
        } else {
            throw RuntimeException("$context must implement BottomNavigationListener")
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.priceEvent.observe(requireActivity()){event ->
            when(event){
                PriceEvent.CreatePrice -> {
                    startActivity(Intent(requireContext(), CreatePriceActivity::class.java))
                }
            }
        }
    }

    private fun setupListeners() {
        binding.buttonAddPrice.setOnClickListener {
            viewModel.tapOnCreatePrice()
        }
    }

    override fun onResume() {
        bottomNavigationListener?.onChangeFragmentBottomNavigation(R.id.priceBuyerFragment)
        super.onResume()
    }
}