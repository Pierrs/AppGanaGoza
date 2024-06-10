package com.dapm.ganagoza.view.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dapm.ganagoza.databinding.ItemRetoBinding
import com.dapm.ganagoza.model.Reto
import com.dapm.ganagoza.view.viewmodel.JuegoViewModel
import com.dapm.ganagoza.viewholder.RetoViewHolder

class RetoAdapter(private val listaReto:MutableList<Reto>,private  val juegoViewModel: JuegoViewModel):RecyclerView.Adapter<RetoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RetoViewHolder {
        val binding = ItemRetoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RetoViewHolder(binding,juegoViewModel)

    }

    override fun getItemCount(): Int {
        return listaReto.size
    }

    override fun onBindViewHolder(retoViewHolder: RetoViewHolder, position: Int) {
        val reto = listaReto[position]
        retoViewHolder.setItemReto(reto)
    }
}