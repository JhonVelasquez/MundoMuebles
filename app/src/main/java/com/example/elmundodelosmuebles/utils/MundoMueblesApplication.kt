package com.example.elmundodelosmuebles.utils

import android.app.Application

class MundoMueblesApplication: Application() {

    companion object{
        lateinit var prefs:Prefs
    }

    override fun onCreate() {
        super.onCreate()
        prefs=Prefs(applicationContext)
    }
}