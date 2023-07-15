package com.example.cotacaofacil.presentation.ui.fragment.buyer

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.cotacaofacil.R
import com.example.cotacaofacil.databinding.FragmentCreatePriceBinding
import com.example.cotacaofacil.presentation.ui.adapter.PartnerCreatePriceAdapter
import com.example.cotacaofacil.presentation.ui.dialog.EditDateFinishPriceDialog
import com.example.cotacaofacil.presentation.ui.extension.animateCloseEffect
import com.example.cotacaofacil.presentation.ui.extension.animateOpenEffect
import com.example.cotacaofacil.presentation.ui.extension.toDateTimeLong
import com.example.cotacaofacil.presentation.ui.fragment.buyer.CreatePriceSelectProductsFragment.Companion.PRICE_MODEL
import com.example.cotacaofacil.presentation.viewmodel.buyer.price.CreatePriceViewModel
import com.example.cotacaofacil.presentation.viewmodel.buyer.price.contractCreatePrice.CreatePriceEvent
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePriceFragment : Fragment() {
    private lateinit var binding: FragmentCreatePriceBinding
    private val viewModel by viewModel<CreatePriceViewModel>()
    private val adapter = PartnerCreatePriceAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatePriceBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.createPriceEventLiveData.observe(viewLifecycleOwner) { event ->
            when (event) {
                CreatePriceEvent.AutoChecked -> {
                    binding.constraintLayoutEditDateAndHour.animateOpenEffect()
                }

                CreatePriceEvent.NotAutoChecked -> {
                    binding.constraintLayoutEditDateAndHour.animateCloseEffect()

                }
                CreatePriceEvent.CreatePrice -> {
                    Toast.makeText(context, "proxmo", Toast.LENGTH_SHORT).show()
                }
                is CreatePriceEvent.UpdateListEvent -> {
                    adapter.updateList(event.partnerList)
                }
                is CreatePriceEvent.ChangeSwitch -> {
                    binding.switchAllPartner.isChecked = event.isChecked
                }
                CreatePriceEvent.AllPartnersWithEffect -> {
                    binding.constraintLayoutSelectPartners.animateCloseEffect()
                    adapter.updateCheck(true)
                }
                CreatePriceEvent.NotAllPartnersWithEffect -> {
                    binding.constraintLayoutSelectPartners.animateOpenEffect()
                    adapter.updateCheck(false)
                }
                is CreatePriceEvent.AllPartners -> {
                    adapter.updateCheck(true)
                }
                is CreatePriceEvent.NotAllPartners -> {
                    adapter.updateCheck(false)
                }
                is CreatePriceEvent.SuccessNext -> {
                    val navOptions = NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_in_right)
                        .setExitAnim(R.anim.slide_out_left)
                        .setPopEnterAnim(R.anim.slide_out_right)
                        .setPopExitAnim(R.anim.slide_in_left)
                        .build()
                    val bundle = Bundle()
                    bundle.putParcelable(PRICE_MODEL, event.priceModel)
                    findNavController().navigate(R.id.createPriceSelectProductsFragment, bundle, navOptions)

                }
            }
        }

        viewModel.createPriceStateLiveData.observe(viewLifecycleOwner) { state ->
            adapter.listPartner = state.listPartnersSelect
            binding.recycleViewPartner.adapter = adapter
            binding.progressBar.isVisible = state.showProgressBar
            binding.textViewMessageErrorPartners.text = state.messageErrorPartners
            binding.buttonDateAndHour.text = state.dateFinishPrice
            binding.buttonDateAndHourDelivery.text = state.dateDelivery
            binding.textViewMessageError.text = state.messageError
        }
    }

    private fun setupListeners() {
        binding.buttonNext.setOnClickListener {
            val autoClose = binding.switchAutoClose.isChecked
            val allowAllPartners = binding.switchAllPartner.isChecked
            val dateFinish = binding.buttonDateAndHour.text.toString().toDateTimeLong()
            val dateDelivery = binding.buttonDateAndHourDelivery.text.toString().toDateTimeLong()
            val quantityPartners = adapter.listPartner
            val priority = viewModel.selectPriority(binding.radioButton.isChecked, binding.radioButton2.isChecked, binding.radioButton3.isChecked)
            val description = binding.editTextDescription.text.toString()
            viewModel.tapOnButtonNext(autoClose, allowAllPartners, dateFinish, dateDelivery, quantityPartners, description, priority)
        }

        adapter.clickPartner = {
            viewModel.checkAllPartners(adapter.listPartner, false)
        }

        binding.ButtonSelectAll.setOnClickListener {
            viewModel.tapOnSelectAll(binding.switchAllPartner.isChecked)
        }

        binding.buttonDateAndHourDelivery.setOnClickListener {
            EditDateFinishPriceDialog.newInstance(binding.buttonDateAndHourDelivery.text.toString().toDateTimeLong(), 24) {
                viewModel.updateHourFinishPrice(it, false)
            }.show(childFragmentManager, "")
        }

        binding.buttonDateAndHour.setOnClickListener {
            EditDateFinishPriceDialog.newInstance(binding.buttonDateAndHour.text.toString().toDateTimeLong(), 1) {
                viewModel.updateHourFinishPrice(it, true)
            }.show(childFragmentManager, "")
        }
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Chamado antes de alterar o texto no EditText
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Chamado quando o texto no EditText está sendo alterado
                // Você pode acessar o texto atual com 's' e realizar ações com base nele

            }

            override fun afterTextChanged(s: Editable?) {
                // Chamado depois de alterar o texto no EditText
                viewModel.filterSearch(s)
            }
        })

        binding.switchAutoClose.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.tapOnSwitchAutoClose(isChecked)
        }

        binding.switchAllPartner.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.tapOnSwitchAllPartners(isChecked, adapter.listPartner)
        }
    }
}