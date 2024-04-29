package com.dapm.ganagoza.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dapm.ganagoza.R
import com.dapm.ganagoza.databinding.ActivityMainBinding
import com.dapm.ganagoza.view.fragment.FragmentCuenta
import com.dapm.ganagoza.view.fragment.FragmentJuego
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fireBaseAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fireBaseAuth = FirebaseAuth.getInstance()
        comprobarRegistro()
        verFragmentJuego()
        binding.BotonNV.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.Item_juegos->{
                    verFragmentJuego()
                    true
                }
                R.id.Item_cuenta->{
                    verFragmentCuenta()
                    true
                }
                else ->{
                    false
                }
            }
        }



    }
    private fun comprobarRegistro(){
        if(fireBaseAuth.currentUser==null){
            startActivity(Intent(this,OpcionesRegistro::class.java))
            finishAffinity()
        }
    }
    private fun verFragmentJuego(){
        binding.TituloRl.text="Juego"
        val fragment = FragmentJuego()
        val fragmentTransient=supportFragmentManager.beginTransaction()
        fragmentTransient.replace(binding.FragmentL1.id,fragment,"FragmentJuego")
        fragmentTransient.commit()
    }
    private fun verFragmentCuenta(){
        binding.TituloRl.text="Mi cuenta"
        val fragment = FragmentCuenta()
        val fragmentTransient=supportFragmentManager.beginTransaction()
        fragmentTransient.replace(binding.FragmentL1.id,fragment,"FragmentCuenta")
        fragmentTransient.commit()
    }
}