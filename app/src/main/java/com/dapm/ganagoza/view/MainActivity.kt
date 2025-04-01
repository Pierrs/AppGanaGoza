package com.dapm.ganagoza.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dapm.ganagoza.R
import com.dapm.ganagoza.databinding.ActivityMainBinding
import com.dapm.ganagoza.view.fragment.FragmentJuego
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fireBaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar Firebase Auth
        fireBaseAuth = FirebaseAuth.getInstance()

        // Cargar solo el fragmento de juego
        val fragment = FragmentJuego()
        supportFragmentManager.beginTransaction()
            .replace(R.id.FragmentL1, fragment)
            .commit()
    }
}