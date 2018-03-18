package com.alisabet.runtimepermission

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import android.widget.Toast

//1.add the permissions to manifest file
//2.add two button to layer, one for check and one for request,
// then find view by id them here and run two click listener for them
//3.checkPermission function to check permissions are granted or not
//4.requestPermission function to request permissions if they're not granted
//5.showMessageOKCancel function to show alert dialog about need to grant both permissions

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {

            R.id.check -> if (checkPermission()) {
                Toast.makeText(this, "Permission already granted.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Please request permission.", Toast.LENGTH_LONG).show()
            }

            R.id.request -> if (!checkPermission()) {
                requestPermission()
            } else {
                Toast.makeText(this, "Permission already granted.", Toast.LENGTH_LONG).show()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val check_button = findViewById<View>(R.id.check) as Button
        val request_button = findViewById<View>(R.id.request) as Button

        check_button.setOnClickListener(this)
        request_button.setOnClickListener(this)

    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
        val result1 = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA), 100)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            100 -> if (grantResults.size > 0) {

                val locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                if (locationAccepted && cameraAccepted)
                    Toast.makeText(this, "Permission Granted, Now you can access location data and camera.", Toast.LENGTH_LONG).show()
                else {
                    Toast.makeText(this, "Permission Denied, You cannot access location data and camera.", Toast.LENGTH_LONG).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA),
                                                    100)
                                        }
                                    })
                            return
                        }
                    }

                }
            }
        }
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this@MainActivity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show()
    }

}
