package com.dapm.ganagoza.utils

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

    fun establecerLocale(actividad: AppCompatActivity, codigoIdioma: String) {
        val locale = Locale(codigoIdioma)
        Locale.setDefault(locale)

        val recursos = actividad.resources
        val configuracion = Configuration(recursos.configuration)
        configuracion.setLocale(locale)

        actividad.baseContext.createConfigurationContext(configuracion)
        recursos.updateConfiguration(configuracion, recursos.displayMetrics)


        val preferencias = actividad.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        preferencias.edit() { putString("idioma_seleccionado", codigoIdioma) }

        actividad.recreate()
    }

    fun cargarIdiomaGuardado(contexto: Context): String {
        val preferencias = contexto.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return preferencias.getString("idioma_seleccionado", ESPANOL) ?: ESPANOL
    }
}