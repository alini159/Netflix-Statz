package realtimeDatabase

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import data.vo.Movie
import data.vo.MovieDetails
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class RealTimeDataBase {

    //Banco de Dados
    private var mDatabaseReference: DatabaseReference? =null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private var user : String? = null
    private var currentUserDb : DatabaseReference? = null
    private var mapMovie: ArrayList<MovieDetails>

    constructor(userId : String ){
        mapMovie = ArrayList()
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()
        user = userId
        currentUserDb = mDatabaseReference!!.child(user!!)
    }

//    fun createProfile (userName : String , userEmail : String, userBornDate : String  ){
//        currentUserDb!!.child("nome").setValue(userName)
//        currentUserDb!!.child("email").setValue(userEmail)
//        currentUserDb!!.child("data").setValue(userBornDate)
//    }

    fun getcurrentUserDb(): DatabaseReference {
        return currentUserDb!!.child(user!!).child("nome")
            .child("watchlist")
    }

    fun getNome() : String{
        var nome : String? = "Alini Rodrigues"
        nome = currentUserDb!!.child("Users")
            .child(user!!)
            .child("nome").get().addOnSuccessListener {
                it.value
                Log.i("firebase", "Got value ${it.value}")
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }.toString()
        return nome!!
    }

    fun watchlistMovies (movieId: Int, movie:MovieDetails){
        mapMovie.add(movie)
        println(mapMovie.size)

        currentUserDb!!.child(user!!).child("nome")
            .child("watchlist")
            .child(movie.id.toString())

        currentUserDb!!.child(user!!).child("nome")
                .child("watchlist")
                .child("nome").child(movie.id.toString()).setValue(movie.title)

        currentUserDb!!.child(user!!).child("nome")
            .child("watchlist")
            .child("ano").child(movie.id.toString()).setValue(movie.releaseDate)

        currentUserDb!!.child(user!!).child("nome")
            .child("watchlist")
            .child("foto").child(movie.id.toString()).setValue(movie.posterPath)
    }

    fun getMoviesList(): ArrayList<MovieDetails>{
        return mapMovie
    }

    fun getWatchlistMovie(): Task<DataSnapshot> {
        return currentUserDb!!.child("Users").child(user!!)
            .child("nome")
            .child("Watchlist")
            .get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

}




