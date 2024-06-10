package com.dapm.ganagoza.repository

import android.content.Context
import com.dapm.ganagoza.data.DBReto
import com.dapm.ganagoza.data.DaoReto
import com.dapm.ganagoza.model.Reto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetoRepository(val context: Context) {
    private val daoReto: DaoReto = DBReto.getDatabase(context).daoReto()

    suspend fun agregarReto(reto: Reto) {
        withContext(Dispatchers.IO) {
            daoReto.agregarReto(reto)
        }
    }

    suspend fun obtenerListaReto():MutableList<Reto>{
        return withContext(Dispatchers.IO){
            daoReto.obtenerListaReto()
        }
    }
    suspend fun eliminarReto(reto: Reto){
        withContext(Dispatchers.IO){
            daoReto.delete(reto)
        }
    }
    suspend fun actualizarReto(reto: Reto){
        withContext(Dispatchers.IO){
            daoReto.update(reto)
        }
    }
}