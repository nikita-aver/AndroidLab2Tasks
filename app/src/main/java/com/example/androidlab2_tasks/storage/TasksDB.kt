package com.example.androidlab2_tasks.storage

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.location.Location
import com.example.androidlab2_tasks.models.LocationModel
import com.example.androidlab2_tasks.models.TaskModel
import java.sql.Date

class TasksDB(val context: Context, val factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, "contacts.app", factory, 1)  {

    override fun onCreate(db: SQLiteDatabase?) {
        val  query = "create table tasks (id integer primary key autoincrement, description text, date date, latitude real, longitude real)"
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("drop table if exists tasks")
        onCreate(db)
    }

    suspend fun addTask(contact: TaskModel){
        val values = ContentValues()
        values.put("description", contact.description)
        values.put("date", contact.date.toString())
        values.put("latitude", contact.latitude.toString())
        values.put("longitude", contact.longitude.toString())

        val db = this.writableDatabase
        db.insert("tasks", null, values)
    }

    suspend fun updateTask(task: TaskModel){
        val values = ContentValues()
        values.put("description", task.description)
        values.put("date", task.date.toString())
        values.put("latitude", task.latitude.toString())
        values.put("longitude", task.longitude.toString())

        val db = this.writableDatabase
        db.update("tasks", values,"id = ?", arrayOf(task.id.toString()))
    }

    fun getTask(id: Int): TaskModel? {
        val db = this.readableDatabase

        val result = db.rawQuery("select * from tasks where id = '$id'", null).use {
            if(it.moveToFirst()) {
                val task = TaskModel(
                    it.getInt(it.getColumnIndexOrThrow("id")),
                    it.getString(it.getColumnIndexOrThrow("description")),
                    it.getLong(it.getColumnIndexOrThrow("date")),
                    it.getDouble(it.getColumnIndexOrThrow("latitude")),
                    it.getDouble(it.getColumnIndexOrThrow("longitude")))

                return task;
            }
        }

        return null;
    }

    fun deleteTask(id: Int) {
        val db = this.writableDatabase
        db.delete("tasks", "id = ?", arrayOf(id.toString()))
    }

    fun getTasks(): List<TaskModel> {
        val tasks = mutableListOf<TaskModel>()
        val db = readableDatabase
        val it = db.rawQuery("select * from tasks", null)

        if (it.count > 0 && it.moveToFirst()) {
            do {
                tasks.add(TaskModel(
                    it.getInt(it.getColumnIndexOrThrow("id")),
                    it.getString(it.getColumnIndexOrThrow("description")),
                    it.getLong(it.getColumnIndexOrThrow("date")),
                    it.getDouble(it.getColumnIndexOrThrow("latitude")),
                    it.getDouble(it.getColumnIndexOrThrow("longitude"))))
            } while (it.moveToNext())
        }

        it.close()
        return tasks
    }


    fun getTodayTasks(location: Location): List<TaskModel> {
        val tasks = mutableListOf<TaskModel>()
        val db = readableDatabase
        val latitude = location.latitude
        val longitude = location.longitude
        val it = db.rawQuery("select * " +
                                 "  from tasks " +
                                 " where date >= strftime('%s', 'now', 'start of day') * 1000" +
                                 "   and date < strftime('%s', 'now', 'start of day', '+1 day') * 1000", null)

        if (it.count > 0 && it.moveToFirst()) {
            do {
                tasks.add(TaskModel(
                    it.getInt(it.getColumnIndexOrThrow("id")),
                    it.getString(it.getColumnIndexOrThrow("description")),
                    it.getLong(it.getColumnIndexOrThrow("date")),
                    it.getDouble(it.getColumnIndexOrThrow("latitude")),
                    it.getDouble(it.getColumnIndexOrThrow("longitude"))))
            } while (it.moveToNext())
        }

        it.close()
        return tasks
    }
}