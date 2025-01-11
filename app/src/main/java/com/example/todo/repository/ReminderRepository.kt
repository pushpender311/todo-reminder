package com.example.todo.repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.example.todo.database.ReminderDao
import com.example.todo.model.Reminder
import com.example.todo.network.RetrofitInstance
import com.example.todo.utils.NetworkHelper
import retrofit2.HttpException
import kotlin.random.Random

class ReminderRepository(private val reminderDao: ReminderDao) {

    val allToDos: LiveData<List<Reminder>> = reminderDao.getAllTodos()

    suspend fun insert(reminder: Reminder) {
        reminderDao.insertReminder(reminder)
    }

    suspend fun update(reminder: Reminder) {
        reminderDao.updateReminder(reminder)
    }

    suspend fun delete(reminder: Reminder) {
        reminderDao.deleteReminder(reminder)
    }

    suspend fun fetchApiTodos(context: Context): List<Reminder> {

        if (!NetworkHelper.isNetworkAvailable(context)) {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
            return emptyList()
        }
        val apiReminder = RetrofitInstance.api.getTodos()

        return try {
            apiReminder.map {
                Reminder(
                    it.id,
                    it.title,
                    description = "",
                    dateTime = System.currentTimeMillis() + Random.nextLong(3600000),
                    recurrence = "None",
                    isLocal = false,
                )
            }
        } catch (e: HttpException) {
            Toast.makeText(context, e.message(), Toast.LENGTH_SHORT).show()
            emptyList()
        }

    }
}