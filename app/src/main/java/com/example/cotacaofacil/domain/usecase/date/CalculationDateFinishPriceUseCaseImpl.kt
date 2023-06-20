package com.example.cotacaofacil.domain.usecase.date

import com.example.cotacaofacil.domain.usecase.date.contract.CalculationDateFinishPriceUseCase
import com.example.cotacaofacil.presentation.ui.extension.toFormattedDateTime
import java.util.*

class CalculationDateFinishPriceUseCaseImpl : CalculationDateFinishPriceUseCase {
    override fun invoke(date : Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        calendar.add(Calendar.DAY_OF_YEAR, 2)
        return calendar.timeInMillis.toFormattedDateTime()
    }
}