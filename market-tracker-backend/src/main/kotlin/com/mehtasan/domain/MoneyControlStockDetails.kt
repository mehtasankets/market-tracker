package com.mehtasan.domain

data class MoneyControlStockDetails(
        var moneyControlIdentity: String? = null,
        var identity: String? = null,
        var displayName: String? = null,
        var currentPrice: Double? = null,
        var peRatio: Double? = null,
        var bookValue: Double? = null,
        var marketCapital: Double? = null,
        var yearHigh: Double? = null,
        var yearLow: Double? = null
)
