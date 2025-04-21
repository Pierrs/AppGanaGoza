package com.dapm.ganagoza.interfaz.vista

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.dapm.ganagoza.databinding.ActivityReglaJuegoBinding

class ReglaJuego : AppCompatActivity() {

    private lateinit var binding: ActivityReglaJuegoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReglaJuegoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
        setupUI()
        configurarEnlaces()
    }

    private fun setupListeners() {
        binding.icEncabezadoPrincipal.ivRegresar.setOnClickListener { finish() }
    }

    private fun setupUI() {
        binding.icEncabezadoPrincipal.tvTituloEncabezado.text = getString(com.dapm.ganagoza.R.string.name_reglas)
    }
    private fun configurarEnlaces() {
        binding.tvTerminos.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, "https://gana-goza-terms.vercel.app".toUri())
            startActivity(intent)
        }
        binding.tvPrivacidad.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, "https://gana-goza-privacy.vercel.app".toUri())
            startActivity(intent)
        }
    }
}
