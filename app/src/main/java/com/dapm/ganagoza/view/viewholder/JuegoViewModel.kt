package com.dapm.ganagoza.view.viewholder

import android.app.Activity
import android.content.Intent
import android.os.Handler
import androidx.lifecycle.ViewModel
import com.dapm.ganagoza.utils.Constantes.TIEMPO
import com.dapm.ganagoza.view.MainActivity
import com.dapm.ganagoza.view.OpcionesRegistro

class JuegoViewModel:ViewModel() {
     fun pantallaPresentacion(activity: Activity){
        val manejador = Handler()
        manejador.postDelayed({
            activity.startActivity(Intent(activity, OpcionesRegistro::class.java))
            activity.finish()
        },TIEMPO)
    }
}