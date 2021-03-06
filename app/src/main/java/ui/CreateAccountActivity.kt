package ui

import ui.PopularMovie.MainActivity
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.netflix_statz.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import realtimeDatabase.RealTimeDataBase as RealTimeDataBase1

class CreateAccountActivity : AppCompatActivity() {

    //Variaveis de Interface
    private var etCadastroNome : EditText? = null
    private var etCadastroDataNascimento : EditText? = null
    private var etCadastroEmail : EditText? = null
    private var etCadastroSenha : EditText? = null
    private var btnCadastrar : Button? = null
    private var mProgressBar : ProgressDialog? = null
    //Banco de Dados

    private var realTimeDataBase : RealTimeDataBase1? = null
    private var mDatabaseReference: DatabaseReference? =null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private val TAG = "CreateAccountActivity"
    //Variaveis Globais
    private var uid : String? = null
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
            Toast.makeText(this, "Informa????es Preenchidas Corretamente",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Entre com mais detalhes",Toast.LENGTH_SHORT).show()
        }

        mProgressBar!!.setMessage("Registrando Usuario")
        mProgressBar!!.show()

        mAuth!!.createUserWithEmailAndPassword(email!!, senha!!).addOnCompleteListener(this){ task ->


            mProgressBar!!.hide()

            if (task.isSuccessful){
                Log.d(TAG,"CrateUserWithEmailAndPassword:Sucess")

                uid = mAuth!!.currentUser!!.uid

                //Verificar se o usuario vericficou o email
                verificarEmail()

                //armazena os dados da primeira conta
                realTimeDataBase = realtimeDatabase.RealTimeDataBase(uid!!)
                mDatabaseReference!!.child("nome").setValue(nome)
                mDatabaseReference!!.child("email").setValue(email)
                mDatabaseReference!!.child("data").setValue(dataNascimento)


                //Atualizar as informa????es no banco de dados
                atualizarUsuarioInfoeUi()

            } else {
                Log.w(TAG, "CrateUserWithEmailAndPassword:Failure",task.exception)
                Toast.makeText(this@CreateAccountActivity, "Falha na Autentica????o", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun atualizarUsuarioInfoeUi(){

        //Iniciar uma nova Activity
        val intent = Intent(this@CreateAccountActivity,  LoginActivity::class.java)
        intent.putExtra("userId", uid!!)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        this.finish()
    }

    private fun verificarEmail(){
        val mUser = mAuth!!.currentUser
        mUser!!.sendEmailVerification().addOnCompleteListener(this){
            task ->

            if (task.isSuccessful){
                Toast.makeText(this@CreateAccountActivity, "Verifica????o de email enviada para" + mUser.getEmail(),
                Toast.LENGTH_LONG).show()
            } else {
                Log.e(TAG, "sendEmailVerification", task.exception)
                Toast.makeText(this@CreateAccountActivity, "Falha no envio da verifica????o de email",Toast.LENGTH_SHORT).show()
            }
        }
    }
}