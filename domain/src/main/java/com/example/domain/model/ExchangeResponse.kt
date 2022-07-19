package com.example.domain.model

import com.google.gson.annotations.SerializedName

data class ExchangeResponse(
    @SerializedName("base_code")val baseCode: String,
    @SerializedName("conversion_rates") val conversionRates: Map<String, Double>,
    @SerializedName("documentation") val documentation: String,
    @SerializedName("result") val result: String,
    @SerializedName("terms_of_use") val termsOfUse: String,
    @SerializedName("time_last_update_unix") val timeLastUpdateUnix: Int,
    @SerializedName("time_last_update_utc") val timeLastUpdateUtc: String,
    @SerializedName("time_next_update_unix") val timeNextUpdateUnix: Int,
    @SerializedName("time_next_update_utc") val timeNextUpdateUtc: String
)