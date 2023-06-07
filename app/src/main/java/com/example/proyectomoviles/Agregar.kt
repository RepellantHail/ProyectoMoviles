package com.example.proyectomoviles

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.datepicker.MaterialDatePicker


class Agregar : Fragment() {
    lateinit var linearLayout: LinearLayout
    lateinit var textView: TextView

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
        btnCalendar = view.findViewById(R.id.btn_Calendar)
        textView = view.findViewById(R.id.textView)
        linearLayout = view.findViewById(R.id.linLayout)
        btnCalendar.setOnClickListener {
            dateRangePicker.show(parentFragmentManager, "datePickerTag")
        }
        textView.setOnCreateContextMenuListener(this)
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



}


