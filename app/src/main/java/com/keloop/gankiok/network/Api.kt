package com.keloop.gankiok.network

import com.keloop.gankiok.model.BlogResult
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("{name}/{number}/{page}")
    fun getBlog(@Path("name") name: String, @Path("number") number: Int, @Path("page") page: Int) : Single<BlogResult>

}