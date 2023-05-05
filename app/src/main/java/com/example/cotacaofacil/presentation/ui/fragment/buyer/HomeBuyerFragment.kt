package com.example.cotacaofacil.presentation.ui.fragment.buyer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cotacaofacil.R
import com.example.cotacaofacil.databinding.FragmentHomeBuyerBinding
import com.example.cotacaofacil.presentation.ui.activity.StockBuyerActivity
import com.example.cotacaofacil.presentation.ui.dialog.AddProductBottomSheetDialogFragment
import com.example.cotacaofacil.presentation.viewmodel.buyer.home.HomeBuyerViewModel
import com.example.cotacaofacil.presentation.viewmodel.buyer.home.contract.HomeBuyerEvent
import com.example.cotacaofacil.presentation.viewmodel.buyer.home.contract.HomeBuyerState
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeBuyerFragment : Fragment() {
    private val viewModel by viewModel<HomeBuyerViewModel>()
    private lateinit var binding: FragmentHomeBuyerBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBuyerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.homeBuyerEventLiveData.observe(viewLifecycleOwner) { event ->
            when (event) {
                HomeBuyerEvent.ErrorLoadListProducts -> {
                    Toast.makeText(requireContext(), context?.getString(R.string.message_error_impossible_load_products_stock), Toast.LENGTH_SHORT)
                        .show()
                }
                is HomeBuyerEvent.ListEmptyProducts -> {
                    Toast.makeText(requireContext(), context?.getString(R.string.products_empty_toast_add), Toast.LENGTH_SHORT).show()
                    val addProductBottomSheetDialogFragment = AddProductBottomSheetDialogFragment.newInstance(event.user?.cnpj) {

                    }
                    activity?.supportFragmentManager?.let { it1 -> addProductBottomSheetDialogFragment.show(it1, "") }
                }
                is HomeBuyerEvent.SuccessListProducts -> {
                    val intent = Intent(requireContext(), StockBuyerActivity::class.java)
                    intent.putExtra(StockBuyerActivity.USER, event.user)
                    startActivity(intent)
                }
            }
        }

        viewModel.homeBuyerStateLiveData.observe(viewLifecycleOwner) { state ->
           binding.textViewEmail.text = state.email
           binding.textViewCorporateName.text = state.nameCorporation
           binding.textViewFantasyName.text = state.nameFantasy
           binding.textViewFone.text = state.fone
        }
    }

    private fun setupListeners() {
        binding.cardViewStock.setOnClickListener {
            viewModel.tapOnCardStock()
        }
    }
}
