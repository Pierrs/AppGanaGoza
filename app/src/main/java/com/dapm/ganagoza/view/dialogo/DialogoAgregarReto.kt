package com.dapm.ganagoza.view.dialogo

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.dapm.ganagoza.databinding.DialogoAgregarRetoBinding
import com.dapm.ganagoza.model.Reto
import com.dapm.ganagoza.view.viewmodel.JuegoViewModel

object DialogoAgregarReto {
    fun showDialogoAgregarReto(
        context: Context,
        juegoViewModel: JuegoViewModel,
        actualizarLista: () -> Unit
    ) {
        val inflater = LayoutInflater.from(context)
        val binding = DialogoAgregarRetoBinding.inflate(inflater)

        val alertDialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .setCancelable(false) // Evita que se cierre tocando fuera
            .create()

        binding.idEditPenitencia.addTextChangedListener {
            binding.idBtnGuardar.isEnabled = binding.idEditPenitencia.text.toString().isNotEmpty()
        }

        binding.idBtnCancelar.setOnClickListener {
            alertDialog.dismiss()
        }

        binding.idBtnGuardar.setOnClickListener {
            val descripcion = binding.idEditPenitencia.text.toString().trim()
            val reto = Reto(descripcionReto = descripcion)
            juegoViewModel.agregarReto(reto)
            alertDialog.dismiss()
            actualizarLista.invoke()
        }

        alertDialog.show()

        // Forzar que el diálogo aparezca en el centro y tenga el tamaño adecuado
        alertDialog.window?.setLayout(
            android.view.WindowManager.LayoutParams.WRAP_CONTENT,
            android.view.WindowManager.LayoutParams.WRAP_CONTENT
        )

        // Asegurar que el fondo del diálogo sea transparente para evitar el "fondo negro"
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}
