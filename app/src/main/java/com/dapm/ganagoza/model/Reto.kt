package com.dapm.ganagoza.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Reto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val descripcionReto: String
)