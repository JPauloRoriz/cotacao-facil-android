package com.example.cotacaofacil.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cotacaofacil.R
import com.example.cotacaofacil.databinding.ItemProductSelectPriceBinding
import com.example.cotacaofacil.domain.model.ProductPriceModel

class ProductSelectPriceAdapter : RecyclerView.Adapter<ProductSelectPriceAdapter.ViewHolder>() {
    var products: MutableList<ProductPriceModel> = mutableListOf()
    var clickProduct: ((ProductPriceModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemProductSelectPriceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product, position)
    }

    override fun getItemCount() = products.size

    fun updateList(products: MutableList<ProductPriceModel>) {
        this.products = products
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemProductSelectPriceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(productPrice: ProductPriceModel, position: Int) {
            binding.checkBoxIsSelected.isChecked = productPrice.isSelected
            binding.textViewCodeProduct.text = "Cód: ${productPrice.productModel.code}"
            binding.textViewTextDescription.text = productPrice.productModel.description

            binding.cardViewItemProduct.setOnClickListener {
                productPrice.isSelected = !productPrice.isSelected
                binding.checkBoxIsSelected.isChecked = productPrice.isSelected
                clickProduct?.invoke(productPrice)
            }

            binding.checkBoxIsSelected.setOnClickListener {
                productPrice.isSelected = binding.checkBoxIsSelected.isChecked
                clickProduct?.invoke(productPrice)
                notifyItemChanged(position)
            }

            if (productPrice.productModel.quantity.isEmpty() || productPrice.productModel.typeMeasurement == binding.root.context.getString(R.string.other)) {
                binding.textViewNameProduct.text = "${productPrice.productModel.name} ${productPrice.productModel.brand}"
            } else {
                binding.textViewNameProduct.text =
                    "${productPrice.productModel.name} ${productPrice.productModel.brand} - ${productPrice.productModel.quantity} ${productPrice.productModel.typeMeasurement} "
            }


        }
    }
}