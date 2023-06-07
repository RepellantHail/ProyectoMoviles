package com.example.proyectomoviles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adaptador_Citas constructor(var nombrePaciente: List<String>,var nombreDoctor: List<String>,var fecha:List<String> ,var layout:Int,var itemListener:OnItemClickListener):RecyclerView.Adapter <Adaptador_Citas.ViewHolder?>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        val vh = ViewHolder(view)
        return vh
    }

    override fun getItemCount(): Int {
        return nombrePaciente.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(nombrePaciente.get(position),nombreDoctor.get(position),fecha.get(position),itemListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var lblCitas: TextView = itemView.findViewById<View>(R.id.lblCitas)as TextView
        var lblNombrePaciente: TextView = itemView.findViewById<View>(R.id.lblNombrePaciente)as TextView
        var lblNombreDoctor: TextView = itemView.findViewById<View>(R.id.lblNombreDoctor)as TextView
        var lblFecha: TextView = itemView.findViewById<View>(R.id.lblFecha)as TextView

        var txtNombrePaciente: TextView = itemView.findViewById<View>(R.id.txtNombrePaciente)as TextView
        var txtNombreDoctor: TextView = itemView.findViewById<View>(R.id.txtNombreDoctor)as TextView
        var txtFecha: TextView = itemView.findViewById<View>(R.id.txtFecha)as TextView


        fun bind(nombrePaciente: String?,nombreDoctor:String?, fecha: String?,itemListener:OnItemClickListener) {
            lblCitas.text = "Citas Medicas"
            lblNombrePaciente.text = "Nombre Paciente:"
            lblNombreDoctor.text = "Nombre Doctor:"
            lblFecha.text = "Fecha:"
            txtNombrePaciente.text = nombrePaciente
            txtNombreDoctor.text = nombreDoctor
            txtFecha.text = fecha
            itemView.setOnClickListener {
                itemListener.onItemClick(nombrePaciente,nombreDoctor,fecha,absoluteAdapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(nombrePaciente: String?, nombreDoctor: String?,fecha: String?,position: Int)
    }


}