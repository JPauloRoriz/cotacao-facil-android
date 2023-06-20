package com.example.cotacaofacil.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cotacaofacil.databinding.ItemPartnerPriceBinding
import com.example.cotacaofacil.domain.Extensions.Companion.formatCnpj
import com.example.cotacaofacil.domain.Extensions.Companion.ifNotEmpty
import com.example.cotacaofacil.domain.model.PartnerModel

class PartnerCreatePriceAdapter : RecyclerView.Adapter<PartnerCreatePriceAdapter.PartnerCreatePriceViewHolder>() {
    var listPartner : MutableList<PartnerModel> = mutableListOf()
    var clickPartner : ((PartnerModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnerCreatePriceViewHolder {
        val binding = ItemPartnerPriceBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PartnerCreatePriceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PartnerCreatePriceViewHolder, position: Int) {
        holder.bind(position, listPartner[position])
    }

    override fun getItemCount(): Int {
        return listPartner.size
    }

    fun updateList(partnerList: MutableList<PartnerModel>) {
        listPartner.clear()
        listPartner.addAll(partnerList)
        notifyDataSetChanged()
    }

    fun updateCheck(isCheck: Boolean) {
        listPartner.forEach {
            it.isChecked = isCheck
        }
        notifyDataSetChanged()
    }


    inner class PartnerCreatePriceViewHolder(val binding: ItemPartnerPriceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, partnerModel: PartnerModel) {
            binding.textViewCnpj.text = partnerModel.cnpjCorporation.formatCnpj()
            binding.textViewNamePartner.text = partnerModel.nameFantasy.ifNotEmpty()
            binding.textViewNameCompletePartner.text = partnerModel.nameCorporation.ifNotEmpty()
            binding.checkBoxIsSelected.isChecked = partnerModel.isChecked


            binding.checkBoxIsSelected.setOnClickListener {
                partnerModel.isChecked = binding.checkBoxIsSelected.isChecked
                clickPartner?.invoke(partnerModel)
                notifyItemChanged(position)
            }

            binding.root.setOnClickListener {
                partnerModel.isChecked = !binding.checkBoxIsSelected.isChecked
                clickPartner?.invoke(partnerModel)
                notifyItemChanged(position)
            }
        }

    }
}