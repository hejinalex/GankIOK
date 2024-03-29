package com.keloop.gankiok.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitWrap private constructor() {

    private var retrofit: Retrofit? = null

    companion object {
        private val instance: RetrofitWrap by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RetrofitWrap()
        }

        /**
         * 是否开启打印日志，默认关闭
         */
        fun init(baseUrl: String, debugEnable: Boolean = false) {
            instance.retrofit = Retrofit.Builder().apply {
                baseUrl(baseUrl)
                client(okhttpClient(debugEnable))
                addConverterFactory(GsonConverterFactory.create())
                addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            }.build()
        }

        /**
         * 获取API服务
         */
        fun <T> service(service: Class<T>): T {
            if (instance.retrofit != null) {
                return instance.retrofit!!.create(service)
            } else {
                throw IllegalStateException("请先调用RetrofitWrap.init()方法进行初始化")
            }
        }


        //OkHttpClient客户端
        private fun okhttpClient(debugEnable: Boolean = false): OkHttpClient {
            return OkHttpClient.Builder().apply {
                connectTimeout(15, TimeUnit.SECONDS)
                readTimeout(15, TimeUnit.SECONDS)
                if (debugEnable) addInterceptor(interceptorLog())
            }.build()
        }


        //日志打印拦截器
        private fun interceptorLog(): HttpLoggingInterceptor {
            val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.d("RetrofitWrap", message)
                }
            })
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return interceptor
        }
    }
}