package com.dapm.ganagoza.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "retos")
data class Reto(
    @PrimaryKey(autoGenerate = true)
    val retoId: Int = 0,
    val descripcionReto: String
)
