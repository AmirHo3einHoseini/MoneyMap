package com.example.moneymap.presentation.transaction

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionAdapter(private val onDeleteClick: (Transaction) -> Unit) :
    ListAdapter<Transaction, TransactionAdapter.ViewHolder>(
        DiffCallback()
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        p1: Int
    ): ViewHolder {
        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }


    inner class ViewHolder(
        private val binding: ItemTransactionBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction) {
            binding.txtCategory.text = transaction.categoryName
            binding.txtNote.text = transaction.note.ifBlank { transaction.categoryName }
            binding.txtDate.text = SimpleDateFormat(
                "dd/MM/yyyy", Locale.getDefault()
            ).format(Date(transaction.date))

            if (transaction.type == TransactionType.Income){
                binding.txtAmount.text ="+ %,.0f".format(transaction.amount)
                binding.txtAmount.setTextColor(ContextCompat.getColor(binding.root.context,R.color.green_500))
            }else{
                binding.txtAmount.text = "- %,.0f".format(transaction.amount)
                binding.txtAmount.setTextColor(
                    ContextCompat.getColor(binding.root.context,R.color.red_500)
                )
            }

            binding.root.setOnLongClickListener {
                showDeleteDialog(transaction)
                true
            }

        }



        private fun showDeleteDialog(transaction: Transaction){
            MaterialAlertDialogBuilder(binding.root.context)
                .setTitle("حذف تراکنش")
                .setMessage("آیا از حذف این تراکنش مطمئن هستید؟")
                .setPositiveButton("حذف"){_,_ ->onDeleteClick(transaction)}
                .setNegativeButton("",null)
                .show()
        }


    }

    class DiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction) =
            oldItem == newItem
    }

}