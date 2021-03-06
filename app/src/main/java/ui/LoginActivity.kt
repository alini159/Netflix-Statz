package ui

import ui.PopularMovie.MainActivity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.netflix_statz.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import realtimeDatabase.RealTimeDataBase

class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"

    //Variaveis Globais

    private var email: String? = null
    private var senha : String? = null

    //Variaveis de Interface

    private var etLoginEmail : EditText? = null
    private var etLoginSenha : EditText? = null
    private var btnEntrar : Button? = null
    private var btnCadastrar : Button? = null
    private var mProgressBar : ProgressDialog? = null
    private var tvEsqueceuSenha : TextView? = null

    //Banco de Dados
    private var mAuth : FirebaseAuth? = null
    private var mDatabaseReference: DatabaseReference? =null
    private var mDatabase: FirebaseDatabase? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            window.setStatusBarColorTo(R.color.primaryColor)
        }

        inicializaComponentes()
    }

    private fun inicializaComponentes(){

        etLoginEmail = findViewById(R.id.loginEmail) as EditText
        etLoginSenha = findViewById(R.id.loginSenha) as EditText
        btnEntrar = findViewById(R.id.btnLogEntrar) as Button
        btnCadastrar = findViewById(R.id.btnLogCadastrar) as Button
        tvEsqueceuSenha = findViewById(R.id.logEsqueceuSenha) as TextView
        mProgressBar = ProgressDialog(this)
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()
        tvEsqueceuSenha!!
            .setOnClickListener { startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java)) }

        btnCadastrar!!
            .setOnClickListener { startActivity(Intent(this@LoginActivity, CreateAccountActivity::class.java)) }

        btnEntrar!!
            .setOnClickListener { loginUser() }

    }

    private fun loginUser(){
        email = etLoginEmail?.text.toString()
        senha = etLoginSenha?.text.toString()

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(senha)){
            mProgressBar!!.setMessage("Verificando Usuario")
            mProgressBar!!.show()

            Log.d(TAG, "Login do Usuario")

            mAuth!!.signInWithEmailAndPassword(email!! , senha!!).addOnCompleteListener(this){
                task ->

                mProgressBar!!.hide()

                //Autenticando usuario atualizando a UI com as informa????es de login

                if (task.isSuccessful){
                    Log.d(TAG, "Usuario Autenticado com sucesso")
                    atualizaUi()
                }else{
                    Log.e(TAG, "Falha na Autentica????o", task.exception)
                    Toast.makeText(this@LoginActivity, "Falha na Autentica????o.",Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(this, "Entre com mais detalhes", Toast.LENGTH_SHORT).show()
        }
    }
    private fun atualizaUi(){

        mDatabaseReference!!
            .child("nome").get().addOnSuccessListener {
                val intent = Intent(this, PerfilActivity::class.java)
                intent.putExtra("userName", "${it.value}")
                intent.putExtra("userId", mAuth!!.currentUser!!.uid)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                this.finish()
                Log.i("firebase", "Got value ${it.value}")
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }.toString()


    }




    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun Window.setStatusBarColorTo(color: Int){
        this.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        this.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        this.statusBarColor = ContextCompat.getColor(baseContext, color)
    }
}