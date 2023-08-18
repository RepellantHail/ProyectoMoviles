package com.example.proyectomoviles

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import android.util.Log
import android.util.SparseArray
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.Constants.PERMISSION_REQUEST_CAMERA
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class CustomAdapter( private val firestore: FirebaseFirestore): RecyclerView.Adapter<CustomAdapter.ViewHolder?>() {

    private var showAllElements = true
    private lateinit var database: DatabaseReference
    private val doctorsList = ArrayList<Doctor>()
    public var capturedImageBitmap: Bitmap? = null

    init {
        val permanentDoctors = listOf(
            Doctor("id1","Marcos", "Cirujano", R.drawable.doctormario),
            Doctor("id2","Claudia", "Pediatra", R.drawable.doctor1),
            Doctor("id3","Ximena", "Cardiologo", R.drawable.doctor2),
            Doctor("id4","Carlos", "General", R.drawable.doctor3)
        )

        database = Firebase.database.reference

        // Obtener los doctores desde la base de datos
        getDoctorsFromDatabase()

        // Combinar los doctores permanentes con los de la base de datos
        doctorsList.addAll(permanentDoctors)
    }

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

        if (capturedImageBitmap != null) {
            viewHolder.itemImage.setImageDrawable(CustomAdapter.tempDrawables[doctor.imageResourceId])
        } else {
            viewHolder.itemImage.setImageResource(doctor.imageResourceId)
        }

        viewHolder.itemView.setOnLongClickListener {
            val popupMenu = PopupMenu(viewHolder.itemView.context, it)
            popupMenu.menuInflater.inflate(R.menu.context_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.context_menu_edit -> {
                        if (ActivityCompat.checkSelfPermission(
                                viewHolder.itemView.context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED) {
                            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            if (takePictureIntent.resolveActivity(viewHolder.itemView.context.packageManager) != null) {
                                // Start the camera intent to capture an image
                                (viewHolder.itemView.context as Activity).startActivityForResult(takePictureIntent, i)
                            }
                        } else {
                            ActivityCompat.requestPermissions(
                                viewHolder.itemView.context as Activity,
                                arrayOf(Manifest.permission.CAMERA),
                                PERMISSION_REQUEST_CAMERA
                            )
                        }
                        true
                    }
                    R.id.context_menu_delete -> {
                        // Lógica para la opción "Eliminar"
                        val doctor = doctorsList[i]
                        val doctorReference = database.child("doctores").child(doctor.id)
                        doctorReference.removeValue()
                        doctorsList.removeAt(i)
                        notifyDataSetChanged()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
            true
        }

    }
    override fun getItemId(position: Int): Long {
        return doctorsList[position].hashCode().toLong() // Override getItemId to return a unique identifier
    }
    fun getItem(position: Int): Doctor {
        return doctorsList[position]
    }

    override fun getItemCount(): Int {
        return doctorsList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
        var itemImage: ImageView
        var itemTitle: TextView
        var itemDetail: TextView

        init {
            itemImage = itemView.findViewById(R.id.item_image)
            itemTitle = itemView.findViewById(R.id.item_title)
            itemDetail = itemView.findViewById(R.id.item_detalles)
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(contextMenu: ContextMenu?, view: View?, contextMenuInfo: ContextMenu.ContextMenuInfo?) {
            contextMenu?.apply {
                setHeaderTitle("Options")
                add(0, R.id.context_menu_edit, 0, "Edit")
                add(0, R.id.context_menu_delete, 0, "Delete")
            }
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
                    Doctor("id1","Marcos", "Cirujano", R.drawable.doctormario),
                    Doctor("id2","Claudia", "Pediatra", R.drawable.doctor1),
                    Doctor("id3","Ximena", "Cardiologo", R.drawable.doctor2),
                    Doctor("id4","Carlos", "General", R.drawable.doctor3)
                )
                doctorsList.addAll(permanentDoctors)

                // Recorrer los datos de la base de datos y agregar los nuevos doctores
                for (doctorSnapshot in snapshot.children) {
                    val id = doctorSnapshot.child("id").getValue(String::class.java) ?: ""
                    val name = doctorSnapshot.child("name").getValue(String::class.java) ?: ""
                    val specialty = doctorSnapshot.child("specialty").getValue(String::class.java) ?: ""
                    val imageName = doctorSnapshot.child("picture").getValue(String::class.java) ?: ""
                    val imageResourceId = getDoctorImageResource(imageName)
                    // Construir el objeto Doctor y agregarlo a la lista
                    val doctor = Doctor(id,name, specialty,imageResourceId) // Puedes cargar la imagen adecuada
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

    fun updateDoctorImage(position: Int, imageBitmap: Bitmap) {
        if (position >= 0 && position < doctorsList.size) {
            val doctor = doctorsList[position]
            val updatedDoctor = Doctor(doctor.id, doctor.name, doctor.specialty, doctor.imageResourceId)
            updatedDoctor.imageResourceId = getTemporaryResourceId(imageBitmap)
            doctorsList[position] = updatedDoctor
            capturedImageBitmap = null // Clear the captured image after updating
            notifyDataSetChanged()
        }
    }

    private fun getTemporaryResourceId(imageBitmap: Bitmap): Int {
        // Create a temporary resource ID for the newly captured image
        // Convert the Bitmap to a Drawable and set it to the ImageView to generate a temporary resource ID
        val drawable = BitmapDrawable(imageBitmap)
        val tempResourceId = View.generateViewId()
        // Associate the Drawable with the temporary resource ID (this is just a demonstration)
        // In practice, you might want to cache the Bitmap for later use
        tempDrawables[tempResourceId] = drawable
        return tempResourceId
    }

    companion object {
        private val tempDrawables = SparseArray<Drawable>()
    }




}