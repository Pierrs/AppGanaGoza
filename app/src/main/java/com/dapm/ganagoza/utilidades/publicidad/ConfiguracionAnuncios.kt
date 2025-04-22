package com.dapm.ganagoza.utilidades.publicidad

object ConfiguracionAnuncios {
    const val ES_MODO_PRUEBA = true
    val ID_BANNER: String = if (ES_MODO_PRUEBA) {
        "ca-app-pub-3940256099942544/6300978111"
    } else {
        "ca-app-pub-5883634517523480/3097454511"
    }
    val ID_INTERSTICIAL: String = if (ES_MODO_PRUEBA) {
        "ca-app-pub-3940256099942544/1033173712"
    } else {
        "ca-app-pub-5883634517523480/8158209503"
    }
}