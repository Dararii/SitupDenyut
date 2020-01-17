package com.sift.situpdenyut

import android.app.Application
import com.sift.situpdenyut.engine.dagger.AppComponent
import com.sift.situpdenyut.engine.dagger.DaggerAppComponent

class DenyutApp: Application() {

    lateinit var appComponent: AppComponent

    /**
     * App Entry point
     */
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }
}