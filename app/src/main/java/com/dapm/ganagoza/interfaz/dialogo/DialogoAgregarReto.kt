package com.dapm.ganagoza.interfaz.dialogo

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.dapm.ganagoza.databinding.DialogoAgregarRetoBinding
import com.dapm.ganagoza.modelo.Reto
import com.dapm.ganagoza.vistaModelo.VistaModeloJuego

object DialogoAgregarReto {

    fun mostrarDialogoAgregarReto(
        contexto: Context,
        vistaModelo: VistaModeloJuego,
        alActualizarLista: () -> Unit
    ) {
        val binding = DialogoAgregarRetoBinding.inflate(LayoutInflater.from(contexto))
        val dialogo = crearDialogo(contexto, binding)

        configurarEventos(binding, dialogo, vistaModelo, alActualizarLista)
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
        vistaModelo: VistaModeloJuego,
        alActualizarLista: () -> Unit
    ) {
        binding.idEscribirReto.addTextChangedListener {
            binding.idBtnGuardar.isEnabled = it.toString().isNotBlank()
        }

        binding.idBtnCancelar.setOnClickListener {
            dialogo.dismiss()
        }

        binding.idBtnGuardar.setOnClickListener {
            val descripcion = binding.idEscribirReto.text.toString().trim()
            if (descripcion.isNotEmpty()) {
                val nuevoReto = Reto(descripcionReto = descripcion)
                vistaModelo.agregarReto(nuevoReto)
                alActualizarLista()
            }
            dialogo.dismiss()
        }
    }
}
