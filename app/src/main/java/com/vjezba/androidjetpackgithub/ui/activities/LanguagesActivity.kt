package com.vjezba.androidjetpackgithub.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.leinardi.android.speeddial.SpeedDialView
import com.vjezba.androidjetpackgithub.R
import com.vjezba.androidjetpackgithub.ui.fragments.HomeViewPagerFragmentDirections
import com.vjezba.androidjetpackgithub.viewmodels.LanguagesActivityViewModel
import com.vjezba.domain.repository.UserManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_languages.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class LanguagesActivity : AppCompatActivity() {

    @Inject
    lateinit var userManager: UserManager

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_languages)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val drawerToggle = ActionBarDrawerToggle(
            this, drawer_layout,
            R.string.open,
            R.string.close
        )
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.view_pager_fragment,
                R.id.paggin_with_network_and_db
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()

        val headerView = nav_view.getHeaderView(0)
        val navUsername = headerView.findViewById(R.id.tvNameOfUser) as TextView
        navUsername.text = "Welcome: ".plus(userManager.getUserName())

        setupSpeedDialView()
        logoutUser()
    }

    private fun logoutUser() {
        val logout = nav_view?.getHeaderView(0)?.findViewById<ImageView>(R.id.ivLogout)
        logout?.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val languagesActivityViewModel: LanguagesActivityViewModel by viewModels()
                languagesActivityViewModel.deleteAllSavedProgrammingLanguagesOfUser()
                userManager.logout()
                withContext(Dispatchers.Main) {
                    val intent = Intent(this@LanguagesActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun setupSpeedDialView() {
        val speedDialView: SpeedDialView = findViewById(R.id.speedDial)

        speedDialView.setOnActionSelectedListener { speedDialActionItem ->
            when (speedDialActionItem.id) {
                R.id.action_slideshow_fragment -> {
                    val direction =
                        HomeViewPagerFragmentDirections.actionViewPagerFragmentToSlideshowFragment()
                    navController.navigate(direction)
                    false // true to keep the Speed Dial open
                }
                R.id.action_dogo -> {
                    Toast.makeText(this, "Dogo clicked", Toast.LENGTH_LONG).show()
                    false // true to keep the Speed Dial open
                }
                else -> {
                    false
                }
            }
        }

        speedDialView.inflate(R.menu.menu_speed_dial)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}