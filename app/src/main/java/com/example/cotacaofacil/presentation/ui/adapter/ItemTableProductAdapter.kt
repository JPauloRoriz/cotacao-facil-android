package com.example.cotacaofacil.presentation.ui.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.cotacaofacil.R
import com.example.cotacaofacil.databinding.ItemTableProductBinding
import com.example.cotacaofacil.domain.model.ProductPriceEditPriceModel


class ItemTableProductAdapter : RecyclerView.Adapter<ItemTableProductAdapter.ItemTableProductViewHolder>() {
    var listTableProduct: MutableList<ProductPriceEditPriceModel> = mutableListOf()
    var updateQuantity: ((MutableList<ProductPriceEditPriceModel>) -> Unit)? = null
    var isEditable: Boolean = true
    var clickItem: ((ProductPriceEditPriceModel) -> Unit)? = null
    var positionSelect: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTableProductViewHolder {
        val binding = ItemTableProductBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemTableProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemTableProductViewHolder, position: Int) {
        holder.bind(position, listTableProduct[position])
    }

    override fun getItemCount(): Int {
        return listTableProduct.size
    }

    fun updateList(itemTableProductList: MutableList<ProductPriceEditPriceModel>) {
        listTableProduct.clear()
        listTableProduct.addAll(itemTableProductList)
        notifyDataSetChanged()
    }

    fun updateProduct(position: Int) {
        positionSelect = position
        notifyItemChanged(position)
    }


    inner class ItemTableProductViewHolder(val binding: ItemTableProductBinding) : RecyclerView.ViewHolder(binding.root) {
        val context: Context = binding.root.context
        fun bind(position: Int, itemTableProductModel: ProductPriceEditPriceModel) {
            setNameProduct(itemTableProductModel)
            setTextQuantity(itemTableProductModel)

            if (isEditable) {
                setEditValue(position)
                binding.editTextQuantity.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                    override fun afterTextChanged(s: Editable?) {
                        setEditableEditText(itemTableProductModel)
                    }
                })
                colorClicked(context, binding, R.color.colorPrimary, R.color.white)
                binding.imageViewCheck.visibility = View.GONE
            } else {
                binding.imageViewCheck.isVisible = itemTableProductModel.price > 0.0
                binding.editTextQuantity.isEnabled = false
                if (position == positionSelect) {
                    colorClicked(context, binding, R.color.colorPrimary, R.color.white)
                } else {
                    colorClicked(context, binding, R.color.gray_price, R.color.colorPrimary)
                }
            }
            binding.cardView.setOnClickListener {
                if (isEditable.not()) {
                    positionSelect = position
                    notifyDataSetChanged()
                    clickItem?.invoke(itemTableProductModel)
                }
            }
        }

        private fun setNameProduct(itemTableProductModel: ProductPriceEditPriceModel) {
            if (itemTableProductModel.productModel.quantity.isEmpty() || itemTableProductModel.productModel.typeMeasurement == "Outros") {
                binding.tvNameProduct.text = "${itemTableProductModel.productModel.name} ${itemTableProductModel.productModel.brand}"
            } else {
                binding.tvNameProduct.text =
                    "${itemTableProductModel.productModel.name} ${itemTableProductModel.productModel.brand} - ${itemTableProductModel.productModel.quantity} ${itemTableProductModel.productModel.typeMeasurement} "
            }
            binding.editTextQuantity.setText(itemTableProductModel.quantityProducts.toString())
        }

        private fun setTextQuantity(itemTableProductModel: ProductPriceEditPriceModel) {
            if (binding.editTextQuantity.text.toString().isNotEmpty()) {
                itemTableProductModel.quantityProducts = binding.editTextQuantity.text.toString().toInt()
            } else {
                itemTableProductModel.quantityProducts = 1
            }
            binding.textViewIndex.text = itemTableProductModel.productModel.code
        }

        private fun setEditValue(position: Int) {
            if (position == 0) {
                binding.editTextQuantity.requestFocus()
                val imm = binding.editTextQuantity.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
            }
        }

        private fun setEditableEditText(itemTableProductModel: ProductPriceEditPriceModel) {
            binding.editTextQuantity.setSelection(binding.editTextQuantity.text?.length ?: 1)
            if (binding.editTextQuantity.text.toString().isNotEmpty()) {
                itemTableProductModel.quantityProducts = binding.editTextQuantity.text.toString().toInt()
                updateQuantity?.invoke(listTableProduct)
            } else {
                itemTableProductModel.quantityProducts = 1
                updateQuantity?.invoke(listTableProduct)
            }
        }

    }

    private fun colorClicked(context: Context, binding: ItemTableProductBinding, color: Int, colorTextCode: Int) {
        val colorCompat = ContextCompat.getColor(context, color)
        binding.viewEndLine.setBackgroundColor(colorCompat)
        binding.viewBottomLine.setBackgroundColor(colorCompat)
        binding.viewTopLine.setBackgroundColor(colorCompat)
        binding.viewStartLine.setBackgroundColor(colorCompat)
        binding.textViewIndex.setBackgroundColor(colorCompat)
        binding.editTextQuantity.setBackgroundColor(colorCompat)
        binding.textViewIndex.setTextColor(ContextCompat.getColor(context, colorTextCode))
    }
}
