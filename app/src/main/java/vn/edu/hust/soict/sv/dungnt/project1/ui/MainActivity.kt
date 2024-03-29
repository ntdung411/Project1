package vn.edu.hust.soict.sv.dungnt.project1.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import vn.edu.hust.soict.sv.dungnt.project1.R

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_device_list -> {
                    navController.navigate(R.id.deviceListFragment)
                    true
                }
                R.id.navigation_account -> {
                    navController.navigate(R.id.accountFragment)
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            navController.navigate(R.id.deviceListFragment)
        }
    }
}