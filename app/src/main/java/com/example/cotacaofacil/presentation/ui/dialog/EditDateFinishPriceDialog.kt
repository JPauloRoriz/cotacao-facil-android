package com.example.cotacaofacil.presentation.ui.dialog

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.cotacaofacil.R
import com.example.cotacaofacil.databinding.DialogDateHourBinding
import com.example.cotacaofacil.presentation.viewmodel.buyer.price.EditDateHourViewModel
import com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractDateHourPrice.DateEvent
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class EditDateFinishPriceDialog : DialogFragment() {
    private var _binding: DialogDateHourBinding? = null
    private val binding get() = _binding
    var hour: Int = 0
    var minuteOfDay: Int = 0
    private val viewModel by viewModel<EditDateHourViewModel>()
    var clickSave: ((Long) -> Unit)? = null
    var dateFinishPrice: Long? = null
    var hourDistanceMin: Int = 0

    companion object {
        fun newInstance(dateFinishPrice: Long, hourDistanceMin: Int, clickSave: (Long) -> Unit): EditDateFinishPriceDialog {
            val dialog = EditDateFinishPriceDialog()
            dialog.dateFinishPrice = dateFinishPrice
            dialog.clickSave = clickSave
            dialog.hourDistanceMin = hourDistanceMin
            return dialog
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogDateHourBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupView()
        setupObservers()
    }

    private fun setupView() {
        viewModel.setupView(dateFinishPrice, hour)
    }

    private fun setupListeners() {
        binding?.timePicker?.setOnTimeChangedListener { _, hourOfDay, minute ->
            hour = hourOfDay
            minuteOfDay = minute
            viewModel.changeHour(hourOfDay)
        }

        binding?.buttonSaveDate?.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                viewModel.tapOnSaveDate(
                    binding?.datePicker?.year,
                    binding?.datePicker?.month,
                    binding?.datePicker?.dayOfMonth,
                    binding?.timePicker?.hour,
                    binding?.timePicker?.minute,
                    hourDistanceMin
                )
            } else {
                viewModel.tapOnSaveDate(
                    binding?.datePicker?.year,
                    binding?.datePicker?.month,
                    binding?.datePicker?.dayOfMonth,
                    hour,
                    minuteOfDay,
                    hourDistanceMin
                )
            }
        }
    }

    private fun setupObservers() {
        viewModel.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                is DateEvent.UpdateDate -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        binding?.timePicker?.hour = event.hour.get(Calendar.HOUR_OF_DAY)
                        binding?.timePicker?.minute = event.hour.get(Calendar.MINUTE)
                    }

                }
                is DateEvent.SaveDate -> {
                    clickSave?.invoke(event.date)
                    Toast.makeText(requireContext(), getString(R.string.date_and_hout_save), Toast.LENGTH_SHORT).show()
                    this.dismiss()
                }
            }

        }
        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding?.textViewMessageError?.text = state.messageError
//            binding?.datePicker?.updateDate(
//                state.date.get(Calendar.YEAR),
//                state.date.get(Calendar.MONTH),
//                state.date.get(Calendar.DAY_OF_MONTH)
//            )

            val empty = 0
            if (state.isNight)
                binding?.textViewIconHour?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.icon_night,
                    empty,
                    empty,
                    empty
                )
            else
                binding?.textViewIconHour?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.icon_sun,
                    empty,
                    empty,
                    empty
                )
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}