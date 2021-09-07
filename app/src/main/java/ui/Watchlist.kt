package ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.example.netflix_statz.R
import realtimeDatabase.RealTimeDataBase


class Watchlist : AppCompatActivity() {



    lateinit var campo : LinearLayout
    lateinit var currentUser : RealTimeDataBase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watchlist)

        currentUser = RealTimeDataBase(intent.getStringExtra("userId").toString())
        campo = findViewById(R.id.campo)
        populateWatchlist(this)
    }

    fun populateWatchlist(context: Context){

        val user = currentUser.getcurrentUserDb()



        user.get().addOnSuccessListener {
            val movie : TextView = TextView(context)
            movie.setText(it.value.toString() + "\n")
            movie.setTextSize(20F)
            campo.addView(movie)
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

    }

}
