package com.example.sharewhatyoucanproject

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sharewhatyoucanproject.databinding.ActivityDonordrawerBinding
import com.example.sharewhatyoucanproject.donor.PostsActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

class DonorDrawerActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDonordrawerBinding
    lateinit var circularProgressIndicator: com.google.android.material.progressindicator.CircularProgressIndicator
    lateinit var userNames: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonordrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        circularProgressIndicator = findViewById(R.id.progress_circular)
        setSupportActionBar(binding.appBarDrawer.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_drawer)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val navigationView1 = findViewById<NavigationView>(R.id.nav_view)
        val headerview = navigationView1.getHeaderView(0)
        userNames = headerview.findViewById(R.id.donertv)
        userNames.text = "" + FirebaseAuth.getInstance().currentUser?.displayName
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("myCh", "Channel1", NotificationManager.IMPORTANCE_HIGH)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
        FirebaseMessaging.getInstance()
            .subscribeToTopic("" + FirebaseAuth.getInstance().currentUser?.uid)
        navigationView1.menu.getItem(1).setOnMenuItemClickListener {
            drawerLayout.closeDrawers()
            val i = Intent(this@DonorDrawerActivity, PostsActivity::class.java)
            startActivity(i)
            false
        }
        navigationView1.menu.getItem(2).setOnMenuItemClickListener {
            drawerLayout.closeDrawers()
            val i = Intent(this@DonorDrawerActivity, RequestActivity::class.java)
            i.putExtra("user", "")
            startActivity(i)
            false
        }
        navigationView1.menu.getItem(3).setOnMenuItemClickListener {
            drawerLayout.closeDrawers()
            val i = Intent(this@DonorDrawerActivity, UserActivity::class.java)
            startActivity(i)
            false
        }
        navigationView1.menu.getItem(4).setOnMenuItemClickListener {
            val i = Intent(this@DonorDrawerActivity, DonorDrawerActivity::class.java)
            drawerLayout.closeDrawers()
            startActivity(i)
            false
        }
        navigationView1.menu.getItem(5).setOnMenuItemClickListener {
            drawerLayout.closeDrawers()
            circularProgressIndicator.visibility = View.VISIBLE
            circularProgressIndicator.visibility = View.GONE
            FirebaseAuth.getInstance().signOut()
            val i = Intent(this, HomescreenActivity::class.java)
            startActivity(i)
            finishAffinity()
            false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.drawer, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_drawer)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
