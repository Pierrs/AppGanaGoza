package com.dapm.ganagoza.view

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dapm.ganagoza.R
import com.dapm.ganagoza.databinding.ActivityRecuperarPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class RecuperarPassword : AppCompatActivity() {

    private lateinit var binding: ActivityRecuperarPasswordBinding
    private lateinit var progressDialog:ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRecuperarPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.IbRegresar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
        binding.BotonEnviarInstrucciones.setOnClickListener {
            validarEmail()
        }

    }

    private var email =""
    private fun validarEmail() {
        email=binding.EtEmail.text.toString().trim()
        if(email.isEmpty()){
            Toast.makeText(this,"Ingrese su correo",Toast.LENGTH_SHORT).show()
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.EtEmail.error="Email inválido"
            binding.EtEmail.requestFocus()
        }else{
            enviarInstrucciones()
        }
    }

    private fun enviarInstrucciones() {
        progressDialog.setMessage("Enviando insutrcciones al email ${email}")
        progressDialog.dismiss()

        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this,"Las insturcciones fueron enviadas a su correo registrado",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                e->
                progressDialog.dismiss()
                Toast.makeText(this,"No se enviaron las instruccion debido a ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }
}