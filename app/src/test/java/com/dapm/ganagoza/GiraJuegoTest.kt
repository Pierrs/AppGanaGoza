package com.dapm.ganagoza

import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class GiraJuegoTest {

    @Test
    fun testGirarBotella() {
        // Ejecutar
        val rotacion = girarBotella()

        // Verificar
        assertEquals(3600, rotacion.duration.toLong())
        assertTrue(rotacion.fillAfter)
        assertTrue(rotacion.interpolator is DecelerateInterpolator)
    }

    @Test
    fun testCalcularGrados() {
        // Ejecutar
        val grados = calcularGrados()

        // Verificar
        assertTrue(grados >= 1000 && grados <= 4600)
    }

    fun girarBotella(): RotateAnimation {
        val grados = calcularGrados()
        val rotacion = crearAnimacion(grados)
        return rotacion
    }

    fun calcularGrados(): Float {
        return (Math.random() * 3600 + 1000).toFloat()
    }

    fun crearAnimacion(grados: Float): RotateAnimation {
        val rotacion = RotateAnimation(0f, grados, Animation.RELATIVE_TO_SELF,
            0.5f, Animation.RELATIVE_TO_SELF, 0.5f)

        rotacion.fillAfter = true
        rotacion.duration = 3600
        rotacion.interpolator = DecelerateInterpolator()

        return rotacion
    }
}
