package com.dapm.ganagoza.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dapm.ganagoza.model.Reto
import com.dapm.ganagoza.utils.Constantes.NAME_BD

@Database(entities = [Reto::class], version = 1, exportSchema = false)
abstract class DBReto : RoomDatabase() {
    abstract fun daoReto(): DaoReto

    companion object {
        @Volatile
        private var INSTANCE: DBReto? = null

        fun getDatabase(context: Context): DBReto {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DBReto::class.java,
                    NAME_BD
                )
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}