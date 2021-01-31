package com.raystatic.iconfinder.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.raystatic.iconfinder.R
import com.raystatic.iconfinder.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragmentMain) as NavHostFragment

        binding.bottomNavigationView.setupWithNavController(navHostFragment.findNavController())
        binding.bottomNavigationView.setOnNavigationItemReselectedListener {
            /* NO OP*/
        }

    }
}