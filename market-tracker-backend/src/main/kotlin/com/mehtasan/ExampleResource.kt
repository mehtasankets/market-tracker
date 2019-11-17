package com.mehtasan

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@Path("/hello")
class ExampleResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun hello(@QueryParam("name") name: String?): Pair<String, String> = Pair("hello", "$name")
}