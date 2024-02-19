package com.example.cotacaofacil.presentation.util

import android.text.Editable
import android.text.TextWatcher

import android.widget.EditText
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


object Maks {
    private const val maskCNPJ = "##.###.###/####-##"
    private const val maxSizeValueProduct = 14

    fun unmask(s: String): String {
        val r: String
        return s.replace("[^0-9]*".toRegex(), "")
    }

    fun insert(editText: EditText): TextWatcher {
        return object : TextWatcher {
            var isUpdating = false
            var old = ""
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val str = unmask(s.toString())
                val mask: String
                val defaultMask = getDefaultMask(str)
                mask = when (str.length) {
                    14 -> maskCNPJ
                    else -> defaultMask
                }
                var mascara = ""
                if (isUpdating) {
                    old = str
                    isUpdating = false
                    return
                }
                var i = 0
                for (m in mask.toCharArray()) {
                    if (m != '#' && str.length > old.length || m != '#' && str.length < old.length && str.length != i) {
                        mascara += m
                        continue
                    }
                    mascara += try {
                        str[i]
                    } catch (e: Exception) {
                        break
                    }
                    i++
                }
                isUpdating = true
                editText.setText(mascara)
                editText.setSelection(mascara.length)
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {}
        }
    }

    private fun getDefaultMask(str: String): String {
        var defaultMask = maskCNPJ
        if (str.length > 11) {
            defaultMask = maskCNPJ
        }
        return defaultMask
    }


    fun EditText.addCurrencyMask() {
        this.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // Não é necessário implementar antes da mudança de texto
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                // Não é necessário implementar durante a mudança de texto
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != current) {
                    this@addCurrencyMask.removeTextChangedListener(this)

                    val cleanString = s.toString().replace("\\D".toRegex(), "")

                    val parsed: Double = try {
                        cleanString.toDouble()
                    } catch (e: NumberFormatException) {
                        0.0
                    }

                    // Remova a formatação de moeda, mantendo apenas o valor numérico
                    val formatted = String.format("%.2f", parsed / 100)

                    current = formatted

                    if (formatted.length < maxSizeValueProduct) {
                        this@addCurrencyMask.setText(formatted)
                        this@addCurrencyMask.setSelection(formatted.length)
                        this@addCurrencyMask.addTextChangedListener(this)
                    }
                }
            }
        })
    }

    fun formatValueMoney(valor: Double): String {
        val formatMoney = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        return formatMoney.format(valor)
    }
}