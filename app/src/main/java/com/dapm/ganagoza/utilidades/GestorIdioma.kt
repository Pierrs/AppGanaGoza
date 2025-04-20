package com.dapm.ganagoza.utilidades

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale
import androidx.core.content.edit

object GestorIdioma {
    const val ESPANOL = "es"
    const val INGLES = "en"
    const val FRANCES = "fr"
    const val PORTUGUES = "pt"
    private var idiomaActual: String? = null
    fun establecerLocale(actividad: AppCompatActivity, codigoIdioma: String) {
        val locale = Locale(codigoIdioma)
        Locale.setDefault(locale)

        val recursos = actividad.resources
        val configuracion = Configuration(recursos.configuration)
        configuracion.setLocale(locale)

        actividad.createConfigurationContext(configuracion)

        val preferencias = actividad.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        preferencias.edit { putString("idioma_seleccionado", codigoIdioma) }

        idiomaActual = codigoIdioma

        actividad.recreate()
    }

    fun obtenerIdiomaActual(): String {
        if (idiomaActual != null) {
            return idiomaActual!!
        }
        return Locale.getDefault().language
    }
    fun reiniciarIdioma() {
        idiomaActual = null
    }
}