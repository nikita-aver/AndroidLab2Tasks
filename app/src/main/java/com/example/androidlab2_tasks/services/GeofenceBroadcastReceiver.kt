package com.example.androidlab2_tasks.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast


class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val startIntent = context!!
            .packageManager
            .getLaunchIntentForPackage(context.packageName)

        startIntent!!.setFlags(
            Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or
                    Intent.FLAG_ACTIVITY_NEW_TASK or
            Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        )
        context.startActivity(startIntent)
        Toast.makeText(context, intent.toString(), Toast.LENGTH_LONG).show()
    }
}
