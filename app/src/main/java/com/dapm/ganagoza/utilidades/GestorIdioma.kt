package com.dapm.ganagoza.utilidades

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale
import androidx.core.content.edit

object GestorIdioma {
    const val ESPANOL = "es"
    const val INGLES = "en"
    const val FRANCES = "fr"
    const val PORTUGUES = "pt"
    private var idiomaActual: String? = null

    fun establecerLocale(contexto: Context, codigoIdioma: String) {
        val locale = Locale(codigoIdioma)
        Locale.setDefault(locale)

        val resources = contexto.resources
        val configuration = resources.configuration

        configuration.setLocale(locale)
        @Suppress("DEPRECATION")
        resources.updateConfiguration(configuration, resources.displayMetrics)

        contexto.getSharedPreferences("app_prefs", Context.MODE_PRIVATE).edit {
            putString("idioma_seleccionado", codigoIdioma)
        }

        idiomaActual = codigoIdioma

        if (contexto is AppCompatActivity) {
            contexto.recreate()
        }
    }

    fun obtenerIdiomaActual(): String {
        return idiomaActual ?: Locale.getDefault().language
    }

    fun reiniciarIdioma() {
        idiomaActual = null
    }
}