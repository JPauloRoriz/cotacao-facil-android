package com.example.cotacaofacil.presentation.viewmodel.product.model

sealed class ProductAddEvent {
    data class GetListSpinner(val listOptions : MutableList<String>) : ProductAddEvent()
    object SuccessAddProductAdd : ProductAddEvent()
    data class ShowDialogConfirmationDataEmpty(
        val name: String,
        val description: String,
        val brand: String,
        val typeMeasurements: String,
        val cnpjUser: String,
        val quantity : String

    ) : ProductAddEvent()

}