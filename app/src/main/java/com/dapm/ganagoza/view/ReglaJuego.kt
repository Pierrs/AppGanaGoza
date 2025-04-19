package com.dapm.ganagoza.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
    }

    private fun setupListeners() {
        binding.icEncabezadoPrincipal.ivRegresar.setOnClickListener { finish() }
    }

    private fun setupUI() {
        binding.icEncabezadoPrincipal.tvTituloEncabezado.text = getString(com.dapm.ganagoza.R.string.name_reglas)
    }
}
