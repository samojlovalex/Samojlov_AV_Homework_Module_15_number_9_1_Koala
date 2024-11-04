package com.example.samojlov_av_homework_module_15_number_9_1_koala.utils

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.samojlov_av_homework_module_15_number_9_1_koala.models.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun getProductDao(): ProductDao

    companion object {
        private var INSTANSE: ProductDatabase? = null
        fun getDatabase(context: Context): ProductDatabase {
            return INSTANSE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        ProductDatabase::class.java,
                        "product_database"
                    ).build()
                INSTANSE = instance
                instance
            }
        }
    }

}