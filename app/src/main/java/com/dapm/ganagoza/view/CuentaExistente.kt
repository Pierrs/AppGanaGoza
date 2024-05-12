package com.dapm.ganagoza.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dapm.ganagoza.R
import com.dapm.ganagoza.databinding.ActivityCuentaExistenteBinding
import com.dapm.ganagoza.databinding.ActivityOpcionesRegistroBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class CuentaExistente : AppCompatActivity() {

    private lateinit var binding: ActivityCuentaExistenteBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCuentaExistenteBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        firebaseAuth=FirebaseAuth.getInstance()
        progressDialog=ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)
        binding.BotonIngresar.setOnClickListener {
            validarInformacion()
        }
        binding.Registro.setOnClickListener{
            startActivity(Intent(this@CuentaExistente,Registro_usuario::class.java))
        }
        binding.TvRecuperar.setOnClickListener {
            startActivity(Intent(this@CuentaExistente,RecuperarPassword::class.java))
        }

    }
    private var email=""
    private var contraseña=""
    private fun validarInformacion() {
        email=binding.EtEmail.text.toString().trim()
        contraseña=binding.EtPassword.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.EtEmail.error="Email incorrecto"
            binding.EtEmail.requestFocus()
        }
        else if(email.isEmpty()){
            binding.EtEmail.error="Ingresar Email"
            binding.EtEmail.requestFocus()
        }
        else if(contraseña.isEmpty()){
            binding.EtPassword.error="Ingresar contraseña"
            binding.EtPassword.requestFocus()
        }
        else{
            SessionUsuario()
        }
    }

    private fun SessionUsuario() {
        progressDialog.setMessage("Ingresando")
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email,contraseña)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this,MainActivity::class.java))
                finishAffinity()
                Toast.makeText(
                    this,
                    "Bienvenido",
                    Toast.LENGTH_SHORT
                ).show()


            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "No se pudo iniciar Session debido a ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

            }
    }
}