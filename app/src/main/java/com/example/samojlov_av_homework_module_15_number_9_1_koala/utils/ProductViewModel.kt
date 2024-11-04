package com.example.samojlov_av_homework_module_15_number_9_1_koala.utils

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.samojlov_av_homework_module_15_number_9_1_koala.models.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProductRepository
    val products: LiveData<List<Product>>

    init {
        val dao = ProductDatabase.getDatabase(application).getProductDao()
        repository = ProductRepository(dao)
        products = repository.products
    }

    fun deleteProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(product)
    }

    fun insertProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(product)
    }

    fun updateProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(product)
    }

    fun deleteAllProduct() =  viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

}