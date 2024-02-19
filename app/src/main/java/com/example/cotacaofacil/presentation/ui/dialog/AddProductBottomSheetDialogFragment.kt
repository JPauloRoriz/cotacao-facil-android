package com.example.cotacaofacil.presentation.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.FragmentManager
import com.example.cotacaofacil.R
import com.example.cotacaofacil.databinding.BottomSheetAddProductBinding
import com.example.cotacaofacil.domain.model.ProductModel
import com.example.cotacaofacil.presentation.viewmodel.product.AddProductViewModel
import com.example.cotacaofacil.presentation.viewmodel.product.model.ProductAddEvent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddProductBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetAddProductBinding
    private val viewModel by viewModel<AddProductViewModel>()
    var cnpjUser: String? = ""
    var productModel: ProductModel? = null
    var updateListProducts: (() -> Unit)? = null
    var dismissDialog: (() -> Unit)? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BottomSheetAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle);

    }

    companion object {
        fun newInstance(cnpjUser: String?, productModel: ProductModel?, updateListProducts: () -> Unit, dismissDialog : () -> Unit): AddProductBottomSheetDialogFragment {
            val addProductBottomSheetDialogFragment = AddProductBottomSheetDialogFragment()
            addProductBottomSheetDialogFragment.cnpjUser = cnpjUser
            addProductBottomSheetDialogFragment.productModel = productModel
            addProductBottomSheetDialogFragment.updateListProducts = updateListProducts
            addProductBottomSheetDialogFragment.dismissDialog = dismissDialog
            return addProductBottomSheetDialogFragment
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (isAdded) return
        super.show(manager, tag)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.skipCollapsed = true
//        dialog.behavior.isHideable = true
        dialog.behavior.isDraggable = true

        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupListeners()
        setupObservers()
    }

    private fun setupView() {
        viewModel.createOrEdit(productModel)
    }

    private fun setupSpinner(listOptions: MutableList<String>, typeMeasurements: String) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        if(typeMeasurements.isNotEmpty()){
            binding.spinner.setSelection(listOptions.indexOf(typeMeasurements))
        }
    }

    private fun setupListeners() {
        binding.imageViewFavorite.setOnClickListener {
            viewModel.tapOnIconFavorite(
                binding.imageViewFavorite.isSelected,
                productModel, binding.edtNameProduct.text.toString(),
                binding.edtDescription.text.toString(),
                binding.edtBrand.text.toString(),
                binding.spinner.selectedItem.toString(),
                binding.edtQuantity.text.toString()
                )
        }

        binding.btnSaveProduct.setOnClickListener {
            cnpjUser?.let { cnpjUser ->
                viewModel.tapOnSaveProduct(
                    binding.edtNameProduct.text.toString(),
                    binding.edtDescription.text.toString(),
                    binding.edtBrand.text.toString(),
                    binding.spinner.selectedItem.toString(),
                    cnpjUser,
                    binding.edtQuantity.text.toString(),
                    false,
                    binding.imageViewFavorite.isSelected,
                    productModel
                )
            }
        }

        binding.imageViewTrash.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.delete_product))
            builder.setPositiveButton(R.string.yes) { p0, p1 ->
                viewModel.tapOnTrash(productModel)
            }
            builder.setNegativeButton(R.string.not) { p0, p1 ->
            }
            builder.create().show()
        }
    }

    private fun setupObservers() {
        viewModel.eventLiveData.observe(viewLifecycleOwner) { event ->
            when (event) {
                is ProductAddEvent.ShowDialogConfirmationDataEmpty -> {
                    val alert = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)
                    alert.setMessage(
                        getString(
                            R.string.confirmation_save_product_data_empty
                        )
                    ).setTitle(getString(R.string.attention)).setNegativeButton(
                        getString(R.string.not)
                    ) { dialog, int -> }.setPositiveButton(
                        R.string.yes
                    ) { dialog, int ->
                            viewModel.tapOnSaveProduct(
                                event.name,
                                event.description,
                                event.brand,
                                event.typeMeasurements,
                                event.cnpjUser,
                                event.quantity,
                                true,
                                binding.imageViewFavorite.isSelected,
                                productModel
                            )
                    }.show()
                }

                is ProductAddEvent.ModificationProduct -> {
                    updateListProducts?.invoke()
                    Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                    this.dismiss()
                }

            }
        }

        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            setupSpinner(state.listSpinner, state.typeMeasurement)
            binding.titleAddProduct.text = state.titleBottomNavigation
            binding.tvMessageError.text = state.messageError
            binding.edtNameProduct.setText(state.nameText)
            binding.edtDescription.setText(state.descriptionText)
            binding.edtBrand.setText(state.brandText)
            binding.edtQuantity.setText(state.quantityText)
            binding.btnSaveProduct.text = state.textButton
            binding.imageViewFavorite.isSelected = state.isFavorite
            binding.imageViewTrash.isGone = state.trashIsGone
        }
    }

    override fun dismiss() {
        dismissDialog?.invoke()
        super.dismiss()
    }

    override fun onDestroy() {
        dismissDialog?.invoke()
        super.onDestroy()
    }

}