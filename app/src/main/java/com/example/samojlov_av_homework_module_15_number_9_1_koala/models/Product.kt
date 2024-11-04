package com.example.samojlov_av_homework_module_15_number_9_1_koala.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
data class Product(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "price") var price: Double,
    @ColumnInfo(name = "image") var image: Int,
    @ColumnInfo(name = "currency") var currency: String,
    @ColumnInfo(name = "count") var count: Int,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
)