package com.example.samojlov_av_homework_module_15_number_9_1_koala.utils

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.samojlov_av_homework_module_15_number_9_1_koala.models.Product


@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: Product)

    @Delete
    suspend fun delete (product: Product)

    @Update
    suspend fun update (product: Product)

    @Query("SELECT * FROM product_table ORDER BY id ASC")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("DELETE FROM product_table")
    fun deleteAll()
}