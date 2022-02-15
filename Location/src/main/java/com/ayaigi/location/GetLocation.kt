package com.ayaigi.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

class GetLocation(context: Context, private val locationListener: LocationListener) {

    companion object {
        interface LocationListener {
            fun onComplete(location: Location?)
        }
    }

    private var PERMISSION_ID = 52
    private var fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    var task: Task<Location>? = null

    init {
        if (!CheckPermission(context)) {
            RequestPermission(context)
        }
    }

    fun get(context: Context) {
        Log.i("ApD", "LocateGet")
        if (task == null) {
            Log.i("ApD", "LocateGetNN")
            task = getLastLocation(context)
            task!!.addOnCompleteListener { task ->
                locationListener.onComplete(task.result)
                this.task = null
            }
        } else {
            Toast.makeText(context, "Already initialized", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(context: Context): Task<Location>? {
        val t = if (CheckPermission(context)) {
            if (isLocationEnabled(context)) {
                fusedLocationProviderClient.lastLocation
            } else {
                Toast.makeText(context, "Please Enable GPS", Toast.LENGTH_SHORT).show()
                return null
            }
        } else {
            RequestPermission(context)
            return null
        }
        return t
    }

    //check Permission
    private fun CheckPermission(context: Context): Boolean {
        if (
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun RequestPermission(context: Context) {
        //Toast.makeText(context, "request", Toast.LENGTH_SHORT).show()
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSION_ID
        )
        /*
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            PERMISSION_ID
        )
        */

    }

    private fun isLocationEnabled(context: Context): Boolean {

        var locationManager: LocationManager =
            (context as Activity).getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}
