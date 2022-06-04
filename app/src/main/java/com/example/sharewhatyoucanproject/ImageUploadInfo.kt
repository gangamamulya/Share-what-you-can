package com.example.sharewhatyoucanproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sharewhatyoucanproject.R
import android.content.Intent
import com.example.sharewhatyoucanproject.HomescreenActivity
import com.example.sharewhatyoucanproject.MainActivity
import android.widget.EditText
import com.google.firebase.storage.StorageReference
import com.google.firebase.database.DatabaseReference
import android.app.ProgressDialog
import android.Manifest.permission
import android.os.Build
import com.example.sharewhatyoucanproject.DonorActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.database.FirebaseDatabase
import android.widget.Toast
import android.content.pm.PackageManager
import android.annotation.TargetApi
import android.content.DialogInterface
import android.app.Activity
import android.graphics.Bitmap
import android.provider.MediaStore
import android.content.ContentResolver
import android.webkit.MimeTypeMap
import com.google.android.gms.tasks.OnSuccessListener
import com.example.sharewhatyoucanproject.ImageUploadInfo
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.storage.OnProgressListener
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import android.os.IBinder


class ImageUploadInfo {
    var imageName: String? = null
    var imageURL: String? = null
    var imageDescription: String? = null
    constructor() {}
    constructor(name: String?, url: String?, descr: String?) {
        imageName = name
        imageURL = url
        imageDescription = descr
    }
}
