package com.mehtasan.resource

import com.mehtasan.domain.CompanyInfo
import com.mehtasan.external.CompanyDao
import com.mehtasan.external.SqliteExecutor
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/company")
class CompanyResource {

    @Inject
    private lateinit var companyDao: CompanyDao

    @GET
    @Path("/upsert/{identity}/{moneyControlId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    fun upsert(@PathParam("identity") identity: String, @PathParam("moneyControlId") moneyControlId: String) {
        companyDao.upsert(identity, moneyControlId)
    }

    @GET
    @Path("/fetch/{identity}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    fun fetch(@PathParam("identity") identity: String): CompanyInfo? {
        return companyDao.fetch(identity)
    }

    @GET
    @Path("/fetchAll")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    fun fetchAll(): List<CompanyInfo> {
        return companyDao.fetchAll()
    }

    @GET
    @Path("/delete/{identity}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    fun delete(@PathParam("identity") identity: String) {
        companyDao.delete(identity)
    }
}