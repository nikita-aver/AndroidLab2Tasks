package com.example.androidlab2_tasks.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.androidlab2_tasks.models.LocationModel
import com.example.androidlab2_tasks.modules.NotificationModule
import com.example.androidlab2_tasks.storage.TasksDB
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.lang.Math.pow


class LocationService : Service() {
    val TAG = "APK: CONSOLE - SERVICE"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val notificationModule = NotificationModule(this)
    private val db = TasksDB(this, null)


    val CHANNEL_ID = "MyForegroundServiceChannel"
    val CHANNEL_NAME = "Channel name"
    val NOTIFICATION_ID = 1

    fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = applicationContext.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    fun startForeground(service: Service) {
        val notification = NotificationCompat.Builder(service, CHANNEL_ID).build()
        service.startForeground(NOTIFICATION_ID, notification)
    }


    override fun stopService(name: Intent?): Boolean {
        Log.d("Stopping","Stopping Service")

        return super.stopService(name)
    }

    override fun onDestroy() {
        Toast.makeText(
            applicationContext, "Service execution completed",
            Toast.LENGTH_SHORT
        ).show()
        Log.d("Stopped","Service Stopped")
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        notificationModule.createNotificationChannel()

        updateLocationTracking()

        return START_REDELIVER_INTENT
    }


    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
        startForeground(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        updateLocationTracking()
    }

    private fun updateLocationTracking() {

        val request = LocationRequest().apply {
            interval = 3000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            result.lastLocation
            onLocationChanged(result.lastLocation!!)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun onLocationChanged(location: Location) {
        notificationModule.createNotificationChannel()
        db.getTodayTasks().forEach { item ->
            val distance = distance(LocationModel(location.latitude, location.longitude), LocationModel(item.latitude, item.longitude))
            if(distance < 0.005 ) {
                notificationModule.sendNotification(item.id, item.description)
            }
        }
    }

    private fun distance(loc1: LocationModel, loc2: LocationModel) : Double {
        return pow(pow(loc1.latitude.toDouble() - loc2.latitude.toDouble(), 2.0)
                + pow(loc1.longitude.toDouble() - loc2.longitude.toDouble(), 2.0), 0.5)
    }

}