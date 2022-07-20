package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CurrencyDto::class], version = 1)
abstract class CurrencyDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao
}