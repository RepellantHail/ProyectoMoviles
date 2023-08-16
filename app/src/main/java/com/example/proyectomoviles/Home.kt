package com.example.proyectomoviles

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class Home : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var firestore: FirebaseFirestore
    private lateinit var toggleAllButton: Button
    private lateinit var adapter : CustomAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        firestore = FirebaseFirestore.getInstance()
         adapter = CustomAdapter(firestore)
        toggleAllButton = view.findViewById<Button>(R.id.toggleButton)

        toggleAllButton.setOnClickListener {
            toggleAllElements(it)
        }

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        // Other view setup or initialization if needed


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireContext()) // Replace with your desired layout manager (e.g., GridLayoutManager)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        registerForContextMenu(recyclerView)
        // Set any necessary layout manager, item decorations, or other configurations
    }

    fun toggleAllElements(view: View) {
        adapter.toggleAllElementsVisibility()
    }

}