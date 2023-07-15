package com.example.cotacaofacil.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cotacaofacil.R
import com.example.cotacaofacil.databinding.ItemPriceBuyerBinding
import com.example.cotacaofacil.domain.model.PriceModel
import com.example.cotacaofacil.presentation.ui.extension.dateEmpty
import com.example.cotacaofacil.presentation.ui.extension.toFormattedDateTime
import com.example.cotacaofacil.presentation.ui.extension.toTextStatus

class ItemPriceBuyerAdapter : RecyclerView.Adapter<ItemPriceBuyerAdapter.ItemPriceBuyerViewHolder>() {
    var listPriceModel: MutableList<PriceModel> = mutableListOf()
//    var updateQuantity: ((MutableList<ProductPriceModel>) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPriceBuyerViewHolder {
        val binding = ItemPriceBuyerBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemPriceBuyerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemPriceBuyerViewHolder, position: Int) {
        holder.bind(position, listPriceModel[position])
    }

    override fun getItemCount(): Int {
        return listPriceModel.size
    }

    fun updateList(productsModel: MutableList<PriceModel>) {
        listPriceModel.clear()
        listPriceModel.addAll(productsModel)
        notifyDataSetChanged()
    }


    inner class ItemPriceBuyerViewHolder(val binding: ItemPriceBuyerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, priceModel: PriceModel) {
            binding.tvCodePrice.text = binding.root.context.getString(R.string.code_adapter_price, priceModel.code)
            binding.tvClosingDateLabel.text =
                binding.root.context.getString(R.string.date_init_price_adapter_price, priceModel.dateStartPrice.toFormattedDateTime())
            binding.tvCreationDateLabel.text = priceModel.dateFinishPrice?.dateEmpty(binding.root.context, priceModel.closeAutomatic)


            binding.tvStatus.text = priceModel.status.toTextStatus(binding.root.context)
            binding.tvClosureType.text = "0"
            binding.tvProductQuantity.text = priceModel.productsPrice.size.toString()
        }
    }
}