package com.mehtasan

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test

@QuarkusTest
open class CompanyResourceTest {

    @Test
    fun `test happy path`() {
        given()
          .`when`().get("/company/fetchAll")
          .then()
             .statusCode(200)
    }

}