package com.example.proyectomoviles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



class Home : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var miAdapter: CustomAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        // Other view setup or initialization if needed
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireContext()) // Replace with your desired layout manager (e.g., GridLayoutManager)
        recyclerView.layoutManager = layoutManager
        val adapter = CustomAdapter()
        recyclerView.adapter = adapter
        // Set any necessary layout manager, item decorations, or other configurations
    }

}