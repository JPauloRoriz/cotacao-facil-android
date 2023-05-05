package com.example.cotacaofacil.presentation.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.cotacaofacil.R
import com.example.cotacaofacil.databinding.ActivityStockBuyerBinding
import com.example.cotacaofacil.domain.model.UserModel
import com.example.cotacaofacil.presentation.ui.adapter.ProductAdapter
import com.example.cotacaofacil.presentation.ui.dialog.AddProductBottomSheetDialogFragment
import com.example.cotacaofacil.presentation.viewmodel.product.StockBuyerViewModel
import com.example.cotacaofacil.presentation.viewmodel.product.model.StockEvent
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class StockBuyerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStockBuyerBinding
    val user by lazy { intent?.extras?.getParcelable<UserModel>(USER) }
    private val viewModel: StockBuyerViewModel by viewModel { parametersOf(user) }
    private val productAdapter = ProductAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockBuyerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recycleViewProducts.adapter = productAdapter


        setupListeners()
        setupObservers()
    }


    private fun setupObservers() {
        viewModel.stateLiveData.observe(this) { state ->
            productAdapter.products = state.productsList
            binding.progressBar.isVisible = state.isLoading
            binding.recycleViewProducts.visibility = state.showListProducts
            binding.textViewMessageError.visibility = state.showError
            binding.imageButtonReload.visibility = state.showError
        }

        viewModel.eventLiveData.observe(this) { event ->
            when (event) {
                is StockEvent.StockEmptyEvent -> {
                    Toast.makeText(this, getString(R.string.stock_empty_message), Toast.LENGTH_SHORT).show()
                    this.finish()
                }
                is StockEvent.UpdateList -> {
                    productAdapter.updateList(event.products)
                }
            }
        }

    }

    private fun setupListeners() {
        binding.imageButtonReload.setOnClickListener {
            viewModel.initViewModel()
        }

        binding.floatingButtonAdd.setOnClickListener {
            AddProductBottomSheetDialogFragment.newInstance(user?.cnpj) { viewModel.initViewModel() }
                .show(supportFragmentManager, ADD_PRODUCT_BOTTOM_SHEET)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object {
        const val USER = "user"
        const val ADD_PRODUCT_BOTTOM_SHEET = "AddProductBottomSheetDialogFragment"
    }
}