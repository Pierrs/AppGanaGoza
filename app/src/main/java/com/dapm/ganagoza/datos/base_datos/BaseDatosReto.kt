package com.dapm.ganagoza.datos.base_datos
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dapm.ganagoza.datos.dao.AccesoDatosReto
import com.dapm.ganagoza.modelo.Reto
import com.dapm.ganagoza.utilidades.Constantes.NOMBRE_BASE_DE_DATOS

@Database(entities = [Reto::class], version = 1, exportSchema = false)
abstract class BaseDatosReto : RoomDatabase() {

    abstract fun accesoDatosReto(): AccesoDatosReto

    companion object {
        @Volatile
        private var INSTANCIA: BaseDatosReto? = null

        fun obtenerInstancia(context: Context): BaseDatosReto =
            INSTANCIA ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    BaseDatosReto::class.java,
                    NOMBRE_BASE_DE_DATOS
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCIA = it }
            }
    }
}
