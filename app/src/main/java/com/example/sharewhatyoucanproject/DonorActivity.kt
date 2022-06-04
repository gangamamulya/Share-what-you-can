package com.example.sharewhatyoucanproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sharewhatyoucanproject.R
import android.content.Intent

import com.example.sharewhatyoucanproject.HomescreenActivity
import com.example.sharewhatyoucanproject.MainActivity
import com.example.sharewhatyoucanproject.DonorActivity

import android.widget.EditText
import com.google.firebase.storage.StorageReference
import com.google.firebase.database.DatabaseReference
import android.app.ProgressDialog
import android.Manifest.permission
import android.os.Build

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.database.FirebaseDatabase
import android.widget.Toast
import android.content.pm.PackageManager
import android.annotation.TargetApi
import android.content.DialogInterface
import android.app.Activity
import android.app.AlertDialog
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
import android.net.Uri
import androidx.core.app.ActivityCompat
import android.os.IBinder
import android.view.View
import android.widget.Button
import android.widget.ImageView

import com.google.firebase.storage.UploadTask
import java.io.IOException
import java.util.ArrayList

class DonorActivity : AppCompatActivity() {


    // Folder path for Firebase Storage.
    var Storage_Path = "All_Image_Uploads/"

    // Root Database Name for Firebase Database.
    var Database_Path = "All_Image_Uploads_Database"

    // Creating button.
    var ChooseButton: Button? = null
    var UploadButton: Button? = null


    // Creating EditText.
    var ImageName: EditText? = null
    var ImageDescription: EditText? = null

    // Creating ImageView.
    var SelectImage: ImageView? = null

    // Creating URI.
    var FilePathUri: Uri? = null

    // Creating StorageReference and DatabaseReference object.
    var storageReference: StorageReference? = null
    var databaseReference: DatabaseReference? = null

    // Image request code for onActivityResult() .
    var Image_Request_Code = 7
    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor)


        // Assign FirebaseStorage instance to storageReference.
        storageReference = FirebaseStorage.getInstance().reference

        // Assign FirebaseDatabase instance with root database name.
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path)

        //Assign ID'S to button.
        ChooseButton = findViewById<View>(R.id.btnChoose) as Button
        UploadButton = findViewById<View>(R.id.btnUpload) as Button


        // Assign ID's to EditText.
        ImageName = findViewById<View>(R.id.foodname) as EditText
        ImageDescription = findViewById<View>(R.id.fooddesc) as EditText

        // Assign ID'S to image view.
        SelectImage = findViewById<View>(R.id.imageView) as ImageView

        // Assigning Id to ProgressDialog.
        progressDialog = ProgressDialog(this@DonorActivity)

        // Adding click listener to Choose image button.
        ChooseButton!!.setOnClickListener { // Creating intent.
            val intent = Intent()

            // Setting intent type as image to select image from phone storage.
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Please Select Image"),
                Image_Request_Code
            )
        }


        // Adding click listener to Upload image button.
        UploadButton!!.setOnClickListener { // Calling method to upload selected image on Firebase storage.
            UploadImageFileToFirebaseStorage()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val imageselection =
            (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.data != null)

        if (imageselection) {
            if (data != null) {
                FilePathUri = data.data
            }
            try {

                // Getting selected image into Bitmap.
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, FilePathUri)

                // Setting up bitmap selected image into ImageView.
                SelectImage!!.setImageBitmap(bitmap)

                // After selecting image change choose button above text.
                ChooseButton!!.text = "Image Selected"
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    fun GetFileExtension(uri: Uri?): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    fun UploadImageFileToFirebaseStorage() {

        // TODO: Implement uploading of the image to the backend, <jira ticket: https://oregonstate-innovationlab.atlassian.net/browse/LAB-94
        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            progressDialog!!.setTitle("Image is Uploading...")

            // Showing progressDialog.
            progressDialog!!.show()

            // Creating second StorageReference.
            val storageReference2nd = storageReference!!.child(
                Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri)
            )

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri!!)
                .addOnSuccessListener { taskSnapshot -> // Getting image name and description from EditText and store into string variable.
                    val TempImageName = ImageName!!.text.toString().trim { it <= ' ' }
                    val TempImageDesc = ImageDescription!!.text.toString().trim { it <= ' ' }
                    // float TempLocation = LocationButton.getVa

                    // Hiding the progressDialog after done uploading.
                    progressDialog!!.dismiss()

                    // Showing toast message after done uploading.
                    Toast.makeText(
                        applicationContext,
                        "Image Uploaded Successfully ",
                        Toast.LENGTH_LONG
                    ).show()
                    val imageUploadInfo = ImageUploadInfo(
                        TempImageName,
                        TempImageDesc,
                        taskSnapshot.uploadSessionUri.toString()
                    )

                    // Getting image upload ID.
                    val ImageUploadId = databaseReference!!.push().key

                    // Adding image upload id s child element into databaseReference.
                    databaseReference!!.child(ImageUploadId!!).setValue(imageUploadInfo)
                } // If something goes wrong .
                .addOnFailureListener { exception -> // Hiding the progressDialog.
                    progressDialog!!.dismiss()

                    // Showing exception error message.
                    Toast.makeText(this@DonorActivity, exception.message, Toast.LENGTH_LONG).show()
                } // On progress change upload time.
                .addOnProgressListener(object : OnProgressListener<UploadTask.TaskSnapshot?> {
                    override fun onProgress(snapshot: UploadTask.TaskSnapshot) {

                        // Setting progressDialog Title.
                        progressDialog!!.setTitle("Image is Uploading...")
                    }
                })
        } else {
            Toast.makeText(
                this@DonorActivity,
                "Please Select Image or Add Image Name",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    companion object {
        private const val ALL_PERMISSIONS_RESULT = 101
    }
}