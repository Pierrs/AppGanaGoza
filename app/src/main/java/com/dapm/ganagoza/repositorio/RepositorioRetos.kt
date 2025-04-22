package com.dapm.ganagoza.repositorio

import android.content.Context
import com.dapm.ganagoza.datos.base_datos.BaseDatosReto
import com.dapm.ganagoza.datos.dao.AccesoDatosReto
import com.dapm.ganagoza.modelo.Reto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositorioRetos(context: Context) {

    private val accesoDatos: AccesoDatosReto = BaseDatosReto.obtenerInstancia(context).accesoDatosReto()

    suspend fun agregarReto(reto: Reto) {
        withContext(Dispatchers.IO) {
            accesoDatos.agregarReto(reto)
        }
    }
    suspend fun actualizarReto(reto: Reto) {
        withContext(Dispatchers.IO) {
            accesoDatos.actualizarReto(reto)
        }
    }
    suspend fun eliminarReto(reto: Reto) {
        withContext(Dispatchers.IO) {
            accesoDatos.eliminarReto(reto)
        }
    }
    suspend fun obtenerTodosLosRetos(): MutableList<Reto> {
        return withContext(Dispatchers.IO) {
            accesoDatos.obtenerTodosLosRetos()
        }
    }


}
