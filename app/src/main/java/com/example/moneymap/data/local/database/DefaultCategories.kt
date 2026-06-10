package com.example.moneymap.data.local.database

import com.example.moneymap.data.local.entity.CategoryEntity

object DefaultCategories {
    fun get() = listOf(
        CategoryEntity(name = "حقوق", icon = "ic_salary", type = "INCOME", color = "#1D9E75"),
        CategoryEntity(name = "سرمایه‌گذاری", icon = "ic_invest", type = "INCOME", color = "#378ADD"),
        CategoryEntity(name = "هدیه", icon = "ic_gift", type = "INCOME", color = "#7F77DD"),
        CategoryEntity(name = "سایر درآمد", icon = "ic_other", type = "INCOME", color = "#5DCAA5"),

        CategoryEntity(name = "خوراک", icon = "ic_food", type = "EXPENSE", color = "#D85A30"),
        CategoryEntity(name = "حمل و نقل", icon = "ic_transport", type = "EXPENSE", color = "#BA7517"),
        CategoryEntity(name = "قبوض", icon = "ic_bill", type = "EXPENSE", color = "#E24B4A"),
        CategoryEntity(name = "سلامت", icon = "ic_health", type = "EXPENSE", color = "#D4537E"),
        CategoryEntity(name = "تفریح", icon = "ic_fun", type = "EXPENSE", color = "#534AB7"),
        CategoryEntity(name = "خرید", icon = "ic_shop", type = "EXPENSE", color = "#993C1D"),
        CategoryEntity(name = "سایر هزینه", icon = "ic_other", type = "EXPENSE", color = "#888780")
    )
}