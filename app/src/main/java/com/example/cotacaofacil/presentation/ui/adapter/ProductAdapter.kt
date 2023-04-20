package com.example.cotacaofacil.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cotacaofacil.databinding.ItemProductBinding
import com.example.cotacaofacil.domain.model.ProductModel


class ProductAdapter(private val products: List<ProductModel>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount() = products.size

    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductModel) {
            if(product.quantity.isEmpty() || product.typeMeasurement == "Outros"){
                binding.textViewNameProduct.text = "${product.name} ${product.brand}"
            }else {
                binding.textViewNameProduct.text = "${product.name} ${product.brand} ${product.typeMeasurement} ${product.quantity}"
            }
            binding.textViewCodeProduct.text = "Cód: ${product.code}"
            binding.textViewTextDescription.text = product.description
        }
    }

}

