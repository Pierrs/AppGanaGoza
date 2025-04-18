package com.dapm.ganagoza.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.dapm.ganagoza.R
import com.dapm.ganagoza.databinding.ActivityReglaJuegoBinding


class ReglaJuego : AppCompatActivity() {
    private lateinit var binding: ActivityReglaJuegoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityReglaJuegoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controladores()
    }

    private fun controladores() {
        binding.icContenedorBarra.ivAtras.setOnClickListener {
            finish()
        }
        binding.icContenedorBarra.tvTituloBarra.text=getString(R.string.name_reglas)
    }
}
