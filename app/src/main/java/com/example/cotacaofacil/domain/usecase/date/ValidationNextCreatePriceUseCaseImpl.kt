package com.example.cotacaofacil.domain.usecase.date

import com.example.cotacaofacil.domain.Extensions.Companion.verifyAutoClose
import com.example.cotacaofacil.domain.exception.*
import com.example.cotacaofacil.domain.model.PartnerModel
import com.example.cotacaofacil.domain.model.PriceModel
import com.example.cotacaofacil.domain.model.PriorityPrice
import com.example.cotacaofacil.domain.model.StatusPrice
import com.example.cotacaofacil.domain.usecase.date.contract.ValidationNextCreatePriceUseCase
import java.util.*

class ValidationNextCreatePriceUseCaseImpl : ValidationNextCreatePriceUseCase {
    override fun invoke(
        autoClose: Boolean,
        allowAllPartners: Boolean,
        date: Long?,
        dateDelivery: Long,
        partners: MutableList<PartnerModel>,
        description: String,
        priority: PriorityPrice?,
        currentDate: Long
    ): Result<PriceModel> {
        var exception: Exception? = null
        val partnersSelect = partners.filter { it.isChecked }

        if (autoClose && date != null) {
            val currentDateOneHourLater = Calendar.getInstance()
            currentDateOneHourLater.timeInMillis = currentDate
            currentDateOneHourLater.add(Calendar.HOUR_OF_DAY, 1)
            exception = if (date < currentDate) {
                ScheduleDateForPastException()
            } else if (date < currentDateOneHourLater.timeInMillis) {
                StLeastOneHourException()
            } else {
                null
            }
        } else if (partnersSelect.size < 2) {
            exception = MinTwoProvidersInPriceException()
        } else if (priority == null) {
            exception = PriorityIsEmptyException()
        }
        if (exception == null) {
            val currentDateOneHourLater = Calendar.getInstance()
            currentDateOneHourLater.timeInMillis = currentDate
            currentDateOneHourLater.add(Calendar.HOUR_OF_DAY, 24)
            exception = if (dateDelivery < currentDate) {
                ScheduleDateForPastException()
            } else if (dateDelivery < currentDateOneHourLater.timeInMillis) {
                StLeastOneDayException()
            } else {
                null
            }
        }
        return if (exception == null) {
            Result.success(
                PriceModel(
                    code = "",
                    productsPrice = mutableListOf(),
                    partnersAuthorized = partners,
                    dateStartPrice = currentDate,
                    dateFinishPrice = date?.verifyAutoClose(autoClose),
                    priority = PriorityPrice.AVERAGE,
                    cnpjBuyerCreator = "",
                    closeAutomatic = autoClose,
                    allowAllProvider = allowAllPartners,
                    deliveryDate = dateDelivery,
                    description = description,
                    status = StatusPrice.OPEN

                )
            )
        } else {
            Result.failure(exception)
        }

    }
}