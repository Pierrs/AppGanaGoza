package com.dapm.ganagoza.interfaz.dialogo

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.dapm.ganagoza.databinding.DialogoSeleccionIdiomaBinding
import com.dapm.ganagoza.utilidades.GestorIdioma
import com.dapm.ganagoza.utilidades.publicidad.GestorPublicidad
import androidx.core.graphics.drawable.toDrawable

class DialogoIdioma(private val contexto: Context) {

    private val binding: DialogoSeleccionIdiomaBinding by lazy {
        DialogoSeleccionIdiomaBinding.inflate(LayoutInflater.from(contexto))
    }

    private val dialogo: Dialog by lazy {
        Dialog(contexto).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            window?.apply {
                setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
                setLayout(
                    android.view.WindowManager.LayoutParams.MATCH_PARENT,
                    android.view.WindowManager.LayoutParams.WRAP_CONTENT
                )
            }
            setCancelable(true)
        }
    }

    private val gestorPublicidad: GestorPublicidad by lazy {
        GestorPublicidad.obtenerInstancia()
    }

    fun mostrar() {
        configurarEscuchadores()
        dialogo.show()
    }

    private fun configurarEscuchadores() {
        binding.formatoEspanol.setOnClickListener {
            mostrarAnuncioCambioIdioma(GestorIdioma.ESPANOL)
        }

        binding.formatoIngles.setOnClickListener {
            mostrarAnuncioCambioIdioma(GestorIdioma.INGLES)
        }

        binding.formatoFrancia.setOnClickListener {
            mostrarAnuncioCambioIdioma(GestorIdioma.FRANCES)
        }

        binding.formatoBrasil.setOnClickListener {
            mostrarAnuncioCambioIdioma(GestorIdioma.PORTUGUES)
        }
    }

    private fun mostrarAnuncioCambioIdioma(codigoIdioma: String) {

        dialogo.dismiss()
        gestorPublicidad.mostrarAnuncioObligatorioIdioma(contexto as AppCompatActivity) {
            GestorIdioma.establecerLocale(contexto, codigoIdioma)
        }
    }
}