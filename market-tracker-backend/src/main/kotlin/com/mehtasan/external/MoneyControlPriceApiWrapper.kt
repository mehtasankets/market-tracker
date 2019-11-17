package com.mehtasan.external

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.httpGet
import com.mehtasan.domain.MoneyControlStockDetails
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class MoneyControlPriceApiWrapper {

    @Inject
    private lateinit var companyDao: CompanyDao

    private val moneyControlIdCache = mutableMapOf<String, String>()

    private val objectMapper = ObjectMapper()

    fun fetchPriceDetails(stockIdentities: List<String>): Map<String, MoneyControlStockDetails> {
        val moneyControlIds = getMoneyControlIds(stockIdentities)
        return stockIdentities.filter { moneyControlIds.containsKey(it) }.map {
            val details = fetchDetails(moneyControlIds.getValue(it))
            details?.identity = it
            it to details
        }.filter { it.second != null }.toMap() as Map<String, MoneyControlStockDetails>
    }

    private fun fetchDetails(moneyControlId: String): MoneyControlStockDetails? {
        try {
            val (_, _, result) =
                    "https://priceapi-aws.moneycontrol.com/pricefeed/nse/equitycash/$moneyControlId"
                            .httpGet()
                            .responseString()

            if (result.component2() != null || result.component1() == null) {
                println("Error while fetching data from MoneyControl: $result")
                return null
            }
            val priceDetails = objectMapper.readValue(result.component1(), Map::class.java)
            return enrichDomainObject(MoneyControlStockDetails(moneyControlId), priceDetails)
        } catch (e: Exception) {
            println(e.message)
            return null
        }
    }

    private fun enrichDomainObject(stockDetails: MoneyControlStockDetails, priceDetails: Map<*, *>?): MoneyControlStockDetails? {
        if (priceDetails == null || priceDetails.isEmpty()) {
            return null
        }
        val data = priceDetails["data"] as Map<*, *>
        stockDetails.displayName = data["SC_FULLNM"] as String
        stockDetails.currentPrice = (data["pricecurrent"] as String).toDouble()
        stockDetails.peRatio = data["PE"] as Double
        stockDetails.bookValue = (data["BV"] as String).toDouble()
        stockDetails.marketCapital = data["MKTCAP"] as Double
        stockDetails.yearHigh = (data["52H"] as String).toDouble()
        stockDetails.yearLow = (data["52L"] as String).toDouble()
        return stockDetails
    }

    private fun getMoneyControlIds(stockIdentities: List<String>): Map<String, String> {
        stockIdentities.forEach {
            if (!moneyControlIdCache.containsKey(it)) {
                val companyInfo = companyDao.fetch(it)
                if (companyInfo?.moneyControlId != null)
                    moneyControlIdCache[it] = companyInfo.moneyControlId!!
            }
        }
        return moneyControlIdCache.filter { stockIdentities.contains(it.key) }
    }
}
