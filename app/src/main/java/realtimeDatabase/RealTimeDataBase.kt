package realtimeDatabase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RealTimeDataBase {

    //Banco de Dados
    private var mDatabaseReference: DatabaseReference? =null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private var user : String? = null
    private lateinit var watchlist : MutableMap<Int, String>
    private var currentUserDb : DatabaseReference? = null

    constructor(userId : String ){
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()
        user = userId
        currentUserDb = mDatabaseReference!!.child(user!!)
    }

    fun createProfile (userName : String , userEmail : String, userBornDate : String ,isMainProfile : Boolean ){
        currentUserDb!!.child("nome completo").setValue(userName)
    }

    fun watchlistMovies (movieId: Int, movieName:String){
        currentUserDb!!.child("nome completo")
                .child("watchlist")
                .child(movieId.toString()).setValue(movieName)
    }

}




