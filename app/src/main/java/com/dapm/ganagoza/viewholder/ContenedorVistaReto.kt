package com.dapm.ganagoza.viewholder
import androidx.recyclerview.widget.RecyclerView
import com.dapm.ganagoza.databinding.ItemRetoBinding
import com.dapm.ganagoza.model.Reto
import com.dapm.ganagoza.view.dialogo.DialogoEditarReto
import com.dapm.ganagoza.view.dialogo.DialogoEliminarReto.mostrarDialogoEliminarReto
import com.dapm.ganagoza.view.viewmodel.VistaModeloJuego

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
