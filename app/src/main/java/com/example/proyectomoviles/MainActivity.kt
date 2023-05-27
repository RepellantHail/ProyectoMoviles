package com.example.proyectomoviles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var homeFragment : Home
    lateinit var agregarFragment: Agregar
    lateinit var citasFragment: Citas

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        homeFragment = Home()
        agregarFragment = Agregar()
        citasFragment = Citas()

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        supportFragmentManager.beginTransaction().replace(R.id.container,homeFragment).commit()
        bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container,homeFragment).commit()
                    true
                }
                R.id.agregar -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container,agregarFragment).commit()
                    true
                }
                R.id.cita -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container,citasFragment).commit()
                    true
                }
                else -> false
            }
        }
    }
}

