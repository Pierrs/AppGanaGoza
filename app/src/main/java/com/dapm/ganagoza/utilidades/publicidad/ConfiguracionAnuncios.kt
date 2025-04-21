package com.dapm.ganagoza.utilidades.publicidad

object ConfiguracionAnuncios {
    const val ES_MODO_PRUEBA = true
    val ID_BANNER: String = if (ES_MODO_PRUEBA) {
        "ca-app-pub-3940256099942544/6300978111"
    } else {
        "ca-app-pub-XXXXXXXXXXXXXXXX/YYYYYYYYYY"
    }
    val ID_INTERSTICIAL: String = if (ES_MODO_PRUEBA) {
        "ca-app-pub-3940256099942544/1033173712"
    } else {
        "ca-app-pub-XXXXXXXXXXXXXXXX/ZZZZZZZZZZ"
    }
}