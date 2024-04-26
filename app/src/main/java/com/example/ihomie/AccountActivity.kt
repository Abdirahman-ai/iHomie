package com.example.ihomie

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth

class AccountActivity : AppCompatActivity(),
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback,
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var signOutButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        // Retrieve the selected theme preference and apply it

        super.onCreate(savedInstanceState)
        applyTheme()
        setContentView(R.layout.activity_account)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.account_container, AccountFragment())
            .commit()

        // Sign-out button
        signOutButton = findViewById(R.id.signOutButton)
        signOutButton.setOnClickListener {
            signOut();
        }

    }

    private fun signOut()
    {
        FirebaseAuth.getInstance().signOut()
        // Navigate to the login pagee
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the shared preference change listener
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
    }
    override fun onResume() {
        super.onResume()
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
    }


    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat, pref: Preference): Boolean {
        // Instantiate the new Fragment.
        val args = pref.extras
        val fragment = supportFragmentManager.fragmentFactory.instantiate(classLoader,
            pref.fragment!!
        )
        fragment.arguments = args
        fragment.setTargetFragment(caller, 0)
        // Replace the existing Fragment with the new Fragment.
        supportFragmentManager.beginTransaction()
            .replace(R.id.account_container, fragment)
            .addToBackStack(null)
            .commit()
        return true
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        // Handle theme preference change
        if (key == "theme_preference")
        {
            applyTheme()
            recreate()
        }
    }

    private fun applyTheme()
    {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val themePreference = sharedPreferences.getString("theme_preference", "light_theme")

        when (themePreference) {
            "light_theme" -> setTheme(R.style.AppTheme_Light)
            "dark_theme" -> setTheme(R.style.AppTheme_Dark)
        }

    }
}

