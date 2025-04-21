package com.dapm.ganagoza.utilidades.publicidad

object ConfiguracionAnuncios {
    // Modo de prueba (true durante desarrollo, false para producción)
    const val ES_MODO_PRUEBA = true

    const val ID_APP = "ca-app-pub-3940256099942544~3347511713"

    // ID para Banner
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

    const val INTERVALO_MINIMO_SEGUNDOS = 60

    // Eventos que pueden mostrar anuncios
    const val EVENTO_AGREGAR_RETO = "agregar_reto"
    const val EVENTO_SELECCIONAR_OPCION = "seleccionar_opcion"
    const val EVENTO_CERRAR_RETO = "cerrar_reto"
    const val EVENTO_CAMBIAR_IDIOMA = "cambiar_idioma"
    const val EVENTO_GIRAR_BOTELLA = "girar_botella"
}