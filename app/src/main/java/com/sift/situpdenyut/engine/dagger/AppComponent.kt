package com.sift.situpdenyut.engine.dagger

import com.sift.situpdenyut.engine.DbEngine
import dagger.Component
import javax.inject.Singleton

/**
 * App Scope component
 */
@Singleton
@Component
interface AppComponent {

    fun getDbEngine(): DbEngine

}