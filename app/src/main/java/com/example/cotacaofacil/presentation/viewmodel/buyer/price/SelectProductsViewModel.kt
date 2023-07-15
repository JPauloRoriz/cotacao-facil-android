package com.example.cotacaofacil.presentation.viewmodel.buyer.price

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cotacaofacil.R
import com.example.cotacaofacil.data.helper.UserHelper
import com.example.cotacaofacil.domain.Extensions.Companion.toProductPriceModel
import com.example.cotacaofacil.domain.model.PriceModel
import com.example.cotacaofacil.domain.model.ProductModel
import com.example.cotacaofacil.domain.model.ProductPriceModel
import com.example.cotacaofacil.domain.usecase.product.contract.GetAllByCnpjProductsUseCase
import com.example.cotacaofacil.presentation.viewmodel.base.SingleLiveEvent
import com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractSelectProducts.SelectProductsEvent
import com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractSelectProducts.SelectProductsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SelectProductsViewModel(
    private val userHelper: UserHelper,
    private val productsUseCase: GetAllByCnpjProductsUseCase,
    private val getAllProductsUseCase: GetAllByCnpjProductsUseCase,
    private val context: Context,
    private val priceModel: PriceModel
) : ViewModel() {
    val stateLiveData = MutableLiveData(SelectProductsState())
    val eventLiveData = SingleLiveEvent<SelectProductsEvent>()

    var listAllProductsPrice: MutableList<ProductPriceModel> = mutableListOf()

    init {
        initViewModel()
    }

    private fun initViewModel() {
        stateLiveData.postValue(
            stateLiveData.value?.copy(
                isLoading = true,
                colorTextButtonNext = R.color.black,
                colorButtonNext = R.color.white_dark_button
            )
        )
        viewModelScope.launch(Dispatchers.IO) {
            userHelper.user?.cnpj?.let {
                productsUseCase.invoke(it)
                    .onSuccess { listProductModel ->
                        if (listProductModel.isEmpty()) {
                            stateLiveData.postValue(
                                stateLiveData.value?.copy(
                                    messageError = context.getString(R.string.products_empty_toast_add),
                                    showMessageError = true,
                                    isLoading = false,

                                    )
                            )
                        } else {
                            listAllProductsPrice = listProductModel.toProductPriceModel()
                            stateLiveData.postValue(
                                stateLiveData.value?.copy(
                                    messageError = "",
                                    products = listAllProductsPrice,
                                    showMessageError = false,
                                    isLoading = false,
                                )
                            )
                        }
                    }
                    .onFailure {

                    }
            }

        }
    }

    fun filterList(position: Int?, textSearch: String) {
        if (position == 1) {
            val listFavorites = stateLiveData.value?.products?.filter { it.productModel.isFavorite }?.toMutableList()

            if (textSearch.isNotEmpty()) {
                val result = mutableListOf<ProductPriceModel>()
                listFavorites?.forEach { productModel ->
                    if (productModel.productModel.code.contains(textSearch, true)) {
                        result.add(productModel)
                    }
                }
                stateLiveData.postValue(stateLiveData.value?.copy(products = result))
            } else {
                listFavorites?.let { stateLiveData.value?.copy(products = it) }
            }
        } else {
            if (textSearch.isNotEmpty()) {
                val result = mutableListOf<ProductPriceModel>()
                listAllProductsPrice.forEach { productModel ->
                    if (productModel.productModel.code.contains(textSearch, true)) {
                        result.add(productModel)
                    }
                }
                stateLiveData.postValue(stateLiveData.value?.copy(products = result))
            } else {
                stateLiveData.postValue(stateLiveData.value?.products?.filter { it.productModel.isFavorite }?.toMutableList()
                    ?.let { stateLiveData.value?.copy(products = listAllProductsPrice, messageError = "") })
            }
        }
    }

    fun tapOnNext() {
        val listSelects = listAllProductsPrice.filter { it.isSelected }.toMutableList()
        if (listSelects.isEmpty()) {
//            stateLiveData.postValue(stateLiveData.value?.copy(products =  listSelects) )
            eventLiveData.postValue(SelectProductsEvent.ErrorSelectMinOneProduct(context.getString(R.string.min_one_product_price)))
        } else {
            priceModel.productsPrice = listSelects.toMutableList()
            eventLiveData.postValue(SelectProductsEvent.Next(priceModel))
        }
    }

    private fun verifyButton() {
        val listSelects = listAllProductsPrice.filter { it.isSelected }
        if (listSelects.isEmpty()) {
            stateLiveData.postValue(stateLiveData.value?.copy(colorTextButtonNext = R.color.black, colorButtonNext = R.color.white_dark_button))
        } else {
            stateLiveData.postValue(stateLiveData.value?.copy(colorTextButtonNext = R.color.white, colorButtonNext = R.color.colorPrimary))
        }
    }

    fun tapOnSelectProduct(it: ProductPriceModel) {
        verifyButton()
    }

    fun updateListProducts(listProductsPrice : MutableList<ProductPriceModel>){
        eventLiveData.postValue(SelectProductsEvent.UpdateListProducts(listProductsPrice))
    }

    fun performSearch(query: String?, selectedTabPosition: Int, products: MutableList<ProductPriceModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            userHelper.user?.cnpj?.let {
                var newListProducts: MutableList<ProductModel>?
                getAllProductsUseCase.invoke(it)
                    .onSuccess { listProducts ->
                        if (query?.isNotEmpty() == true) {
                            newListProducts = if (selectedTabPosition == 1) {
                                listProducts.filter { it.isFavorite }.toMutableList()
                            } else {
                                listProducts
                            }
                            val result = mutableListOf<ProductModel>()
                            newListProducts?.forEach {
                                if (it.code.contains(query, true)) {
                                    result.add(it)
                                }

                            }
                            eventLiveData.postValue(SelectProductsEvent.UpdateListProducts(result.toProductPriceModel()))
                        } else {
                            if (selectedTabPosition == 1) {
                                val listFavorites = listProducts.filter { it.isFavorite }.toMutableList()
                                eventLiveData.postValue(SelectProductsEvent.UpdateListProducts(listFavorites.toProductPriceModel()))
                            } else {
                                eventLiveData.postValue(SelectProductsEvent.UpdateListProducts(listProducts.toProductPriceModel()))
                            }

                        }
                    }.onFailure {
                        stateLiveData.postValue(stateLiveData.value?.copy(messageError = context.getString(R.string.message_error_impossible_load_products_stock)))
                    }
            }
        }


    }

}