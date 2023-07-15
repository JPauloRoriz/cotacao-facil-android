package com.example.cotacaofacil.presentation.ui.dialog

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.cotacaofacil.R
import com.example.cotacaofacil.databinding.DialogConfirmationCreatePriceBinding

class ConfirmationCreatePriceDialog : DialogFragment() {

    private var _binding: DialogConfirmationCreatePriceBinding? = null
    private val binding get() = _binding
    var codePrice = ""
    private var confirmationDialog: ConfirmationCreatePriceDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogConfirmationCreatePriceBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupView()
    }

    private fun setupView() {
        binding?.textViewDescription?.text = Html.fromHtml(getString(R.string.continue_description, codePrice))
    }

    companion object {
        fun newInstance(codePrice: String): ConfirmationCreatePriceDialog {
            val dialog = ConfirmationCreatePriceDialog()
            dialog.codePrice = codePrice
            return dialog
        }
    }

    override fun getTheme(): Int {
        return R.style.RoundedCornersDialog
    }

    private fun setupListeners() {
        binding?.buttonContinue?.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.dismiss()
    }
}