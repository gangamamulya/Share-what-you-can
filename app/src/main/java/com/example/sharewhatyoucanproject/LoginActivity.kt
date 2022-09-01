package com.example.sharewhatyoucanproject

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    lateinit var mytv: TextView
    lateinit var submitdevice: Button
    lateinit var email: String
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    var type = 0
    lateinit var deviceid: String
    lateinit var pd: ProgressDialog
    lateinit var sharedPreferences: SharedPreferences
    lateinit var myEdit: SharedPreferences.Editor
    lateinit var namearray: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        myEdit = sharedPreferences.edit()
        type = intent.getIntExtra("type", 0)
        pd = ProgressDialog(this)
        pd.setTitle("Please Wait")
        mytv = findViewById(R.id.mytv)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        if (type == 1) {
            mytv.text = "Doner"
        } else if (type == 2) {
            mytv.text = "Receiver"
        }
        namearray = ArrayList()
        namearray.add("Tom")
        namearray.add("Harry")
        namearray.add("Tony")
        namearray.add("Noah")
        namearray.add("Emma")
        namearray.add("Oliver")
        namearray.add("James")
        namearray.add("William")
        namearray.add("Henry")
        namearray.add("Benjamin")
        submitdevice = findViewById(R.id.submitdevice)
        submitdevice.setOnClickListener(View.OnClickListener {
            deviceid = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
            email = "s" + deviceid + "@gmail.com"
            pd.show()
            checkuser()
        })
    }

    fun getname(): String {
        var name = ""
        val myindex = (0 until 10).random()
        name = namearray.get(myindex)
        return name
    }

    fun checkuser() {
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val size = task.result.size()

                    if (size == 0) {
                        createuser()
                    } else {
                        loginuser()
                    }
                } else {
                    pd.dismiss()
                    Toast.makeText(
                        applicationContext,
                        "Failed " + task.exception,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun createuser() {

        auth.createUserWithEmailAndPassword(
            email,
            "Test@123"
        ).addOnCompleteListener(OnCompleteListener {
            if (it.isSuccessful) {
                pd.dismiss()
                Toast.makeText(applicationContext, "Created", Toast.LENGTH_SHORT).show()
                val usermap: MutableMap<String, String> = HashMap()
                val random = (1111 until 9999).random()
                val myname = getname() + random
                usermap["name"] = myname
                usermap["email"] = email
                val user1 = it.result.user
                if (user1 != null) {
                    usermap["uuid"] = it.result.user!!.uid
                }
                usermap["deviceId"] = deviceid

                it.result?.user?.uid?.let { it1 ->
                    db.collection("users")
                        .document(it1)
                        .set(usermap)
                        .addOnCompleteListener { task2 ->
                            if (task2.isSuccessful) {

                                val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                                val profileUpdates: UserProfileChangeRequest =
                                    UserProfileChangeRequest.Builder()
                                        .setDisplayName("" + myname)
                                        .build()
                                user?.updateProfile(profileUpdates)
                                    ?.addOnCompleteListener(OnCompleteListener { task3 ->
                                        if (task3.isSuccessful) {
                                            pd.dismiss()
                                            if (type == 1) {
                                                myEdit.putInt("selector", 1)
                                                myEdit.commit()
                                                val i = Intent(
                                                    this@LoginActivity,
                                                    SelectUserActivity::class.java
                                                )
                                                startActivity(i)
                                                finishAffinity()
                                            } else if (type == 2) {
                                                myEdit.putInt("selector", 2)
                                                myEdit.commit()
                                                val i = Intent(
                                                    this,
                                                    DrawerActivity2::class.java
                                                )
                                                startActivity(i)
                                                finishAffinity()
                                            }
                                        } else {
                                            pd.dismiss()
                                            Toast.makeText(
                                                applicationContext,
                                                "Failed " + task3.exception,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })
                            } else {
                                pd.dismiss()
                                Toast.makeText(
                                    applicationContext,
                                    "Failed " + task2.exception,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                }
            } else {
                pd.dismiss()
                Toast.makeText(applicationContext, "Failed " + it.exception, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    fun loginuser() {
        auth.signInWithEmailAndPassword(
            email, "Test@123"
        ).addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                pd.dismiss()
                if (type == 1) {
                    myEdit.putInt("selector", 1)
                    myEdit.commit()
                    val i = Intent(this@LoginActivity, DrawerActivity::class.java)
                    startActivity(i)
                    finishAffinity()
                } else if (type == 2) {
                    myEdit.putInt("selector", 2)
                    myEdit.commit()
                    val i = Intent(this@LoginActivity, DrawerActivity2::class.java)
                    startActivity(i)
                    finishAffinity()
                }
            } else {
                pd.dismiss()
                Toast.makeText(applicationContext, "Failed " + task.exception, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}