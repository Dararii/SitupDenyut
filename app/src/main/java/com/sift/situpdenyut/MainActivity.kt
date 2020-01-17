package com.sift.situpdenyut

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.sift.situpdenyut.engine.DbEngine
import com.sift.situpdenyut.engine.Denyut
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MAIN_ACTIVITY"
    }
    private lateinit var dbEngine: DbEngine
    private lateinit var denyutListener: ValueEventListener
    private lateinit var denyutRef: DatabaseReference
    private lateinit var situpListener: ValueEventListener
    private lateinit var situpRef: DatabaseReference
    private var currentSessionDenyutData: ArrayList<Denyut> = ArrayList()
    private var currentSessionSitupData: Long = 0

    /**
     * Activity entry point
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initiate Database object
        dbEngine = (application as DenyutApp).appComponent.getDbEngine()
        Log.d("MAIN", "engine object $dbEngine")

        initRealtimeListener()

        btnCountSitup.setOnClickListener {
            dbEngine.getDb().reference.child("data_denyut").child("${System.currentTimeMillis()}")
                .setValue((0..50).random())
        }

        switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dbEngine.getDb().reference.child("switch").setValue(1)
            } else {
                dbEngine.getDb().reference.child("switch").setValue(0)
                dbEngine.getDb().reference.child("data_denyut").removeValue()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        denyutRef.limitToLast(15).addValueEventListener(denyutListener)
        situpRef.addValueEventListener(situpListener)
    }

    override fun onPause() {
        denyutRef.limitToLast(15).removeEventListener(denyutListener)
        situpRef.removeEventListener(situpListener)
        super.onPause()
    }

    /**
     * Real-time event listener from database
     */
    private fun initRealtimeListener() {
        denyutRef = dbEngine.getDb().reference.child("data_denyut")
        denyutListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(data: DataSnapshot) {
                currentSessionDenyutData.clear()
                Log.d(TAG, "Data test1: ${data.childrenCount}")
                for (i in data.children) {
                    try {
                        val v = i.value
                        val t = "${i.key}"

                        val denyut = Denyut(
                            value = v.toString().toInt(),
                            timestamp = t
                        )
                        currentSessionDenyutData.add(denyut)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error while parsing data denyut ${e.message}")
                    }
                }
                drawData(currentSessionDenyutData, chartMainDenyut)
            }
        }

        situpRef = dbEngine.getDb().reference.child("situp")
        situpListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(data: DataSnapshot) {
                try {
                    currentSessionSitupData = data.value.toString().toLong()
                    Log.d(TAG, "Data situp: ${data.value}")
                    textMainSitupCount.text = "$currentSessionSitupData kali"
                } catch (e: Exception) {
                    Log.e(TAG, "Error while parsing data situp ${e.message}")
                }
            }
        }
    }

    /**
     * Draw data to chart
     */
    fun drawData(data: ArrayList<Denyut>, chart: LineChart) {
        val entries: MutableList<Entry> = ArrayList()
        for (i in 0 until data.size) {
            val v = try { // get the object by the key.
                data[i].value.toFloat()
            } catch (e: java.lang.Exception) {
                0f
            }
            entries.add(Entry(i.toFloat(), v))
        }

        val dataSet = LineDataSet(entries, "Denyut") // add entries to dataset
        dataSet.setColors(Color.BLUE)
        dataSet.setDrawValues(false)

        val dataSets: MutableList<ILineDataSet> = ArrayList()
        dataSets.add(dataSet)

        val lineData = LineData(dataSets)
        chart.data = lineData
        val yAxisLeft = chart.axisLeft
        val yAxisRight = chart.axisRight
        yAxisLeft.setDrawLabels(false)
        yAxisLeft.setDrawGridLines(false)
        yAxisRight.setDrawLabels(false)
        yAxisRight.setDrawGridLines(false)


        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 12f
        xAxis.textColor = Color.BLACK
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawLabels(false)
        xAxis.granularity = 1f
        xAxis.axisMinimum = 0f
        xAxis.axisMaximum = if (data.size > 30) 60f else data.size.toFloat()
        chart.invalidate() // refresh

    }
}
