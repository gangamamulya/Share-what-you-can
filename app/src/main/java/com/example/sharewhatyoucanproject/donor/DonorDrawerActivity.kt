package com.example.sharewhatyoucanproject

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sharewhatyoucanproject.databinding.ActivityDonordrawerBinding
import com.example.sharewhatyoucanproject.databinding.NavHeaderDrawerBinding
import com.example.sharewhatyoucanproject.donor.DonorDrawerViewModel
import com.example.sharewhatyoucanproject.donor.PostsActivity
import com.example.sharewhatyoucanproject.utils.createNotificationChannel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.auth.FirebaseAuth

class DonorDrawerActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDonordrawerBinding
    private lateinit var drawerLayout: DrawerLayout
    lateinit var circularProgressIndicator: CircularProgressIndicator
    private lateinit var donorDrawerViewModel: DonorDrawerViewModel

    companion object {
        fun navigate(activity: AppCompatActivity) {
            val intent = Intent(activity, DonorDrawerActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityDonordrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        donorDrawerViewModel = ViewModelProvider(this)[DonorDrawerViewModel::class.java]
        //supportActionBar?.hide()
        circularProgressIndicator = binding.progressCircular
        //setSupportActionBar(binding.appBarDrawer.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_content_drawer)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)
        val headerView = navView.getHeaderView(0)
        val headerBinding: NavHeaderDrawerBinding = NavHeaderDrawerBinding.bind(headerView)
        headerBinding.donertv.text = donorDrawerViewModel.getUserName()

        createNotificationChannel(this, donorDrawerViewModel.getUserId())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.drawer, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_drawer)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

     object DonorConstants{
        const val CHANNEL_1_ID = "myCh"
        const val CHANNEL_2_ID = "Channel1"
         const val  empty_string=""
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.posts_item -> {
                PostsActivity.navigate(this)
            }
            R.id.request_item -> {
                RequestActivity.navigate(this, DonorConstants.empty_string)
            }
            R.id.user_item -> {
                UserActivity.navigate(this)
            }
            R.id.review_item -> {
                DonorDrawerActivity.navigate(this)
            }
            R.id.logout_item -> {
                circularProgressIndicator.visibility = View.VISIBLE
                circularProgressIndicator.visibility = View.GONE
                FirebaseAuth.getInstance().signOut()
                HomescreenActivity.navigate(this)
                finishAffinity()
            }
            else -> {}
        }
       //drawerLayout.closeDrawers()
        return false
    }

    }

