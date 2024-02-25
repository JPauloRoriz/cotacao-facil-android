package com.example.cotacaofacil.presentation.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.cotacaofacil.R
import com.example.cotacaofacil.databinding.ItemPartnerBinding
import com.example.cotacaofacil.domain.Extensions.Companion.formatCnpj
import com.example.cotacaofacil.domain.Extensions.Companion.ifNotEmpty
import com.example.cotacaofacil.domain.model.PartnerModel
import com.example.cotacaofacil.domain.model.StatusIsMyPartner

class PartnerAdapter : RecyclerView.Adapter<PartnerAdapter.PartnerViewHolder>() {
    var listPartner : MutableList<PartnerModel> = mutableListOf()
    var clickPartner : ((PartnerModel) -> Unit)? = null
    var clickAcceptPartner : ((PartnerModel) -> Unit)? = null
    var clickRejectPartner : ((PartnerModel) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnerViewHolder {
        val binding = ItemPartnerBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PartnerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PartnerViewHolder, position: Int) {
        holder.bind(listPartner[position])
    }

    override fun getItemCount(): Int {
        return listPartner.size
    }


    inner class PartnerViewHolder(val binding: ItemPartnerBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(partnerModel: PartnerModel) {
                Glide.with(binding.root.context)
                    .load(partnerModel.imageProfile)
                    .apply(RequestOptions().transform(CircleCrop()))
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(binding.imageViewUser)
                binding.imageViewAddPartner.visibility = View.VISIBLE
                binding.constraintLayoutAcceptPartner.visibility = View.GONE
                binding.imageViewAddPartner.setOnClickListener {
                    clickPartner?.invoke(partnerModel)
                }
                binding.imageButtonPositive.setOnClickListener {
                    clickAcceptPartner?.invoke(partnerModel)
                }
                binding.imageButtonNegative.setOnClickListener {
                    clickRejectPartner?.invoke(partnerModel)
                }
                binding.textViewCnpj.text = partnerModel.cnpjCorporation.formatCnpj()
                binding.textViewNamePartner.text = partnerModel.nameFantasy.ifNotEmpty()
                binding.textViewNameCompletePartner.text = partnerModel.nameCorporation.ifNotEmpty()

                when(partnerModel.isMyPartner){

                    StatusIsMyPartner.TRUE -> {
                        binding.constraintLayoutAcceptPartner.visibility = View.GONE
                        Glide.with(binding.imageViewAddPartner)
                            .load(R.drawable.icon_trash)
                            .into(binding.imageViewAddPartner)
                    }
                    StatusIsMyPartner.FALSE -> {
                        binding.constraintLayoutAcceptPartner.visibility = View.GONE
                        Glide.with(binding.imageViewAddPartner)
                            .load(R.drawable.ic_person_add)
                            .into(binding.imageViewAddPartner)
                    }
                    StatusIsMyPartner.WAIT_ANSWER -> {
                        binding.constraintLayoutAcceptPartner.visibility = View.GONE
                        Glide.with(binding.imageViewAddPartner)
                            .load(R.drawable.ic_send)
                            .into(binding.imageViewAddPartner)
                    }
                    StatusIsMyPartner.TO_RESPOND -> {
                        binding.imageViewAddPartner.visibility = View.GONE
                        binding.constraintLayoutAcceptPartner.visibility = View.VISIBLE
                        Glide.with(binding.imageViewAddPartner)
                            .load(R.drawable.ic_send)
                            .into(binding.imageViewAddPartner)
                    }
                }
            }

    }
}