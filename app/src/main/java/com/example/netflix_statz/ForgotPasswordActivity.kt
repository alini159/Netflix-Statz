package com.example.netflix_statz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private val TAG = "ForgotPAsswordActivity"

    //Variaveis de Inetrface

    private var etEmail : EditText? = null
    private var btnEsqueceuSenhaSubmit : Button? = null

    //Banco de Dados

    private var mAuth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        inicializarComponentes()
    }

    private fun inicializarComponentes(){
        etEmail = findViewById(R.id.esqueceuSenhaEmail) as EditText
        btnEsqueceuSenhaSubmit = findViewById(R.id.btnEsqueceuSenhaSubmit) as Button

        mAuth = FirebaseAuth.getInstance()

        btnEsqueceuSenhaSubmit!!
            .setOnClickListener { enviarEmailVerificador() }
    }

    private fun enviarEmailVerificador(){
        val email = etEmail?.text.toString()

        if(!TextUtils.isEmpty(email)){
            mAuth!!
                .sendPasswordResetEmail(email)
                .addOnCompleteListener{
                    task ->
                    if (task.isSuccessful){
                        val message = "Email Enviado"
                        Log.d(TAG,message)
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        atualizarUI()
                    } else {
                        Log.w(TAG, task.exception!!.message!!)
                        Toast.makeText(this, "Nenhum Usuario Encontrado com este Email", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Entre com um Email Valido", Toast.LENGTH_SHORT).show()
        }
    }

    private fun atualizarUI(){
        val intent = Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}