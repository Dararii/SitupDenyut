package com.sift.situpdenyut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.sift.situpdenyut.engine.DbEngine
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var dbEngine: DbEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initiate Database object
        dbEngine = (application as DenyutApp).appComponent.getDbEngine()
        Log.d("MAIN", "engine object $dbEngine")

        dbEngine.getDb().getReference("test1").addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.d("MAIN", "Data test1: ${p0.value}")
            }

        })
    }
}
