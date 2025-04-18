package com.dapm.ganagoza.viewholder
import androidx.recyclerview.widget.RecyclerView
import com.dapm.ganagoza.databinding.ItemRetoBinding
import com.dapm.ganagoza.model.Reto
import com.dapm.ganagoza.view.dialogo.DialogoAgregarReto
import com.dapm.ganagoza.view.dialogo.DialogoEditarReto
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
    private fun onEliminarClick(reto: Reto) {
        val dialogo = DialogoAgregarReto(binding.root.context, vistaModeloJuego, reto)
        dialogo.show()
    }
    private fun onEditarClick(reto: Reto) {
        DialogoEditarReto.showDialogEditReto(binding.root.context, vistaModeloJuego, reto) {
            vistaModeloJuego.obtenerTodosLosRetos()
        }
    }
}
