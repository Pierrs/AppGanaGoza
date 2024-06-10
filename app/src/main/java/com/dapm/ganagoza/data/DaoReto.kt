package com.dapm.ganagoza.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dapm.ganagoza.model.Reto

@Dao
interface DaoReto {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun agregarReto(reto: Reto)

     @Query("SELECT *FROM Reto")
     fun obtenerListaReto():MutableList<Reto>

     @Update
     fun update(reto: Reto)

     @Delete
     fun delete(reto: Reto)
}