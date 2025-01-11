package com.example.todo.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.todo.model.Reminder

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val todo = intent.getParcelableExtra<Reminder>("reminder")
            val notificationHelper = NotificationHelper(context)
            notificationHelper.sendNotification(
                Reminder(
                    title = todo?.title!!,
                    description = todo.description,
                    dateTime = todo.dateTime,
                    recurrence = todo.recurrence,
                    isLocal = todo.isLocal
                )
            )
        }
    }
}