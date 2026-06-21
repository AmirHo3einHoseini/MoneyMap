package com.example.moneymap.presentation.budget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymap.R
import com.example.moneymap.databinding.ItemBudgetBinding
import com.example.moneymap.domain.model.Budget
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BudgetAdapter(
    private val onDeleteClick: (Budget) -> Unit
) : ListAdapter<Budget, BudgetAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): ViewHolder {
        val binding = ItemBudgetBinding.inflate(
            LayoutInflater.from(p0.context), p0, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemBudgetBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(budget: Budget) {
            binding.tvCategoryName.text = budget.categoryName
            binding.tvPercent.text = "${budget.progressPercent}٪"
            binding.progressBudget.progress = budget.progressPercent
            binding.tvSpent.text = "مصرف: %,.0f".format(budget.spentAmount)
            binding.tvLimit.text = "سقف: %,.0f".format(budget.limitAmount)

            // اگر از بودجه گذشت رنگ قرمز نشان بده
            val color = if (budget.isOverBudget) R.color.red_500 else R.color.green_500
            binding.tvPercent.setTextColor(
                ContextCompat.getColor(binding.root.context, color)
            )

            binding.root.setOnLongClickListener {
                MaterialAlertDialogBuilder(binding.root.context)
                    .setTitle("حذف بودجه")
                    .setMessage("آیا از حذف این بودجه مطمئن هستید؟")
                    .setPositiveButton("حذف") { _, _ -> onDeleteClick(budget) }
                    .setNegativeButton("انصراف", null)
                    .show()
                true
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Budget>() {
        override fun areItemsTheSame(oldItem: Budget, newItem: Budget) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Budget, newItem: Budget) =
            oldItem == newItem
    }
}