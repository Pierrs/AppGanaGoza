package com.dapm.ganagoza.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.dapm.ganagoza.R
import com.dapm.ganagoza.databinding.ActivityAgregarRetoBinding
import com.dapm.ganagoza.view.adapter.RetoAdapter
import com.dapm.ganagoza.view.dialogo.DialogoAgregarReto.showDialogoAgregarReto
import com.dapm.ganagoza.view.viewmodel.JuegoViewModel

class AgregarReto : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarRetoBinding
    private val juegoViewModel: JuegoViewModel by viewModels()

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
            showDialogoAgregarReto(this, juegoViewModel,) {
                observadorListaReto()
            }
        }
    }
    private fun observadorViewModel() {
        observadorListaReto()
        observadorProgress()
    }

    private fun observadorProgress() {
       juegoViewModel.progresSstate.observe(this){
           status->
           binding.progress.isVisible=status
       }
    }

    private fun observadorListaReto() {
        juegoViewModel.obtenerListaReto()
        juegoViewModel.listaReto.observe(this) { lista ->
            val recycler=binding.recycleView
            val layoutManager=LinearLayoutManager(this)
            layoutManager.reverseLayout=true
            recycler.layoutManager=layoutManager
            val adapter=RetoAdapter(lista,juegoViewModel)
            recycler.adapter=adapter
            adapter.notifyDataSetChanged()
        }
    }
}

