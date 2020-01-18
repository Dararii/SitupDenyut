package com.sift.situpdenyut.engine

import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Database instance, singleton using Dagger 2
 */
@Singleton
class DbEngine @Inject constructor() {

    private var db: FirebaseDatabase = FirebaseDatabase.getInstance()

    init {
        //Set data persistence enable.
        db.setPersistenceEnabled(true)
    }

    /**
     * Getter function for firebase instance
     */
    fun getDb(): FirebaseDatabase {
        db.goOnline()
        return db
    }

}