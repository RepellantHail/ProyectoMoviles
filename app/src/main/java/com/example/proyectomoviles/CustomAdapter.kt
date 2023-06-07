package com.example.proyectomoviles

import android.util.SparseBooleanArray
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.google.android.material.card.MaterialCardView


class CustomAdapter: RecyclerView.Adapter<CustomAdapter.ViewHolder?>() {

    private var showAllElements = true

    val titles = arrayOf("Cirujano",
        "Pediatra",
        "Cardiologo",
        "General")

    val details = arrayOf("Marcos",
        "Claudia",
        "Ximena",
        "Carlos")

    val imagenes = intArrayOf(R.drawable.doctormario,
        R.drawable.doctor1,
        R.drawable.doctor2,
        R.drawable.doctor3)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v:View = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout, viewGroup,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val cardLayout = viewHolder.itemView.findViewById<MaterialCardView>(R.id.card_view )

        if (showAllElements) {
            cardLayout.visibility = View.VISIBLE
        } else {
            cardLayout.visibility = View.GONE
        }

        viewHolder.itemTitle.text = titles[i]
        viewHolder.itemDetail.text = details[i]
        viewHolder.itemImage.setImageResource(imagenes[i])
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage: ImageView
        var itemTitle: TextView
        var itemDetail: TextView

        init {
            itemImage = itemView.findViewById(R.id.item_image)
            itemTitle = itemView.findViewById(R.id.item_title)
            itemDetail = itemView.findViewById(R.id.item_detalles)
        }
    }

    fun toggleAllElementsVisibility() {
        showAllElements = !showAllElements
        notifyDataSetChanged()
    }

}