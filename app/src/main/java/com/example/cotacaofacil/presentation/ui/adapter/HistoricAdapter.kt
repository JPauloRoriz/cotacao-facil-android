package com.example.cotacaofacil.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.cotacaofacil.R
import com.example.cotacaofacil.databinding.ItemHistoryBinding
import com.example.cotacaofacil.domain.model.HistoryModel
import com.example.cotacaofacil.domain.model.TypeHistory.*
import com.example.cotacaofacil.presentation.ui.extension.formatDateHistoric

class HistoricAdapter : RecyclerView.Adapter<HistoricAdapter.HistoricViewHolder>() {
    var listHistoric: MutableList<HistoryModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricViewHolder {
        val binding = ItemHistoryBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoricViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listHistoric.size
    }

    override fun onBindViewHolder(holder: HistoricViewHolder, position: Int) {
        holder.bind(listHistoric[position], position)
    }

    fun updateList(list : MutableList<HistoryModel>){
        listHistoric = list
        notifyDataSetChanged()
    }

    inner class HistoricViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(historyModel: HistoryModel, position: Int) {
            binding.textViewDate.text = historyModel.date.formatDateHistoric()

            historyModel.isSelected = binding.checkBoxSelected.isSelected
            when (historyModel.typeHistory) {
                CANCEL_PRICE -> {
                    binding.imageViewIconHistory.setBackgroundResource(R.drawable.ic_history)
                }
                FINISH_PRICE -> {
                    binding.imageViewIconHistory.setBackgroundResource(R.drawable.ic_history)
                }
                CREATE_PRICE -> {
                    binding.imageViewIconHistory.setBackgroundResource(R.drawable.ic_history)
                }
                NEW_PARTNER_ADD -> {
                    binding.imageViewIconHistory.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_add_accepted));
                    binding.textViewTitleHistory.text = binding.root.context.getString(R.string.text_title_add_partner)
                    binding.textViewDescription.text = binding.root.context.getString(R.string.text_add_partner, historyModel.nameAssistant)
                }
                REQUEST_PARTNER_REJECT -> {
                    binding.imageViewIconHistory.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_remove_partner));
                    binding.textViewTitleHistory.text = binding.root.context.getString(R.string.text_title_request_rejected_partner)
                    binding.textViewDescription.text = binding.root.context.getString(R.string.text_request_rejected_partner, historyModel.nameAssistant)
                }
                MY_REQUEST_PARTNER_REJECT -> {
                    binding.imageViewIconHistory.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_remove_partner));
                    binding.textViewTitleHistory.text = binding.root.context.getString(R.string.text_title_request_rejected_partner)
                    binding.textViewDescription.text = binding.root.context.getString(R.string.text_request_rejected_partner, historyModel.nameAssistant)
                }
                SEND_REQUEST_PARTNER -> {
                    binding.imageViewIconHistory.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_send));
                    binding.textViewTitleHistory.text = binding.root.context.getString(R.string.text_title_request_partner)
                    binding.textViewDescription.text = binding.root.context.getString(R.string.text_send_request_partner, historyModel.nameAssistant)
                }
                SEND_RECEIVE_PARTNER -> {
                    binding.imageViewIconHistory.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_send));
                    binding.textViewTitleHistory.text = binding.root.context.getString(R.string.text_title_request_partner)
                    binding.textViewDescription.text = binding.root.context.getString(R.string.text_receive_request_partner, historyModel.nameAssistant)
                }
                REQUEST_PARTNER_CANCEL -> {
                    binding.imageViewIconHistory.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_cancel_send_partner));
                    binding.textViewTitleHistory.text = binding.root.context.getString(R.string.text_title_request_cancel_partner)
                    binding.textViewDescription.text = binding.root.context.getString(R.string.text_request_cancel_partner, historyModel.nameAssistant)
                }
                PARTNER_DELETED -> {
                    binding.imageViewIconHistory.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_cancel_add_partner));
                    binding.textViewTitleHistory.text = binding.root.context.getString(R.string.text_title_delete_partner)
                    binding.textViewDescription.text = binding.root.context.getString(R.string.text_delete_partner_adapter, historyModel.nameAssistant)
                }
            }
        }
    }

}