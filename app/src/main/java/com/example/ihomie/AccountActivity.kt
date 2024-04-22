package com.example.ihomie

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat.applyTheme
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class AccountActivity : AppCompatActivity(),
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback,
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Retrieve the selected theme preference and apply it
        applyTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.account_container, AccountFragment())
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the shared preference change listener
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
        if (key == "theme_preference") {
            applyTheme()
        }
    }

    private fun applyTheme() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val themePreference = sharedPreferences.getString("theme_preference", "light_theme") // Default to light theme if not found

        // Log the retrieved theme preference
        Log.d("Theme", "Selected theme preference: $themePreference")

        when (themePreference) {
            "light_theme" -> {
                Log.d("Theme", "Applying light theme")
                setTheme(R.style.AppTheme_Light)
            }
            "dark_theme" -> {
                Log.d("Theme", "Applying dark theme")
                setTheme(R.style.AppTheme_Dark)
            }
        }
    }
}
