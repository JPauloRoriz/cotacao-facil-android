package com.example.cotacaofacil.domain.usecase.date.contract

interface CalculationDateFinishPriceUseCase {
    fun invoke(date : Long) : String
}