package com.example.cotacaofacil.presentation.ui.fragment.buyer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.cotacaofacil.databinding.FragmentPriceSelectProductsBinding
import com.example.cotacaofacil.domain.model.PriceModel
import com.example.cotacaofacil.presentation.ui.adapter.ProductSelectPriceAdapter
import com.example.cotacaofacil.presentation.viewmodel.buyer.price.SelectProductsViewModel
import com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractSelectProducts.SelectProductsEvent
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePriceSelectProductsFragment : Fragment() {
    private lateinit var binding: FragmentPriceSelectProductsBinding
    private val viewModel by viewModel<SelectProductsViewModel>()
    private val adapter = ProductSelectPriceAdapter()
    val priceModel by lazy { arguments?.getParcelable<PriceModel>(PRICE_MODEL) }


    companion object {
        const val PRICE_MODEL = "priceModel"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPriceSelectProductsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycleViewProducts.adapter = adapter
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                  viewModel.filterList(tab?.position)

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding.buttonNext.setOnClickListener {
            viewModel.tapOnNext()
        }

        adapter.clickProduct = {
            viewModel.tapOnSelectProduct(it)
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun setupObservers() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            binding.textViewMessageError.text = state.messageError
            binding.progressBar.isVisible = state.isLoading
            binding.textViewMessageError.isVisible = state.showMessageError
            adapter.updateList(state.products)
            binding.buttonNext.backgroundTintList = AppCompatResources.getColorStateList(requireContext(), state.colorButtonNext)
            binding.buttonNext.setTextColor(ContextCompat.getColor(requireContext(), state.colorTextButtonNext))

        }

        viewModel.eventLiveData.observe(viewLifecycleOwner) { event ->
            when(event){
                SelectProductsEvent.UpdateList -> {}
                SelectProductsEvent.Next -> Toast.makeText(requireContext(), "Proximo", Toast.LENGTH_SHORT).show()
                is SelectProductsEvent.ErrorSelectMinOneProduct -> Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}
