package com.example.cotacaofacil.domain.usecase.product.contract

interface GetAllListSpinnerOptionsUseCase {
    fun invoke() : MutableList<String>
}