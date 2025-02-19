package com.example.androidlab2_tasks

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidlab2_tasks.adapters.TaskAdapter
import com.example.androidlab2_tasks.services.LocationService
import com.example.androidlab2_tasks.storage.TasksDB

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("InvalidPeriodicWorkRequestInterval")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        requestPermissionNotification()
        requestPermissionLocation()

        val addButton = findViewById<Button>(R.id.add_button)

        addButton.setOnClickListener {
            val intent = Intent(this, CreateTaskActivity::class.java)
            startActivity(intent)
        }

        val serviceIntent = Intent(this, LocationService::class.java)
        startForegroundService(serviceIntent)
    }

    override fun onResume() {
        super.onResume()

        val tasksList = findViewById<RecyclerView>(R.id.contacts_list)

        val db = TasksDB(this, null)
        val tasks = db.getTasks()
        tasksList.layoutManager = LinearLayoutManager(this)
        tasksList.adapter = TaskAdapter(tasks, this)
    }

    private fun requestPermissionNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
            }
        }
    }


    private fun requestPermissionLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    102
                )
            }
        }
    }
}