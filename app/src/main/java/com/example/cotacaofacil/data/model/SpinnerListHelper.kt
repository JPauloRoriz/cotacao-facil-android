package com.example.cotacaofacil.data.model

import android.content.Context
import com.example.cotacaofacil.R

class SpinnerListHelper {
    fun getListSpinnerOptionsMeasurements(context: Context): MutableList<String> {
        return mutableListOf(
            context.getString(R.string.kg),
            context.getString(R.string.grams),
            context.getString(R.string.tons),
            context.getString(R.string.liter),
            context.getString(R.string.unity),
            context.getString(R.string.meeters),
            context.getString(R.string.box),
            context.getString(R.string.bales),
            context.getString(R.string.tins),
            context.getString(
                R.string.others
            )
        )
    }
}


