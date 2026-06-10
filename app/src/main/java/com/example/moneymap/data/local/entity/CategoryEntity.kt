package com.example.moneymap.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moneymap.domain.model.Category
import com.example.moneymap.domain.model.TransactionType

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long=0,
    val name: String,
    val icon:String,
    val type:String,
    val color: String
)
fun CategoryEntity.toDomain() = Category(
    id = id,
    name = name,
    icon = icon,
    type = TransactionType.valueOf(type),
    color = color
)

fun Category.toEntity() = CategoryEntity(
    id = id,
    name = name,
    icon = icon,
    type = type.name,
    color = color
)
