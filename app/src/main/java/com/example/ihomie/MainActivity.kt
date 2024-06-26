package com.example.ihomie

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.OrientationEventListener
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.DrawableCompat.applyTheme
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.ihomie.R.id
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.elevation.SurfaceColors


class MainActivity : AppCompatActivity(){
    private lateinit var browseFragment: Fragment
    private lateinit var savedHomesFragment: Fragment
    private lateinit var statisticsFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setSupportActionBar(findViewById(R.id.toolbar))
        window.navigationBarColor = SurfaceColors.SURFACE_2.getColor(this)

        setContentView(R.layout.activity_main)

        val supportFragmentManager = supportFragmentManager
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        // define your fragments here
        browseFragment = BrowseFragment()  // Replace fragment with main screen when implemented
        savedHomesFragment = SavedHomesFragment()
        statisticsFragment = StatisticsFragment()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // handle navigation selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_browse -> replaceFragment(browseFragment)
                R.id.action_saved -> replaceFragment(savedHomesFragment)
                R.id.action_statistics -> replaceFragment(statisticsFragment)
                R.id.action_account -> {
                    // Start the activity for the "action_account" item
                    startActivity(Intent(this, AccountActivity::class.java))
                    // Optionally, you can deselect the item to prevent it from staying selected
                    // after navigating to the activity
                    return@setOnItemSelectedListener true
                }
            }
            true
        }

        // Set default selection only if savedInstanceState is null
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.action_browse
        }

    }

    /*
     * Swap FrameLayout with the fragments when user navigate between screen in bottom nav bar
     */
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    override fun onResume() {
        super.onResume()
        applyTheme()
    }

    private fun applyTheme() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val themePreference = sharedPreferences.getString("theme_preference", "light_theme")

        when (themePreference) {
            "light_theme" -> setTheme(R.style.AppTheme_Light)
            "dark_theme" -> setTheme(R.style.AppTheme_Dark)
        }
    }

}