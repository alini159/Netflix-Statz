package com.example.netflix_statz

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateAccountActivity : AppCompatActivity() {

    //Variaveis de Interface
    private var etCadastroNome : EditText? = null
    private var etCadastroDataNascimento : EditText? = null
    private var etCadastroEmail : EditText? = null
    private var etCadastroSenha : EditText? = null
    private var btnCadastrar : Button? = null
    private var mProgressBar : ProgressDialog? = null
    //Banco de Dados
    private var mDatabaseReference: DatabaseReference? =null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private val TAG = "CreateAccountActivity"
    //Variaveis Globais
    private var nome : String? = null
    private var dataNascimento : String? = null
    private var email : String? = null
    private var senha : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        inicializarComponentes()
    }

    private fun inicializarComponentes(){
        etCadastroNome = findViewById(R.id.cadastroNome) as EditText
        etCadastroEmail= findViewById(R.id.cadastroEmail) as EditText
        etCadastroDataNascimento = findViewById(R.id.cadastroDataNascimento) as EditText
        etCadastroSenha = findViewById(R.id.cadastroSenha) as EditText
        btnCadastrar = findViewById(R.id.btnCadCadastrar) as Button
        mProgressBar = ProgressDialog(this)

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()

        btnCadastrar!!.setOnClickListener { criarNovaConta() }
    }

    private fun criarNovaConta(){

        nome = etCadastroNome?.text.toString()
        dataNascimento = etCadastroDataNascimento?.text.toString()
        senha = etCadastroSenha?.text.toString()
        email = etCadastroEmail?.text.toString()

        if (!TextUtils.isEmpty(nome) && !TextUtils.isEmpty(dataNascimento) && !TextUtils.isEmpty(senha) && !TextUtils.isEmpty(email)){
            Toast.makeText(this, "Informações Preenchidas Corretamente",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Entre com mais detalhes",Toast.LENGTH_SHORT).show()
        }

        mProgressBar!!.setMessage("Registrando Usuario")
        mProgressBar!!.show()

        mAuth!!.createUserWithEmailAndPassword(email!!, senha!!).addOnCompleteListener(this){ task ->
            mProgressBar!!.hide()

            if (task.isSuccessful){
                Log.d(TAG,"CrateUserWithEmailAndPassword:Sucess")

                val userId = mAuth!!.currentUser!!.uid

                //Verificar se o usuario vericficou o email
                verificarEmail()

                val currentUserDb = mDatabaseReference!!.child(userId)
                currentUserDb.child("nome completo").setValue(nome)

                //Atualizar as informações no banco de dados
                atualizarUsuarioInfoeUi()

            } else {
                Log.w(TAG, "CrateUserWithEmailAndPassword:Failure",task.exception)
                Toast.makeText(this@CreateAccountActivity, "Falha na Autenticação", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun atualizarUsuarioInfoeUi(){

        //Iniciar uma nova Activity
        val intent = Intent(this@CreateAccountActivity,  MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun verificarEmail(){
        val mUser = mAuth!!.currentUser
        mUser!!.sendEmailVerification().addOnCompleteListener(this){
            task ->

            if (task.isSuccessful){
                Toast.makeText(this@CreateAccountActivity, "Verificação de email enviada para" + mUser.getEmail(),
                Toast.LENGTH_LONG).show()
            } else {
                Log.e(TAG, "sendEmailVerification", task.exception)
                Toast.makeText(this@CreateAccountActivity, "Falha no envio da verificação de email",Toast.LENGTH_SHORT).show()
            }
        }
    }
}