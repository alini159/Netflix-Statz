package ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.netflix_statz.R
import ui.PopularMovie.MainActivity
import ui.SingleMovie.SingleMovie

class PerfilActivity : AppCompatActivity() {

    lateinit var perfil : Button
    lateinit var nome : TextView
    var getIntent : Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        getIntent = intent
        nome = findViewById(R.id.nomePerfil)
        perfil = findViewById(R.id.perfil)
        nome.setText(intent.getStringExtra("userName"))
        perfil.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("userId", getIntent)
            startActivity(intent)
            this.finish()
        }
    }
}