package com.example.padicareapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.padicareapp.R
import com.example.padicareapp.databinding.ActivityMainBinding
import com.example.padicareapp.view.detect.FragmentDetect
import com.example.padicareapp.view.history.FragmentHistory
import com.example.padicareapp.view.home.FragmentHome

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (savedInstanceState == null) {
            replaceFragment(FragmentHome())
        }


        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(FragmentHome())
                    true
                }
                R.id.navigation_detect -> {
                    replaceFragment(FragmentDetect())
                    true
                }
                R.id.navigation_history -> {
                    replaceFragment(FragmentHistory())
                    true
                }
                else -> false
            }
        }
    }


    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}