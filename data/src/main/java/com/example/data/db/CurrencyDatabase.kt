package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.exchangeapp.db.CurrencyDao
import com.example.exchangeapp.db.CurrencyDto

@Database(entities = [CurrencyDto::class], version = 1)
abstract class CurrencyDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao
}