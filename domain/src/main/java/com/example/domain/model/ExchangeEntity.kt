package com.example.domain.model

import com.example.domain.model.dto.Currency

data class ExchangeEntity(
    val baseCode: String,
    val conversionRates: Map<String, Double>,
    val documentation: String,
    val result: String,
    val termsOfUse: String,
    val timeLastUpdateUnix: Int,
) {
    fun getRatesForCurrency(baseCurrency: String): List<Currency> {
        val currencies = mutableListOf<Currency>()
        conversionRates.entries.filter { it.key != baseCurrency }
            .forEach { currencies.add(Currency(it.key, it.value)) }
        return currencies.toList()
    }
}