package com.example.cotacaofacil.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.cotacaofacil.databinding.FragmentHistoryBinding
import com.example.cotacaofacil.presentation.ui.activity.HomeBuyerActivity
import com.example.cotacaofacil.presentation.ui.activity.HomeProviderActivity
import com.example.cotacaofacil.presentation.ui.adapter.HistoricAdapter
import com.example.cotacaofacil.presentation.viewmodel.history.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class HistoryFragment : Fragment() {
    private val userParcelableBuyer by lazy { (activity as? HomeBuyerActivity)?.user }
    private val userParcelableProvider by lazy { (activity as? HomeProviderActivity)?.user }
    private val viewModel: HistoryViewModel by viewModel { parametersOf(userParcelableBuyer ?: userParcelableProvider) }

    private lateinit var binding: FragmentHistoryBinding
    private val adapter by lazy { HistoricAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.stateLiveData.observe(requireActivity()) { state ->
            adapter.listHistoric = state.historicModelList
            binding.recycleViewHistory.adapter = adapter
            binding.imageViewErrorEmpty.isVisible = state.showImageError
            binding.tvMessageError.text = state.messageError
            binding.tvMessageError.isVisible = state.showImageError
        }
    }

    private fun setupListeners() {

    }

}
