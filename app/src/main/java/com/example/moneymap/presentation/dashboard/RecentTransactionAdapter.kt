package com.example.moneymap.presentation.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymap.R
import com.example.moneymap.databinding.ItemTransactionBinding
import com.example.moneymap.domain.model.Transaction
import com.example.moneymap.domain.model.TransactionType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecentTransactionAdapter :
    ListAdapter<Transaction, RecentTransactionAdapter.ViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): RecentTransactionAdapter.ViewHolder {
        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(p0.context), p0, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        vh.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemTransactionBinding

    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {
            binding.txtCategory.text = transaction.categoryName
            binding.txtNote.text = transaction.note.ifBlank { transaction.categoryName }
            binding.txtDate.text = formatDate(transaction.date)


            if (transaction.type == TransactionType.Income) {
                binding.txtAmount.text = "- %,.0f".format(transaction.amount)
                binding.txtAmount.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.green_500)
                )
            } else {
                binding.txtAmount.text = "- %,.0f".format(transaction.amount)
                binding.txtAmount.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.red_500)
                )
            }

        }
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}


class DiffCallback : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction) =
        oldItem == newItem
}