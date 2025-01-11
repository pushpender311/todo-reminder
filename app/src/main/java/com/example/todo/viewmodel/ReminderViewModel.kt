package com.example.todo.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.model.Reminder
import com.example.todo.repository.ReminderRepository
import com.example.todo.utils.NotificationHelper
import kotlinx.coroutines.launch

class ReminderViewModel(private val repository: ReminderRepository) : ViewModel() {

    val allTodos: LiveData<List<Reminder>> = repository.allToDos


    fun insertTodo(reminder: Reminder) {
        viewModelScope.launch {
            repository.insert(reminder)
        }
    }

    fun updateTodo(reminder: Reminder) {
        viewModelScope.launch {
            repository.update(reminder)
        }
    }


    fun delete(reminder: Reminder) {
        viewModelScope.launch {
            repository.delete(reminder)
        }
    }

    fun fetchApiTodos(context: Context) {
        viewModelScope.launch {
            val response = repository.fetchApiTodos(context)
            for (todo in response) {
                insertTodo(todo)
                NotificationHelper(context).scheduleNotification(context, todo)
            }
        }
    }

}