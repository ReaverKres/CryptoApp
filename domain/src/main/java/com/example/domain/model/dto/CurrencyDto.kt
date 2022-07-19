package com.example.domain.model.dto

data class Currencies(val baseName: String, val currencies: List<Currency>)
data class Currency(val name: String, val value: Double)