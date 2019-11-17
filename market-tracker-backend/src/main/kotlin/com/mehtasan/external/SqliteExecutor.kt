package com.mehtasan.external

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import javax.annotation.PostConstruct
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class SqliteExecutor {

    private lateinit var connection: Connection

    @PostConstruct
    private fun init() {
        Class.forName("org.sqlite.JDBC")
        connection = DriverManager.getConnection("jdbc:sqlite:/mnt/f/Workspace/market-tracker/markettracker.db")
    }

    fun readData(query: String, params: List<String>): ResultSet {
        val statement = connection.prepareStatement(query)
        params.forEachIndexed { index, element ->
            statement.setString(index + 1, element)
        }
        return statement.executeQuery()
    }

    fun writeData(query: String, params: List<String>): Boolean {
        val statement = connection.prepareStatement(query)
        params.forEachIndexed { index, element ->
            statement.setString(index + 1, element)
        }
        return statement.execute()
    }
}