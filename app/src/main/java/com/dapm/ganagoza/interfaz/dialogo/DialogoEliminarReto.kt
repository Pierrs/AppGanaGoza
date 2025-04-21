package com.dapm.ganagoza.interfaz.dialogo

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.dapm.ganagoza.databinding.DialogoEliminarRetoBinding
import com.dapm.ganagoza.modelo.Reto
import com.dapm.ganagoza.utilidades.publicidad.GestorPublicidad
import com.dapm.ganagoza.viewmodel.VistaModeloJuego

object DialogoEliminarReto {

    fun mostrarDialogoEliminarReto(
        contexto: Context,
        vistaModelo: VistaModeloJuego,
        reto: Reto,
        alActualizarLista: () -> Unit
    ) {
        val binding = DialogoEliminarRetoBinding.inflate(LayoutInflater.from(contexto))
        val dialogo = crearDialogo(contexto, binding)

        configurarEventos(binding, dialogo, contexto, vistaModelo, reto, alActualizarLista)
        dialogo.show()
    }

    private fun crearDialogo(
        contexto: Context,
        binding: DialogoEliminarRetoBinding
    ): AlertDialog {
        return AlertDialog.Builder(contexto).apply {
            setView(binding.root)
            setCancelable(false)
        }.create()
    }

    private fun configurarEventos(
        binding: DialogoEliminarRetoBinding,
        dialogo: AlertDialog,
        contexto: Context,
        vistaModelo: VistaModeloJuego,
        reto: Reto,
        alActualizarLista: () -> Unit
    ) {
        val gestorPublicidad = GestorPublicidad.obtenerInstancia()

        binding.idBtnNo.setOnClickListener {
            if (contexto is android.app.Activity) {
                gestorPublicidad.mostrarAnuncioObligatorioCerrarGuardar(contexto)
            }
            dialogo.dismiss()
        }

        binding.idBtnSi.setOnClickListener {
            vistaModelo.eliminarReto(reto)
            alActualizarLista()
            if (contexto is android.app.Activity) {
                gestorPublicidad.mostrarAnuncioObligatorioCerrarGuardar(contexto)
            }
            dialogo.dismiss()
        }
    }
}
