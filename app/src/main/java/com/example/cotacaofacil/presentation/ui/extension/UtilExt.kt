package com.example.cotacaofacil.presentation.ui.extension

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.cotacaofacil.R
import com.example.cotacaofacil.domain.model.PriorityPrice
import com.example.cotacaofacil.domain.model.StatusPrice

fun PriorityPrice.toTextPriority(context: Context): String {
    return when (this) {
        PriorityPrice.HIGH -> context.getString(R.string.high)
        PriorityPrice.AVERAGE -> context.getString(R.string.average)
        PriorityPrice.LOW -> context.getString(R.string.low)
    }
}
fun StatusPrice.toTextStatus(context: Context): String {
    return when (this) {
        StatusPrice.OPEN -> context.getString(R.string.open)
        StatusPrice.CANCELED -> context.getString(R.string.canceled)
        StatusPrice.FINISHED -> context.getString(R.string.finished)
    }
}

fun PriorityPrice.toColorPriority(context: Context): Drawable? {
    return when (this) {
        PriorityPrice.HIGH -> ContextCompat.getDrawable(context, R.drawable.ic_ball_red)
        PriorityPrice.AVERAGE -> ContextCompat.getDrawable(context, R.drawable.ic_ball_yellow)
        PriorityPrice.LOW -> ContextCompat.getDrawable(context, R.drawable.ic_ball_priority)
    }
}

fun String.notEmpty(context: Context): String {
    return this.ifEmpty {
        context.getString(R.string.not_description)
    }
}

fun Long.dateEmpty(context: Context, isCloseAutomatic: Boolean): String {
    return if (!isCloseAutomatic) {
        context.getString(R.string.finish_price_not_auto)
    } else {
        context.getString(R.string.date_finish_price_adapter_price, toFormattedDateTime())
    }
}

