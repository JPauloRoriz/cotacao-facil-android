package com.example.cotacaofacil.presentation.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.cotacaofacil.data.helper.UserHelper
import com.example.cotacaofacil.databinding.ActivityPriceInfoBinding
import com.example.cotacaofacil.presentation.ui.adapter.ItemTableProductAdapter
import com.example.cotacaofacil.presentation.viewmodel.price.PriceInfoViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PriceInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPriceInfoBinding
    private val userHelper by inject<UserHelper>()
    val user by lazy { userHelper.user }
    private val codePrice by lazy { intent.getStringExtra(CODE_PRICE_SHOW) ?: "" }
    private val cnpjBuyerCreator by lazy { intent.getStringExtra(CNPJ_USER) ?: "" }

    private val viewModel: PriceInfoViewModel by viewModel { parametersOf(codePrice, cnpjBuyerCreator) }
    private val allProductsPriceAdapter by lazy { ItemTableProductAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPriceInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupListeners()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.stateLiveData.observe(this) { state ->
            with(binding) {
                btnCancelPrice.isVisible = state.showBtnCancelPrice
                btnFinishPrice.isVisible = state.showBtnFinishPrice
                progressBar.isVisible = state.isLoading
                allProductsPriceAdapter.updateList(state.productsPrice)
                tvProductQuantity.text = state.quantityProducts
                tvCreationDateLabel.text = state.dateInit
                tvClosingDateLabel.text = state.dateFinish
                tvQuantityProvider.text = state.quantityProviders
            }
        }
    }

    private fun setupListeners() {
        binding.btnCancelPrice.setOnClickListener {
            viewModel.tapOnCancelPrice()
        }
    }

    private fun setupView() {
        binding.recyclerViewAllProducts.adapter = allProductsPriceAdapter
        allProductsPriceAdapter.isEditable = false
    }

    companion object {
        const val CODE_PRICE_SHOW = "CODE_PRICE_SHOW"
        const val CNPJ_USER = "CNPJ_USER"
    }
}