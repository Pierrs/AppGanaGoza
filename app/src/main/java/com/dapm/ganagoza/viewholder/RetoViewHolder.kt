package com.dapm.ganagoza.viewholder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dapm.ganagoza.databinding.ItemRetoBinding
import com.dapm.ganagoza.model.Reto
import com.dapm.ganagoza.view.dialogo.DialogoAgregarReto
import com.dapm.ganagoza.view.dialogo.DialogoEditarReto.showDialogEditReto
import com.dapm.ganagoza.view.viewmodel.VistaModeloJuego

class RetoViewHolder(private val binding: ItemRetoBinding, vistaModeloJuego: VistaModeloJuego):ViewHolder(binding.root) {
    private val viewModel= vistaModeloJuego
    fun setItemReto(reto: Reto) {
        binding.tvName.text=reto.descripcionReto
        binding.ivDelete.setOnClickListener {
            val dialogo= DialogoAgregarReto(binding.root.context,viewModel,reto)
            dialogo.show()
        }
        binding.ivEdit.setOnClickListener {
            showDialogEditReto(binding.root.context,viewModel,reto){
                viewModel.obtenerTodosLosRetos()
            }
        }
    }
}