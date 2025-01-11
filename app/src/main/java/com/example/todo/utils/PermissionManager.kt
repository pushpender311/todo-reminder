package com.example.todo.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.todo.R

class PermissionManager(private val context: Context) {

    companion object {
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    }

    fun checkAndRequestNotificationPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            } else {
                onPermissionGranted()
            }
        } else {
            onPermissionGranted()
        }
    }

    // Handle the result of the permission request
    fun onRequestPermissionsResult(
        requestCode: Int, grantResults: IntArray, activity: Activity
    ) {
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                onPermissionGranted()
            } else {
                // Permission denied
                onPermissionDenied(activity)
            }
        }
    }

    private fun onPermissionGranted() {
        Log.d("PermissionManager", "Notification permission granted.")
    }

    private fun onPermissionDenied(activity: Activity) {
        Log.d("PermissionManager", "Notification permission denied.")

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity, Manifest.permission.POST_NOTIFICATIONS
            )
        ) {
            showRationaleDialog(activity)
        }
    }

    private fun showRationaleDialog(activity: Activity) {
        AlertDialog.Builder(activity).setTitle("Permission Required")
            .setMessage(R.string.notification_prompt)
            .setPositiveButton("Grant Permission") { _, _ ->
                // Request permission again
                ActivityCompat.requestPermissions(
                    activity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }.setNegativeButton("Deny") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }
}
