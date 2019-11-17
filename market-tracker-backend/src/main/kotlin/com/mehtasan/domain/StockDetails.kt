package com.mehtasan.domain

data class StockDetails(
    var identity: String,
    var displayName: String? = null,
    var currentPrice: Double? = null,
    var peRatio: Double? = null,
    var bookValue: Double? = null,
    var marketCapital: Double? = null,
    var yearHigh: Double? = null,
    var yearLow: Double? = null
)