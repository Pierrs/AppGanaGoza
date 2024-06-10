package com.dapm.ganagoza
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.junit.Assert.assertTrue
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class GiroAgregarRetoTest {

    @Test
    fun testGirarBotella() {
        // Ejecutar
        val resultado = girarBotella()

        // Verificar la animación
        val rotacion = resultado.first
        assertEquals(3600, rotacion.duration.toLong())
        assertTrue(rotacion.fillAfter)
        assertTrue(rotacion.interpolator is DecelerateInterpolator)

        // Verificar el reto
        val reto = resultado.second
        assertTrue(reto in listaDeRetos)
    }

    @Test
    fun testCalcularGrados() {
        // Ejecutar
        val grados = calcularGrados()

        // Verificar
        assertTrue(grados >= 1000 && grados <= 4600)
    }

    private val listaDeRetos = listOf("Canta una canción", "Haz 10 saltos", "Imita un animal", "Cuenta un chiste")

    fun girarBotella(): Pair<RotateAnimation, String> {
        val grados = calcularGrados()
        val rotacion = crearAnimacion(grados)
        val reto = elegirReto()
        return Pair(rotacion, reto)
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

    fun elegirReto(): String {
        return listaDeRetos.random()
    }
}
