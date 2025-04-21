package com.dapm.ganagoza.interfaz.dialogo

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.core.widget.addTextChangedListener
import com.dapm.ganagoza.databinding.DialogoEditarRetoBinding
import com.dapm.ganagoza.modelo.Reto
import com.dapm.ganagoza.utilidades.publicidad.GestorPublicidad
import com.dapm.ganagoza.viewmodel.VistaModeloJuego

object DialogoEditarReto {

    fun mostrarDialogoEditarReto(
        contexto: Context,
        vistaModelo: VistaModeloJuego,
        reto: Reto,
        alActualizarLista: () -> Unit
    ) {
        val binding = DialogoEditarRetoBinding.inflate(LayoutInflater.from(contexto))
        val dialogo = crearDialogo(contexto, binding)

        configurarEventos(binding, dialogo, contexto, vistaModelo, reto, alActualizarLista)
        configurarCamposIniciales(binding, reto)
        dialogo.show()
    }

    private fun crearDialogo(
        contexto: Context,
        binding: DialogoEditarRetoBinding
    ): AlertDialog {
        return AlertDialog.Builder(contexto).apply {
            setView(binding.root)
            setCancelable(false)
        }.create()
    }

    private fun configurarCamposIniciales(
        binding: DialogoEditarRetoBinding,
        reto: Reto
    ) {
        binding.idEditarReto.setText(reto.descripcionReto)
        binding.btnEditar.isEnabled = reto.descripcionReto.isNotBlank()

        binding.idEditarReto.addTextChangedListener {
            binding.btnEditar.isEnabled = it.toString().isNotBlank()
        }
    }

    private fun configurarEventos(
        binding: DialogoEditarRetoBinding,
        dialogo: AlertDialog,
        contexto: Context,
        vistaModelo: VistaModeloJuego,
        reto: Reto,
        alActualizarLista: () -> Unit
    ) {
        val gestorPublicidad = GestorPublicidad.obtenerInstancia()

        binding.idBtnCancelar.setOnClickListener {

            if (contexto is Activity) {
                gestorPublicidad.mostrarAnuncioObligatorioCerrarGuardar(contexto)
            }
            dialogo.dismiss()
        }

        binding.btnEditar.setOnClickListener {
            val nuevaDescripcion = binding.idEditarReto.text.toString().trim()

            if (nuevaDescripcion.isNotEmpty()) {
                val retoActualizado = Reto(reto.retoId, nuevaDescripcion)
                vistaModelo.actualizarReto(retoActualizado)

                if (contexto is Activity) {
                    gestorPublicidad.mostrarAnuncioObligatorioCerrarGuardar(contexto)
                }
                dialogo.dismiss()
                alActualizarLista()
            }
        }
    }
}