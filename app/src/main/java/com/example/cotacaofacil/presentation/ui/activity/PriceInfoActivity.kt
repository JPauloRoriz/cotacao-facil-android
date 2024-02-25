package com.example.cotacaofacil.presentation.ui.activity

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.cotacaofacil.R
import com.example.cotacaofacil.data.helper.UserHelper
import com.example.cotacaofacil.databinding.ActivityPriceInfoBinding
import com.example.cotacaofacil.domain.model.StatusPrice
import com.example.cotacaofacil.presentation.ui.adapter.ItemTableProductAdapter
import com.example.cotacaofacil.presentation.viewmodel.price.PriceInfoViewModel
import com.example.cotacaofacil.presentation.viewmodel.price.model.PriceEvent
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

        viewModel.eventLiveData.observe(this){event ->
            when(event){
                is PriceEvent.FinishActivity -> {
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnCancelPrice.setOnClickListener {
            val alert = AlertDialog.Builder(this, R.style.MyDialogTheme)
            alert.setMessage(
                getString(
                    R.string.confirmation_cancel_price
                )
            ).setTitle(getString(R.string.attention)).setNegativeButton(getString(R.string.not)) { dialog, int -> }
                .setPositiveButton(R.string.yes) { dialog, int ->
                    viewModel.tapOnCancelPrice(statusPrice = StatusPrice.CANCELED)
                }.show()
        }

        binding.btnFinishPrice.setOnClickListener {
            val alert = AlertDialog.Builder(this, R.style.MyDialogTheme)
            alert.setMessage(
                getString(
                    R.string.confirmation_finish_price
                )
            ).setTitle(getString(R.string.attention)).setNegativeButton(getString(R.string.not)) { dialog, int -> }
                .setPositiveButton(R.string.yes) { dialog, int ->
                    viewModel.tapOnCancelPrice(statusPrice = StatusPrice.FINISHED)
                }.show()

        }
    }

    private fun setupView() {
        binding.recyclerViewAllProducts.adapter = allProductsPriceAdapter
        allProductsPriceAdapter.isEditable = false
    }

    override fun onResume() {
        if(user == null){
            //criar uma viewmodel e se for nulo buscar o user da mesma forma que o login buscou para passar para cá
            finish()
        }
        super.onResume()
    }

    companion object {
        const val CODE_PRICE_SHOW = "CODE_PRICE_SHOW"
        const val CNPJ_USER = "CNPJ_USER"
        const val UPDATE_PRICES = 557
    }
}