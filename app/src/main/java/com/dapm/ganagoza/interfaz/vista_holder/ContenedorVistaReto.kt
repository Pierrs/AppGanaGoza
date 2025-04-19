package com.dapm.ganagoza.interfaz.vista_holder
import androidx.recyclerview.widget.RecyclerView
import com.dapm.ganagoza.databinding.ItemRetoBinding
import com.dapm.ganagoza.modelo.Reto
import com.dapm.ganagoza.interfaz.dialogo.DialogoEditarReto
import com.dapm.ganagoza.interfaz.dialogo.DialogoEliminarReto.mostrarDialogoEliminarReto
import com.dapm.ganagoza.viewmodel.VistaModeloJuego

class ContenedorVistaReto(
    private val binding: ItemRetoBinding,
    private val vistaModeloJuego: VistaModeloJuego
) : RecyclerView.ViewHolder(binding.root) {

    fun asignarReto(reto: Reto) {
        binding.tvDetalleReto.text = reto.descripcionReto
        binding.ivEditar.setOnClickListener { onEditarClick(reto) }
        binding.ivEliminar.setOnClickListener { onEliminarClick(reto) }
    }
    private fun onEditarClick(reto: Reto) {
        DialogoEditarReto.mostrarDialogoEditarReto(binding.root.context, vistaModeloJuego, reto) {
            vistaModeloJuego.obtenerTodosLosRetos()
        }
    }
    private fun onEliminarClick(reto: Reto) {
        mostrarDialogoEliminarReto(
            binding.root.context,
            vistaModeloJuego,
            reto
        ) {
            vistaModeloJuego.obtenerTodosLosRetos()
        }
    }
}
