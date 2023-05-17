package com.example.cotacaofacil.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cotacaofacil.R
import com.example.cotacaofacil.databinding.ItemHistoryBinding
import com.example.cotacaofacil.domain.model.HistoryModel
import com.example.cotacaofacil.domain.model.TypeHistory.*
import com.example.cotacaofacil.presentation.ui.extension.formatDateHistoric

class HistoricAdapter : RecyclerView.Adapter<HistoricAdapter.HistoricViewHolder>() {
    var listHistoric: MutableList<HistoryModel> = mutableListOf()
    var clickIconHistoric: ((HistoryModel) -> Unit)? = null

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
        val item = listHistoric[position]
        val context = holder.binding.root.context
        val backgroundColor = when (item.typeHistory) {
            NEW_PARTNER_ADD -> ContextCompat.getColor(context, R.color.green)
            PARTNER_DELETED -> ContextCompat.getColor(context, R.color.red_color)
            REQUEST_PARTNER_CANCEL -> ContextCompat.getColor(context, R.color.red_color)
            REQUEST_PARTNER_REJECT -> ContextCompat.getColor(context, R.color.red_color)
            MY_REQUEST_PARTNER_REJECT -> ContextCompat.getColor(context, R.color.red_color)
            SEND_REQUEST_PARTNER -> ContextCompat.getColor(context, R.color.gray)
            SEND_RECEIVE_PARTNER -> ContextCompat.getColor(context, R.color.gray)
            else -> ContextCompat.getColor(context, R.color.colorPrimary)
        }

        holder.binding.imageViewIconHistory.setColorFilter(backgroundColor)
        holder.binding.viewArrowRight.setColorFilter(backgroundColor)

        if (position == 0) {
            holder.binding.viewLineTopHistory.visibility = View.INVISIBLE
        } else {
            holder.binding.viewLineTopHistory.visibility = View.VISIBLE
        }
        if (position == listHistoric.size - 1) {
            holder. binding.viewLineBottomHistory.visibility = View.INVISIBLE
        } else {
            holder.binding.viewLineBottomHistory.visibility = View.VISIBLE
        }
    }

    fun updateList(list: MutableList<HistoryModel>) {
        listHistoric = list
        notifyDataSetChanged()
    }

    inner class HistoricViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(historyModel: HistoryModel, position: Int) {
            binding.textViewDate.text = historyModel.date.formatDateHistoric()
            
            binding.imageViewIconHistory.setOnClickListener {
                clickIconHistoric?.invoke(historyModel)
            }

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
//                    binding.imageViewIconHistory.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.green))
//                    binding.viewArrowRight.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.green))
                    binding.textViewTitleHistory.text = binding.root.context.getString(R.string.text_title_add_partner)
                    binding.textViewDescription.text = binding.root.context.getString(R.string.text_add_partner, historyModel.nameAssistant)
                }
                REQUEST_PARTNER_REJECT -> {
                    binding.imageViewIconHistory.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_remove_partner))
//                    binding.imageViewIconHistory.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.red_color))
//                    binding.viewArrowRight.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.red_color))
                    binding.textViewTitleHistory.text = binding.root.context.getString(R.string.text_title_request_rejected_partner)
                    binding.textViewDescription.text =
                        binding.root.context.getString(R.string.text_request_rejected_partner, historyModel.nameAssistant)
                }
                MY_REQUEST_PARTNER_REJECT -> {
                    binding.imageViewIconHistory.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_remove_partner));
//                    binding.imageViewIconHistory.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.red_color))
//                    binding.viewArrowRight.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.red_color))
                    binding.textViewTitleHistory.text = binding.root.context.getString(R.string.text_title_request_rejected_partner)
                    binding.textViewDescription.text =
                        binding.root.context.getString(R.string.text_request_rejected_partner, historyModel.nameAssistant)
                }
                SEND_REQUEST_PARTNER -> {
                    binding.imageViewIconHistory.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_send));
                    binding.textViewTitleHistory.text = binding.root.context.getString(R.string.text_title_request_partner)
//                    binding.imageViewIconHistory.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.gray))
//                    binding.viewArrowRight.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.gray))

                    binding.textViewDescription.text = binding.root.context.getString(R.string.text_send_request_partner, historyModel.nameAssistant)
                }
                SEND_RECEIVE_PARTNER -> {
                    binding.imageViewIconHistory.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_send));
//                    binding.imageViewIconHistory.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.gray))
//                    binding.viewArrowRight.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.gray))
                    binding.textViewTitleHistory.text = binding.root.context.getString(R.string.text_title_request_partner)
                    binding.textViewDescription.text =
                        binding.root.context.getString(R.string.text_receive_request_partner, historyModel.nameAssistant)
                }
                REQUEST_PARTNER_CANCEL -> {
                    binding.imageViewIconHistory.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_cancel_send_partner))
//                    binding.imageViewIconHistory.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.red_color))
//                    binding.viewArrowRight.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.red_color))
                    binding.textViewTitleHistory.text = binding.root.context.getString(R.string.text_title_request_cancel_partner)
                    binding.textViewDescription.text =
                        binding.root.context.getString(R.string.text_request_cancel_partner, historyModel.nameAssistant)
                }
                PARTNER_DELETED -> {
                    binding.imageViewIconHistory.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_cancel_add_partner));
//                    binding.imageViewIconHistory.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.red_color))
//                    binding.viewArrowRight.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.red_color))
                    binding.textViewTitleHistory.text = binding.root.context.getString(R.string.text_title_delete_partner)
                    binding.textViewDescription.text =
                        binding.root.context.getString(R.string.text_delete_partner_adapter, historyModel.nameAssistant)
                }
            }
        }
    }

}