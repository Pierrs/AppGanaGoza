package com.dapm.ganagoza.interfaz.dialogo

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.dapm.ganagoza.databinding.DialogoAgregarRetoBinding
import com.dapm.ganagoza.modelo.Reto
import com.dapm.ganagoza.utilidades.publicidad.GestorPublicidad
import com.dapm.ganagoza.viewmodel.VistaModeloJuego

object DialogoAgregarReto {

    fun mostrarDialogoAgregarReto(
        activity: Activity,
        vistaModelo: VistaModeloJuego,
        alActualizarLista: () -> Unit
    ) {
        val binding = DialogoAgregarRetoBinding.inflate(LayoutInflater.from(activity))
        val dialogo = crearDialogo(activity, binding)

        configurarEventos(binding, dialogo, activity, vistaModelo, alActualizarLista)
        dialogo.show()
    }

    private fun crearDialogo(
        contexto: Context,
        binding: DialogoAgregarRetoBinding
    ): AlertDialog {
        return AlertDialog.Builder(contexto)
            .setView(binding.root)
            .setCancelable(false)
            .create()
    }

    private fun configurarEventos(
        binding: DialogoAgregarRetoBinding,
        dialogo: AlertDialog,
        activity: Activity,
        vistaModelo: VistaModeloJuego,
        alActualizarLista: () -> Unit
    ) {
        val gestorPublicidad = GestorPublicidad.obtenerInstancia()

        binding.idEscribirReto.addTextChangedListener {
            binding.idBtnGuardar.isEnabled = it.toString().isNotBlank()
        }

        binding.idBtnCancelar.setOnClickListener {
            gestorPublicidad.mostrarAnuncioObligatorioCerrarGuardar(activity)
            dialogo.dismiss()
        }

        binding.idBtnGuardar.setOnClickListener {
            val descripcion = binding.idEscribirReto.text.toString().trim()
            if (descripcion.isNotEmpty()) {
                val nuevoReto = Reto(descripcionReto = descripcion)
                vistaModelo.agregarReto(nuevoReto)
                gestorPublicidad.mostrarAnuncioObligatorioCerrarGuardar(activity)

                dialogo.dismiss()
                alActualizarLista()
            }
        }
    }
}