package com.dapm.ganagoza.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.dapm.ganagoza.databinding.ActivityAgregarRetoBinding
import com.dapm.ganagoza.view.adapter.AdaptadorDeRetos
import com.dapm.ganagoza.view.dialogo.DialogoAgregarReto.showDialogoAgregarReto
import com.dapm.ganagoza.view.viewmodel.VistaModeloJuego

class AgregarReto : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarRetoBinding
    private val vistaModeloJuego: VistaModeloJuego by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAgregarRetoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controladores()
        observadorViewModel()
    }

    private fun controladores() {
        binding.icContenedorBarra.ivAtras.setOnClickListener {
            finish()
        }
        binding.floatBtn.setOnClickListener {
            showDialogoAgregarReto(this, vistaModeloJuego) {
                observadorListaReto()
            }
        }
    }

    private fun observadorViewModel() {
        observadorListaReto()
        observadorProgress()
    }

    private fun observadorProgress() {
        vistaModeloJuego.progresSstate.observe(this) { status ->
            binding.progress.isVisible = status
        }
    }

    private fun observadorListaReto() {
        vistaModeloJuego.obtenerTodosLosRetos()
        vistaModeloJuego.listaReto.observe(this) { lista ->
            val recycler = binding.recycleView
            val layoutManager = LinearLayoutManager(this)
            layoutManager.reverseLayout = false // Asegura el orden correcto de los elementos
            layoutManager.stackFromEnd = false // Evita que se apilen al final
            recycler.layoutManager = layoutManager
            val adapter = AdaptadorDeRetos(lista, vistaModeloJuego)
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()
            recycler.scrollToPosition(0) // Desplaza al primer elemento automáticamente
        }
    }
}