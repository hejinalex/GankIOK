package com.keloop.gankiok

import android.app.Application

class App : Application(){

    override fun onCreate() {
        super.onCreate()
        RetrofitWrap.init("http://gank.io/api/data/", true)
    }

}