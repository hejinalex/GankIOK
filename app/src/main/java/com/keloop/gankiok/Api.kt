package com.keloop.gankiok

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("{name}/{number}/{page}")
    fun getBlog(@Path("name") name: String, @Path("number") number: Int, @Path("page") page: Int) : Single<BlogResult>

}