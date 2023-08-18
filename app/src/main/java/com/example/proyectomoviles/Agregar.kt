package com.example.proyectomoviles

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.UUID
import kotlin.random.Random


class Agregar : Fragment() {

    private companion object {
        const val notificationId = 1 // Puedes usar cualquier valor único para identificar la notificación
    }

    lateinit var linearLayout: LinearLayout
    lateinit var textView: TextView


    //CAmpos para agregar
    private lateinit var editNombre: EditText
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var editEspecialidad: Spinner
    private lateinit var editImagen: EditText
    private lateinit var imageSpinner: Spinner
    private lateinit var profileImageView: ImageView

    private lateinit var database: DatabaseReference

    private lateinit var btnGuardar: Button
    private lateinit var btnCalendar: Button
    val dateRangePicker =
        MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select dates")
            .setSelection(
                androidx.core.util.Pair(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            )
            .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_agregar, container, false)

        imageSpinner = view.findViewById(R.id.image_spinner)
        profileImageView = view.findViewById(R.id.profile_image_view)

        val imageNames = arrayOf("doctor1", "doctor2", "doctor3","doctor4","doctor5","doctor6") // Nombres de las imágenes en drawable
        val adapterImageView = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, imageNames)
        adapterImageView.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        imageSpinner.adapter = adapterImageView

        imageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedImageName = imageNames[position]
                val resourceId = resources.getIdentifier(selectedImageName, "drawable", requireContext().packageName)
                profileImageView.setImageResource(resourceId)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        database = Firebase.database.reference

        editNombre = view.findViewById(R.id.nombre_edit_text)
        editEspecialidad = view.findViewById(R.id.spinner_especialidad)
        btnGuardar = view.findViewById(R.id.btn_Save)
        btnCalendar = view.findViewById(R.id.btn_Calendar)
        textView = view.findViewById(R.id.textView)
        linearLayout = view.findViewById(R.id.linLayout)
        btnCalendar.setOnClickListener {
            dateRangePicker.show(parentFragmentManager, "datePickerTag")
        }
        textView.setOnCreateContextMenuListener(this)



        val especialidades = listOf("General", "Cardiologo", "Pediatra","Cirujano","Ornitolaringologo","Dentista")
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
        adapter.addAll(especialidades)
        editEspecialidad.adapter = adapter

        btnGuardar.setOnClickListener{
            val nombre = editNombre.text.toString()
            val especialidad = editEspecialidad.selectedItem.toString()
            val profilePicture = imageSpinner.selectedItem.toString()
            val id = UUID.randomUUID()

            val user = mapOf(
                "id" to id.toString(),
                "name" to nombre,
                "specialty" to especialidad,
                "picture" to profilePicture
            )

            database.child("doctores").child(id.toString()).setValue(user)
                .addOnSuccessListener {
                // Datos guardados exitosamente
                    Toast.makeText(requireContext(), "Datos guardados exitosamente", Toast.LENGTH_SHORT).show()
                    showNotification("Nuevo Doctor Agregado", "Se ha agregado un nuevo doctor: $nombre - $especialidad")
                }
                .addOnFailureListener { e ->
                    // Error al guardar los datos
                    Toast.makeText(requireContext(), "Error al guardar datos", Toast.LENGTH_SHORT).show()
                    println("Datos de usuario no guardados")
                }



        }

        return view
    }



    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        // You can set the menu header with title, icon, etc.
        menu.setHeaderTitle("Choose a color")
        // Add menu items
        menu.add(0, v.id, 0, "Yellow")
        menu.add(0, v.id, 0, "Gray")
        menu.add(0, v.id, 0, "Cyan")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.title) {
            "Yellow" -> linearLayout.setBackgroundColor(Color.YELLOW)
            "Gray" -> linearLayout.setBackgroundColor(Color.GRAY)
            "Cyan" -> linearLayout.setBackgroundColor(Color.CYAN)
        }
        return true
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "mi_canal_id"
        val notificationId = Random.nextInt()

        val notificationBuilder = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.doctormario) // Icono de la notificación
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.color = ContextCompat.getColor(requireContext(), R.color.black)
        }

        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notificationBuilder.build())
    }


}


