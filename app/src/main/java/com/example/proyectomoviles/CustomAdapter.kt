package com.example.proyectomoviles

import android.Manifest
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.util.SparseBooleanArray
import com.example.proyectomoviles.Doctor
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage


class CustomAdapter( private val firestore: FirebaseFirestore): RecyclerView.Adapter<CustomAdapter.ViewHolder?>() {

    private var showAllElements = true
    private lateinit var database: DatabaseReference
    private val doctorsList = ArrayList<Doctor>()

    init {
        val permanentDoctors = listOf(
            Doctor("Marcos", "Cirujano", R.drawable.doctormario),
            Doctor("Claudia", "Pediatra", R.drawable.doctor1),
            Doctor("Ximena", "Cardiologo", R.drawable.doctor2),
            Doctor("Carlos", "General", R.drawable.doctor3)
        )

        database = Firebase.database.reference

        // Obtener los doctores desde la base de datos
         getDoctorsFromDatabase()

        // Combinar los doctores permanentes con los de la base de datos
        doctorsList.addAll(permanentDoctors)
    }

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

        val doctor = doctorsList[i]

        viewHolder.itemTitle.text = doctor.name
        viewHolder.itemDetail.text = doctor.specialty
        viewHolder.itemImage.setImageResource(doctor.imageResourceId)
    }

    override fun getItemCount(): Int {
        return doctorsList.size
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

    fun getDoctorsFromDatabase() {
        // Obtén una referencia a la colección "doctores" en la base de datos en tiempo real
        val doctorsReference = database.child("doctores")

        // Agregar un listener para obtener los datos de la base de datos en tiempo real
        doctorsReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Limpiar la lista actual de doctores
                doctorsList.clear()
                val permanentDoctors = listOf(
                    Doctor("Marcos", "Cirujano", R.drawable.doctormario),
                    Doctor("Claudia", "Pediatra", R.drawable.doctor1),
                    Doctor("Ximena", "Cardiologo", R.drawable.doctor2),
                    Doctor("Carlos", "General", R.drawable.doctor3)
                )
                doctorsList.addAll(permanentDoctors)

                // Recorrer los datos de la base de datos y agregar los nuevos doctores
                for (doctorSnapshot in snapshot.children) {
                    val name = doctorSnapshot.child("name").getValue(String::class.java) ?: ""
                    val specialty = doctorSnapshot.child("specialty").getValue(String::class.java) ?: ""
                    val imageName = doctorSnapshot.child("picture").getValue(String::class.java) ?: ""
                    val imageResourceId = getDoctorImageResource(imageName)
                    // Construir el objeto Doctor y agregarlo a la lista
                    val doctor = Doctor(name, specialty,imageResourceId) // Puedes cargar la imagen adecuada
                    doctorsList.add(doctor)
                }
                // Notificar al adaptador que los datos han cambiado
                notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error en caso de que ocurra
                Log.e(TAG, "Error al obtener datos de la base de datos: ${error.message}")
            }
        })
    }

    private fun getDoctorImageResource(imageName: String): Int {
        // Obtener el ID del recurso de la imagen según el nombre
        return when (imageName) {
            "doctor1" -> R.drawable.doctor1
            "doctor2" -> R.drawable.doctor2
            "doctor3" -> R.drawable.doctor3
            "doctor4" -> R.drawable.doctor4
            "doctor5" -> R.drawable.doctor5
            "doctor6" -> R.drawable.doctor6
            // Agrega más casos para otros nombres de imágenes
            else -> R.drawable.doctormario // Imagen por defecto en caso de que no encuentre la imagen
        }
    }



}