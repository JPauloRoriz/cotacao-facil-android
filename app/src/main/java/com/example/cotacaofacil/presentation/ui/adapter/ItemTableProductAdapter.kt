package com.example.cotacaofacil.presentation.ui.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cotacaofacil.databinding.ItemTableProductBinding
import com.example.cotacaofacil.domain.model.ProductPriceModel


class ItemTableProductAdapter : RecyclerView.Adapter<ItemTableProductAdapter.ItemTableProductViewHolder>() {
    var listTableProduct: MutableList<ProductPriceModel> = mutableListOf()
    var updateQuantity: ((MutableList<ProductPriceModel>) -> Unit)? = null

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

    fun updateList(itemTableProductList: MutableList<ProductPriceModel>) {
        listTableProduct.clear()
        listTableProduct.addAll(itemTableProductList)
        notifyDataSetChanged()
    }


    inner class ItemTableProductViewHolder(val binding: ItemTableProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, itemTableProductModel: ProductPriceModel) {

            if (itemTableProductModel.productModel.quantity.isEmpty() || itemTableProductModel.productModel.typeMeasurement == "Outros") {
                binding.tvNameProduct.text = "${itemTableProductModel.productModel.name} ${itemTableProductModel.productModel.brand}"
            } else {
                binding.tvNameProduct.text =
                    "${itemTableProductModel.productModel.name} ${itemTableProductModel.productModel.brand} - ${itemTableProductModel.productModel.quantity} ${itemTableProductModel.productModel.typeMeasurement} "
            }

            if (binding.editTextQuantity.text.toString().isNotEmpty()) {
                itemTableProductModel.quantityProducts = binding.editTextQuantity.text.toString().toInt()
            } else {
                itemTableProductModel.quantityProducts = 1
            }
            binding.textViewIndex.text = itemTableProductModel.productModel.code

            if(position == 0){
                binding.editTextQuantity.requestFocus()
                val imm =binding.editTextQuantity.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
            }

            binding.editTextQuantity.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    binding.editTextQuantity.setSelection(binding.editTextQuantity.text?.length?:1)
                    if (binding.editTextQuantity.text.toString().isNotEmpty()) {
                        itemTableProductModel.quantityProducts = binding.editTextQuantity.text.toString().toInt()
                        updateQuantity?.invoke(listTableProduct)
                    } else {
                        itemTableProductModel.quantityProducts = 1
                        updateQuantity?.invoke(listTableProduct)
                    }
                }
            })
        }

    }
}
