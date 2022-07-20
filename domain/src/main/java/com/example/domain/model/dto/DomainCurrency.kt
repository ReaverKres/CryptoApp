package com.example.domain.model.dto

data class Currencies(val baseName: String, val currencies: List<Currency>)
data class Currency(val name: String, val value: Double)
data class FavoriteCurrencyDomain(
    val name: String,
    val value: Double,
    val baseName: String,
    val baseValue: Double = 1.0
)