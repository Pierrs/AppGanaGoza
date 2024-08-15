package com.dapm.ganagoza.view.dialogo

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.core.widget.addTextChangedListener
import com.dapm.ganagoza.databinding.DialogoEditarRetoBinding
import com.dapm.ganagoza.model.Reto
import com.dapm.ganagoza.view.viewmodel.JuegoViewModel

object DialogoEditarReto {
    fun showDialogEditReto(
        contex: Context,
        juegoViewModel: JuegoViewModel,
        reto: Reto,

        actualizarLista: () -> Unit
    ) {
        val inflater = LayoutInflater.from(contex)
        val binding = DialogoEditarRetoBinding.inflate(inflater)
        var alertDialog = AlertDialog.Builder(contex).create()
        alertDialog.setCancelable(false)
        alertDialog.setView(binding.root)

        binding.idEditReto.addTextChangedListener{
            binding.btnEditar.isEnabled=binding.idEditReto.text.toString().isNotEmpty()
        }

        binding.idEditReto.setText(reto.descripcionReto)
        binding.idBtnCancelar.setOnClickListener {
            alertDialog.dismiss()
        }

        binding.btnEditar.setOnClickListener {
            val descripcion = binding.idEditReto.text.toString().trim()
            val reto =Reto(reto.id,descripcion)

            juegoViewModel.actualizarReto(reto)
            alertDialog.dismiss()
            actualizarLista.invoke()
        }
        alertDialog.show()

    }

}
