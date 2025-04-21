package com.dapm.ganagoza.interfaz.vista

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.dapm.ganagoza.databinding.ActivityAgregarRetoBinding
import com.dapm.ganagoza.adaptador.AdaptadorDeRetos
import com.dapm.ganagoza.interfaz.dialogo.DialogoAgregarReto.mostrarDialogoAgregarReto
import com.dapm.ganagoza.viewmodel.VistaModeloJuego

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
        icEncabezadoPrincipal.ivRegresar.setOnClickListener { finish() }

        btnFlotanteAgregarReto.setOnClickListener {
            mostrarDialogoAgregarReto(this@AgregarReto, modeloVista) {
                cargarListaDeRetos()
            }
        }
        listaElementos.layoutManager = LinearLayoutManager(this@AgregarReto).apply {
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
            vista.listaElementos.adapter = AdaptadorDeRetos(retos, modeloVista)
            vista.listaElementos.scrollToPosition(0)
        }
    }
}
