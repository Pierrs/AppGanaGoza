package com.dapm.ganagoza.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

object LanguageManager {

    const val SPANISH = "es"
    const val ENGLISH = "en"
    const val FRENCH = "fr"
    const val PORTUGUESE = "pt"

    fun setLocale(activity: AppCompatActivity, languageCode: String) {
        val sharedPref = activity.getSharedPreferences("language_pref", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("language", languageCode)
            apply()
        }

        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources: Resources = activity.resources
        val config: Configuration = resources.configuration

        config.setLocale(locale)

        activity.createConfigurationContext(config)

        resources.updateConfiguration(config, resources.displayMetrics)

        activity.recreate()
    }

    fun getLanguage(context: Context): String {
        val sharedPref = context.getSharedPreferences("language_pref", Context.MODE_PRIVATE)
        return sharedPref.getString("language", SPANISH) ?: SPANISH
    }
}