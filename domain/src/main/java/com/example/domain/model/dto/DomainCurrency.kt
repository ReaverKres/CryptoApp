package com.example.domain.model.dto

data class CurrenciesDto(
    val baseName: String,
    val currencies: List<Currency>,
    val allAccessibleCurrencies: Set<String> = setOf()
)

data class Currency(val name: String, val value: Double, var isSaved: Boolean = false)
data class FavoriteCurrencyDomain(
    val name: String,
    val value: Double,
    val baseName: String,
    val baseValue: Double = 1.0,
)