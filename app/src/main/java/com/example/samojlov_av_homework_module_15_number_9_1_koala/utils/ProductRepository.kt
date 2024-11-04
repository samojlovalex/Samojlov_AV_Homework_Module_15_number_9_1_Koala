package com.example.samojlov_av_homework_module_15_number_9_1_koala.utils

import androidx.lifecycle.LiveData
import com.example.samojlov_av_homework_module_15_number_9_1_koala.models.Product

class ProductRepository(
    private val productDao: ProductDao,
) {
    val products: LiveData<List<Product>> = productDao.getAllProducts()

    suspend fun insert(product: Product) {
        productDao.insert(product)
    }

    suspend fun delete(product: Product) {
        productDao.delete(product)
    }

    suspend fun update(product: Product) {
        productDao.update(product)
    }

    fun deleteAll() {
        productDao.deleteAll()
    }

}