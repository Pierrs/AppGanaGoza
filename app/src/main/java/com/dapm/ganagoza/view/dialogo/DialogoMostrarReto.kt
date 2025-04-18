package com.dapm.ganagoza.view.dialogo

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.dapm.ganagoza.databinding.DialogoMostrarRetoBinding

object DialogoMostrarReto {
    fun mostrarDialogoReto(context: Context, audioFondo: MediaPlayer, mostrarReto: String) {
        val binding = crearBinding(context)
        val alertDialog = crearDialogo(context, binding)

        configurarVista(binding, mostrarReto)
        configurarAcciones(binding, alertDialog, audioFondo)

        alertDialog.show()
    }
    private fun crearBinding(context: Context) = DialogoMostrarRetoBinding.inflate(LayoutInflater.from(context))
    private fun crearDialogo(context: Context, binding: DialogoMostrarRetoBinding): AlertDialog {
        return AlertDialog.Builder(context)
            .setView(binding.root)
            .setCancelable(false)
            .create().apply {
                window?.setBackgroundDrawableResource(android.R.color.transparent)
            }
    }
    private fun configurarVista(binding: DialogoMostrarRetoBinding, mostrarReto: String) {
        binding.tvMostrarReto.text = mostrarReto
    }
    private fun configurarAcciones(binding: DialogoMostrarRetoBinding, alertDialog: AlertDialog, audioFondo: MediaPlayer) {
        binding.idBtnCancelar.setOnClickListener {
            audioFondo.start()
            alertDialog.dismiss()
        }
    }
}
