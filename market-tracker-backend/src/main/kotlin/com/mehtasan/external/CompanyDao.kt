package com.mehtasan.external

import com.mehtasan.domain.CompanyInfo
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.PathParam

@ApplicationScoped
class CompanyDao {

    @Inject
    private lateinit var sqliteExecutor: SqliteExecutor

    fun upsert(identity: String, moneyControlId: String) {
        val query = """
            INSERT INTO company_info(identity, money_control_id)
            VALUES (?, ?)
            ON CONFLICT(identity) DO UPDATE SET
            money_control_id = ?;
        """.trimIndent()
        println(query)
        sqliteExecutor.writeData(query, listOf(identity, moneyControlId, moneyControlId))
    }

    fun fetch(@PathParam("identity") identity: String): CompanyInfo? {
        val query = """
            SELECT identity, money_control_id
            FROM company_info
            WHERE identity = ?;
        """.trimIndent()
        val resultSet = sqliteExecutor.readData(query, listOf(identity))
        return if (resultSet.next()) CompanyInfo(resultSet.getString(1), resultSet.getString(2)) else null
    }

    fun fetchAll(): List<CompanyInfo> {
        val query = """
            SELECT identity, money_control_id
            FROM company_info;
        """.trimIndent()
        val resultSet = sqliteExecutor.readData(query, listOf())
        val results = mutableListOf<CompanyInfo>()
        while (resultSet.next()) {
            results.add(CompanyInfo(resultSet.getString(1), resultSet.getString(2)))
        }
        return results
    }

    fun delete(identity: String) {
        val query = """
            DELETE FROM company_info
            WHERE identity = ?;
        """.trimIndent()
        sqliteExecutor.writeData(query, listOf(identity))
    }
}