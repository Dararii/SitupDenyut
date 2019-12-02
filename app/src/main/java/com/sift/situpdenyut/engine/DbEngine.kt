package com.sift.situpdenyut.engine

import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DbEngine @Inject constructor() {

    private var db: FirebaseDatabase = FirebaseDatabase.getInstance()

    init {
        db.setPersistenceEnabled(true)
    }

    fun getDb(): FirebaseDatabase {
        db.goOnline()
        return db
    }

}