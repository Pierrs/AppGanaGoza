package com.dapm.ganagoza.interfaz.dialogo

import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.dapm.ganagoza.databinding.DialogoMostrarRetoBinding
import com.dapm.ganagoza.utilidades.publicidad.GestorPublicidad

object DialogoMostrarReto {

    fun mostrarDialogoReto(context: Context, audioFondo: MediaPlayer, mostrarReto: String) {
        val binding = DialogoMostrarRetoBinding.inflate(LayoutInflater.from(context))
        val alertDialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .setCancelable(false)
            .create().apply {
                window?.setBackgroundDrawableResource(android.R.color.transparent)
            }

        binding.tvMostrarReto.text = mostrarReto

        binding.idBtnCerrar.setOnClickListener {
            if (context is Activity) {
                GestorPublicidad.obtenerInstancia().mostrarAnuncioObligatorioIdioma(context) {
                    audioFondo.start()
                    alertDialog.dismiss()
                }
            } else {
                audioFondo.start()
                alertDialog.dismiss()
            }
        }

        alertDialog.show()
    }
}
