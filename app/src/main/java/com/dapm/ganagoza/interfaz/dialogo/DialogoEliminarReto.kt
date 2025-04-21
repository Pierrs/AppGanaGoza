package com.dapm.ganagoza.interfaz.dialogo

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.dapm.ganagoza.databinding.DialogoEliminarRetoBinding
import com.dapm.ganagoza.modelo.Reto
import com.dapm.ganagoza.vistaModelo.VistaModeloJuego

object DialogoEliminarReto {

    fun mostrarDialogoEliminarReto(
        contexto: Context,
        vistaModelo: VistaModeloJuego,
        reto: Reto,
        alActualizarLista: () -> Unit
    ) {
        val binding = DialogoEliminarRetoBinding.inflate(LayoutInflater.from(contexto))
        val dialogo = crearDialogo(contexto, binding)

        configurarEventos(binding, dialogo, vistaModelo, reto, alActualizarLista)
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
        vistaModelo: VistaModeloJuego,
        reto: Reto,
        alActualizarLista: () -> Unit
    ) {
        binding.idBtnNo.setOnClickListener {
            dialogo.dismiss()
        }

        binding.idBtnSi.setOnClickListener {
            vistaModelo.eliminarReto(reto)
            alActualizarLista()
            dialogo.dismiss()
        }
    }
}