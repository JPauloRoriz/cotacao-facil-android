package com.example.cotacaofacil.presentation.ui.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.cotacaofacil.R
import com.example.cotacaofacil.databinding.FragmentHistoryBinding
import com.example.cotacaofacil.presentation.ui.adapter.HistoricAdapter
import com.example.cotacaofacil.presentation.viewmodel.history.HistoryViewModel
import com.example.cotacaofacil.presentation.viewmodel.history.model.HistoryEvent
import org.koin.androidx.viewmodel.ext.android.viewModel


class HistoryFragment : Fragment() {
    private val viewModel: HistoryViewModel by viewModel()

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

    override fun onResume() {
        viewModel.getAllItemHistory()
        super.onResume()
    }

    private fun setupObservers() {
        viewModel.eventLiveData.observe(requireActivity()) { event ->
            when (event){
                HistoryEvent.TapOnBack -> activity?.onBackPressed()
                is HistoryEvent.MessageSuccess -> {
                    adapter.updateList(event.newListHistoric)
                    Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.stateLiveData.observe(requireActivity()) { state ->
            adapter.listHistoric = state.historicModelList
            binding.recycleViewHistory.adapter = adapter
            binding.imageViewErrorEmpty.isVisible = state.showImageError
            binding.tvMessageError.text = state.messageError
            binding.tvMessageError.isVisible = state.showImageError
            binding.progressBar.isVisible = state.isLoading
        }
    }

    private fun setupListeners() {
        binding.arrow.setOnClickListener {
            viewModel.tapOnBack()
        }

        adapter.clickIconHistoric = {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.delete_historic))
            builder.setPositiveButton(R.string.yes) { p0, p1 ->
                viewModel.tapOnDeleteHistoric(it)
            }
            builder.setNegativeButton(R.string.not) { p0, p1 ->

            }
            builder.create().show()
        }
    }

}
