package com.example.cotacaofacil.presentation.ui.fragment.buyer

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.cotacaofacil.databinding.FragmentPriceBuyerBinding
import com.example.cotacaofacil.presentation.ui.activity.CreatePriceActivity
import com.example.cotacaofacil.presentation.ui.activity.CreatePriceActivity.Companion.SUCCESS_CREATE_PRICE
import com.example.cotacaofacil.presentation.ui.adapter.ItemPriceBuyerAdapter
import com.example.cotacaofacil.presentation.ui.dialog.ConfirmationCreatePriceDialog
import com.example.cotacaofacil.presentation.viewmodel.buyer.price.PriceBuyerViewModel
import com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractPrice.PriceEvent
import org.koin.androidx.viewmodel.ext.android.viewModel

class PriceBuyerFragment : Fragment() {
    private lateinit var binding: FragmentPriceBuyerBinding
    private val viewModel by viewModel<PriceBuyerViewModel>()
    private val adapter = ItemPriceBuyerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPriceBuyerBinding.inflate(inflater, container, false)
        binding.recycleViewPartner.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.priceEvent.observe(requireActivity()) { event ->
            when (event) {
                PriceEvent.CreatePrice -> {
                    startActivityForResult(Intent(requireContext(), CreatePriceActivity::class.java), SUCCESS_CREATE_PRICE)
                }
                is PriceEvent.ShowDialogSuccess -> ConfirmationCreatePriceDialog.newInstance(event.code).show(childFragmentManager, "")
            }
        }
        viewModel.priceState.observe(requireActivity()) { state ->
            binding.tvMessageError.text = state.messageError
            adapter.updateList(state.pricesModel)
            binding.progressBar.isVisible = state.showProgressBar
        }
    }

    private fun setupListeners() {
        binding.buttonAddPrice.setOnClickListener {
            viewModel.tapOnCreatePrice()
        }
    }

    override fun onResume() {
//        viewModel.updateListPrices()
        super.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SUCCESS_CREATE_PRICE && resultCode == RESULT_OK) {
            data?.getStringExtra(CODE_PRICE)?.let {
                viewModel.updateListPrices()
                viewModel.showDialogSuccess(it)
            }
        }
    }

    companion object {
        const val CODE_PRICE = "CODE_PRICE"
    }
}