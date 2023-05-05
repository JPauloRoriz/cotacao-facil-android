package com.example.cotacaofacil.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cotacaofacil.databinding.ItemProductBinding
import com.example.cotacaofacil.domain.model.ProductModel
import com.example.cotacaofacil.presentation.viewmodel.product.model.StockEvent


class ProductAdapter() :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    var products: List<ProductModel> = mutableListOf()

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
    fun updateList(products: MutableList<ProductModel>) {
        this.products = products
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductModel) {
            if(product.quantity.isEmpty() || product.typeMeasurement == "Outros"){
                binding.textViewNameProduct.text = "${product.name} ${product.brand}"
            }else {
                binding.textViewNameProduct.text = "${product.name} ${product.brand} ${product.quantity} ${product.typeMeasurement} "
            }
            binding.textViewCodeProduct.text = "Cód: ${product.code}"
            binding.textViewTextDescription.text = product.description
        }
    }

}

