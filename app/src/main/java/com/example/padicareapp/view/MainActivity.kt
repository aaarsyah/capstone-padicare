package com.example.padicareapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.padicareapp.R
import com.example.padicareapp.view.detect.FragmentDetect
import com.example.padicareapp.view.history.FragmentHistory
import com.example.padicareapp.view.home.FragmentHome
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadFragment(FragmentHome())

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    loadFragment(FragmentHome())
                    true
                }
                R.id.navigation_detect -> {
                    loadFragment(FragmentDetect())
                    true
                }
                R.id.navigation_history -> {
                    loadFragment(FragmentHistory())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}