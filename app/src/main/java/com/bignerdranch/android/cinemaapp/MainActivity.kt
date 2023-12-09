package com.bignerdranch.android.cinemaapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bignerdranch.android.cinemaapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // установка селектора для цвета иконок и текста при нажатии
        binding.navView.itemIconTintList = resources.getColorStateList(R.drawable.bottom_nav_item_color)
        binding.navView.itemTextColor = resources.getColorStateList(R.drawable.bottom_nav_item_color)

        binding.navView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    switchFragment(NavigationHomeFragment())
                    true
                }
                R.id.navigation_collection -> {
                    switchFragment(NavigationCollectionsFragment())
                    true
                }
                R.id.navigation_set -> {
                    switchFragment(NavigationSetFragment())
                    true
                }
                R.id.navigation_profile -> {
                    switchFragment(NavigationProfileScreenFragment())
                    true
                }
                else -> false
            }
        }

        // восстановление состояния
        savedInstanceState?.let {
            val selectedItemId = it.getInt("SELECTED_ITEM_ID", R.id.navigation_home)
            binding.navView.selectedItemId = selectedItemId
            switchFragment(getFragmentForItemId(selectedItemId))
        } ?: run {
            switchFragment(NavigationHomeFragment())
        }
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }

    private fun getFragmentForItemId(itemId: Int): Fragment {
        return when (itemId) {
            R.id.navigation_home -> NavigationHomeFragment()
            R.id.navigation_collection -> NavigationCollectionsFragment()
            R.id.navigation_set -> NavigationSetFragment()
            R.id.navigation_profile -> NavigationProfileScreenFragment()
            else -> NavigationHomeFragment()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("SELECTED_ITEM_ID", binding.navView.selectedItemId)
    }
}
