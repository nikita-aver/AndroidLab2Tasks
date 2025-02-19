package com.example.androidlab2_tasks.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidlab2_tasks.CreateTaskActivity
import com.example.androidlab2_tasks.R
import com.example.androidlab2_tasks.models.TaskModel
import com.example.androidlab2_tasks.storage.TasksDB
import java.util.Date

class TaskAdapter(var items: List<TaskModel>, var context: Context): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){
    class TaskViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val dateArea = view.findViewById<TextView>(R.id.date_area)
        val descriptionArea = view.findViewById<TextView>(R.id.description_area)
        val deleteButton = view.findViewById<Button>(R.id.delete_button)
        val selectButton = view.findViewById<LinearLayout>(R.id.card_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  items.count()
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.descriptionArea.text = items[position].description
        holder.dateArea.setText(Date(items[position].date).toString())
        holder.deleteButton.setOnClickListener {
            val db = TasksDB(context, null)
            db.deleteTask(items[position].id)
            items = db.getTasks()
            notifyDataSetChanged()
        }

        holder.selectButton.setOnClickListener {
            val intent = Intent(context, CreateTaskActivity::class.java).apply {
                putExtra("task_id", items[position].id)
            }
            context.startActivity(intent)
        }
    }
}