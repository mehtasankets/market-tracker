package com.mehtasan.resource

import com.mehtasan.domain.MoneyControlStockDetails
import com.mehtasan.domain.StockDetails
import com.mehtasan.external.CompanyDao
import com.mehtasan.external.MoneyControlPriceApiWrapper
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/stock")
class StockResource {

    @Inject
    private lateinit var companyDao: CompanyDao

    @Inject
    private lateinit var moneyControlPriceApiWrapper: MoneyControlPriceApiWrapper

    @GET
    @Path("/stockDetailsAll")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    fun stockDetailsAll(): List<StockDetails> {
        val identities = companyDao.fetchAll().mapNotNull { it.identity }
        val moneyControlStockDetails = moneyControlPriceApiWrapper.fetchPriceDetails(identities)
        return identities.map {
            val stockDetails = StockDetails(it)
            enrichMoneyControlStockDetails(stockDetails, moneyControlStockDetails[it])
        }
    }

    @GET
    @Path("/stockDetails")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    fun stockDetails(@QueryParam("identities") identities: List<String>): List<StockDetails> {
        val moneyControlStockDetails = moneyControlPriceApiWrapper.fetchPriceDetails(identities)
        return identities.map {
            val stockDetails = StockDetails(it)
            enrichMoneyControlStockDetails(stockDetails, moneyControlStockDetails[it])
        }
    }

    private fun enrichMoneyControlStockDetails(stockDetails: StockDetails, moneyControlStockDetails: MoneyControlStockDetails?): StockDetails {
        if (moneyControlStockDetails == null) {
            return stockDetails
        }
        stockDetails.displayName = moneyControlStockDetails.displayName
        stockDetails.currentPrice = moneyControlStockDetails.currentPrice
        stockDetails.peRatio = moneyControlStockDetails.peRatio
        stockDetails.bookValue = moneyControlStockDetails.bookValue
        stockDetails.marketCapital = moneyControlStockDetails.marketCapital
        stockDetails.yearHigh = moneyControlStockDetails.yearHigh
        stockDetails.yearLow = moneyControlStockDetails.yearLow
        return stockDetails
    }
}