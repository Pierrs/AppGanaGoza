package com.dapm.ganagoza.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.dapm.ganagoza.databinding.ActivityAgregarRetoBinding
import com.dapm.ganagoza.view.adapter.AdaptadorDeRetos
import com.dapm.ganagoza.view.dialogo.DialogoAgregarReto.mostrarDialogoAgregarReto
import com.dapm.ganagoza.view.viewmodel.VistaModeloJuego

class AgregarReto : AppCompatActivity() {

    private lateinit var vista: ActivityAgregarRetoBinding
    private val modeloVista: VistaModeloJuego by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        vista = ActivityAgregarRetoBinding.inflate(layoutInflater)
        setContentView(vista.root)
        configurarUI()
        observarModeloVista()
    }

    private fun configurarUI() = with(vista) {
        icEncabezadoContenedor.ivRegresar.setOnClickListener { finish() }

        btnFlotanteAgregarReto.setOnClickListener {
            mostrarDialogoAgregarReto(this@AgregarReto, modeloVista) {
                cargarListaDeRetos()
            }
        }
        recycleView.layoutManager = LinearLayoutManager(this@AgregarReto).apply {
            reverseLayout = false
            stackFromEnd = false
        }
    }

    private fun observarModeloVista() {
        cargarListaDeRetos()
        modeloVista.estadoDeProgreso.observe(this) { estaCargando ->
            vista.progress.isVisible = estaCargando
        }
    }

    private fun cargarListaDeRetos() {
        modeloVista.obtenerTodosLosRetos()
        modeloVista.retosDisponibles.observe(this) { retos ->
            vista.recycleView.adapter = AdaptadorDeRetos(retos, modeloVista)
            vista.recycleView.scrollToPosition(0)
        }
    }
}
