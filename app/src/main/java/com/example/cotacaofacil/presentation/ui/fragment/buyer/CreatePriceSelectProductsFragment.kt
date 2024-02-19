package com.example.cotacaofacil.presentation.ui.fragment.buyer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.cotacaofacil.R
import com.example.cotacaofacil.databinding.FragmentPriceSelectProductsBinding
import com.example.cotacaofacil.domain.model.PriceModel
import com.example.cotacaofacil.presentation.ui.adapter.ProductSelectPriceAdapter
import com.example.cotacaofacil.presentation.viewmodel.buyer.price.SelectProductsViewModel
import com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractSelectProducts.SelectProductsEvent
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CreatePriceSelectProductsFragment : Fragment() {
    private lateinit var binding: FragmentPriceSelectProductsBinding
    private val priceModel by lazy { arguments?.getParcelable<PriceModel>(PRICE_MODEL) }
    val viewModel: SelectProductsViewModel by viewModel { parametersOf(priceModel) }
    private val adapter = ProductSelectPriceAdapter()


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
                viewModel.filterList(tab?.position, binding.searchView.query.toString())
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

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                viewModel.performSearch(p0, binding.tabLayout.selectedTabPosition, adapter.products)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                viewModel.performSearch(p0, binding.tabLayout.selectedTabPosition, adapter.products)
                return true
            }

        })

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
            when (event) {
                SelectProductsEvent.UpdateList -> {}
                is SelectProductsEvent.ErrorSelectMinOneProduct -> Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                is SelectProductsEvent.UpdateListProducts -> adapter.updateList(event.products)
                is SelectProductsEvent.Next -> {
                    val navOptions = NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_in_right)
                        .setExitAnim(R.anim.slide_out_left)
                        .setPopEnterAnim(R.anim.slide_out_right)
                        .setPopExitAnim(R.anim.slide_in_left)
                        .build()
                    val bundle = Bundle()
                    bundle.putParcelable(PRICE_MODEL, event.priceModel)
                    findNavController().navigate(R.id.reportInitPriceFragment, bundle, navOptions)

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

}
