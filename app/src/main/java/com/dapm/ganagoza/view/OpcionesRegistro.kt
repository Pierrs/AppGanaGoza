package com.dapm.ganagoza.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.dapm.ganagoza.databinding.ActivityOpcionesRegistroBinding
import com.google.firebase.auth.FirebaseAuth

class OpcionesRegistro : AppCompatActivity() {
    private lateinit var binding:ActivityOpcionesRegistroBinding

    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityOpcionesRegistroBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        firebaseAuth=FirebaseAuth.getInstance()
        comprobarRegistro()
        binding.IngresarCuenta.setOnClickListener{
            startActivity(Intent(this@OpcionesRegistro, CuentaExistente::class.java))
        }



    }
    private fun comprobarRegistro(){
        if(firebaseAuth.currentUser!=null){
            startActivity(Intent(this,MainActivity::class.java))
            finishAffinity()
        }
    }
}