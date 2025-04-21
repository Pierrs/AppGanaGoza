package com.dapm.ganagoza.adaptador

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dapm.ganagoza.databinding.ItemRetoBinding
import com.dapm.ganagoza.modelo.Reto
import com.dapm.ganagoza.viewmodel.VistaModeloJuego
import com.dapm.ganagoza.interfaz.vistaHolder.ContenedorVistaReto

class AdaptadorDeRetos(
    private val todosLosRetos: List<Reto>,
    private val vistaModeloJuego: VistaModeloJuego
) : RecyclerView.Adapter<ContenedorVistaReto>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContenedorVistaReto {
        val binding = ItemRetoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContenedorVistaReto(binding, vistaModeloJuego)
    }
    override fun getItemCount(): Int = todosLosRetos.size
    override fun onBindViewHolder(contenedor: ContenedorVistaReto, position: Int) {
        val reto = todosLosRetos[position]
        contenedor.asignarReto(reto)
    }
}
