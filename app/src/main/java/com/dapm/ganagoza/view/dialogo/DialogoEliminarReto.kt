package com.dapm.ganagoza.view.dialogo

import android.app.AlertDialog
import android.content.Context
import com.dapm.ganagoza.R
import com.dapm.ganagoza.model.Reto
import com.dapm.ganagoza.view.viewmodel.JuegoViewModel

fun DialogoAgregarReto(context: Context,juegoViewModel: JuegoViewModel,reto: Reto):AlertDialog{
    val builder = AlertDialog.Builder(context)
    builder.setCancelable(false)
    builder.setTitle(R.string.title_dialog_eliminar)
        .setMessage("\n${reto.descripcionReto}")
        .setPositiveButton("SI"){
            dialog,with->
            juegoViewModel.eliminarReto(reto)
            dialog.dismiss()
            juegoViewModel.obtenerListaReto()
        }
        .setNegativeButton("NO"){
            dialog,with ->
            dialog.dismiss()
        }
    return builder.create()
}