package com.example.proyectomoviles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.datepicker.MaterialDatePicker


class Agregar : Fragment() {
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
        btnCalendar.setOnClickListener {
            dateRangePicker.show(parentFragmentManager, "datePickerTag")
        }

        return view
    }



}


