package com.example.androidlab2_tasks

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidlab2_tasks.models.TaskModel
import com.example.androidlab2_tasks.storage.TasksDB
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar

class CreateTaskActivity : AppCompatActivity() {

    private lateinit var calendar : CalendarView
    private lateinit var descriptionArea : EditText
    private lateinit var latitudeArea : EditText
    private lateinit var longitudeArea : EditText
    private lateinit var saveButton : Button
    private lateinit var task: TaskModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_tasks)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.card)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        calendar = findViewById(R.id.calendar_view)
        descriptionArea = findViewById(R.id.description_text)
        latitudeArea = findViewById(R.id.latitude_text)
        longitudeArea = findViewById(R.id.longitude_text)
        saveButton = findViewById(R.id.save_button)

        updateTaskInfo()

        calendar.date = task.date
        descriptionArea.setText(task.description)
        latitudeArea.setText(task.latitude.toString())
        longitudeArea.setText(task.longitude.toString())

        calendar.setOnDateChangeListener{cal, year, mon, day ->
            val converter = Calendar.getInstance()
            converter.set(year, mon, day) // Month is zero-based (0 = January)
            cal.date = converter.timeInMillis
        }

        saveButton.setOnClickListener{
            updateTasks(this)
            finish()
        }
    }

    fun updateTasks(context: Context) = runBlocking {
        val db = TasksDB(context, null)
        val task = TaskModel(task.id, descriptionArea.text.toString(), calendar.date, latitudeArea.text.toString().toDouble(), longitudeArea.text.toString().toDouble())
        if(task.id >= 0) {
            launch{ db.updateTask(task) }
        } else {
            launch { db.addTask(task) }
        }
    }

    fun updateTaskInfo() {
        val id = intent.getIntExtra("task_id", -1)
        task = TaskModel(id, "", System.currentTimeMillis(), 0, 0)
        if(id < 0) {
            return
        }

        val db = TasksDB(this, null)
        task = db.getTask(id)!!
    }
}