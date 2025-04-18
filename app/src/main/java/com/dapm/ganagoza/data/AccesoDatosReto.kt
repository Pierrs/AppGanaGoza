package com.dapm.ganagoza.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dapm.ganagoza.model.Reto


@Dao
interface AccesoDatosReto {


 @Insert(onConflict = OnConflictStrategy.REPLACE)
 fun agregarReto(reto: Reto)


 @Query("SELECT * FROM retos")
 fun obtenerTodosLosRetos(): MutableList<Reto>


 @Update
 fun actualizarReto(reto: Reto)


 @Delete
 fun eliminarReto(reto: Reto)
}
