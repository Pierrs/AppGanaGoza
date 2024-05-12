package com.dapm.ganagoza.utils

import java.util.Calendar
import java.util.Locale
import android.text.format.DateFormat

object Constantes {
    const val TIEMPO:Long=5000

    fun obtenerTiempoDispositivo():Long {
        return System.currentTimeMillis()
    }

    fun obtenerFecha(tiempo:Long):String{
        val calendario = Calendar.getInstance(Locale.ENGLISH)
        calendario.timeInMillis  =tiempo

        return DateFormat.format("dd/MM/yyyy",calendario).toString()
    }


}