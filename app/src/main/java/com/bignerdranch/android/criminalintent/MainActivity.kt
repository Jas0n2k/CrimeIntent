package com.bignerdranch.android.criminalintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController

//private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appToolBar: Toolbar
    private lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
//        if (currentFragment == null) {
//            val fragment = CrimeListFragment()
//            supportFragmentManager
//                .beginTransaction()
//                .add(R.id.fragment_container, fragment)
//                .commit()
//        }
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        appToolBar = findViewById(R.id.app_toolbar)
        appToolBar.setupWithNavController(navController, appBarConfiguration)
    }

//    override fun onCrimeSelected(crimeId: UUID) {
//        val fragment = CrimeFragment.newInstance(crimeId)
//        supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.fragment_container, fragment)
//            .addToBackStack(null)
//            .commit()
//
//        Log.d(TAG, "onCrimeSelected: $crimeId")
//    }
}