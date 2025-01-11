package com.example.todo.ui

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.todo.R
import com.example.todo.database.ReminderDatabase
import com.example.todo.databinding.ActivityReminderDetailsBinding
import com.example.todo.model.Reminder
import com.example.todo.repository.ReminderRepository
import com.example.todo.utils.NotificationHelper
import com.example.todo.viewmodel.ReminderViewModel
import com.example.todo.viewmodel.ReminderViewModelFactory
import java.util.Calendar
import java.util.Locale

class ReminderDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReminderDetailsBinding
    private lateinit var viewModel: ReminderViewModel
    private var reminder: Reminder? = null
    private lateinit var notificationHelper: NotificationHelper
    //Text to speech
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReminderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        val repository = ReminderRepository(ReminderDatabase.getDatabase(application).reminderDao())
        val factory = ReminderViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(ReminderViewModel::class.java)
        notificationHelper = NotificationHelper(this)



        // Get reminder from intent
        reminder = intent.getParcelableExtra("REMINDER")
        if (reminder != null) {
            populateReminderDetails(reminder!!)
        }
        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale.getDefault()
                speakTodo(reminder!!)
            }
        }

        // Set up button listeners
        binding.btnUpdate.setOnClickListener { updateReminder() }
        binding.btnDelete.setOnClickListener { deleteReminder() }
        binding.backButton.setOnClickListener { finish() }
    }

    private fun populateReminderDetails(reminder: Reminder) {
        binding.etTitle.setText(reminder.title)
        binding.etDescription.setText(reminder.description)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = reminder.dateTime
        binding.datePicker.updateDate(
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        )
        binding.timePicker.hour = calendar.get(Calendar.HOUR_OF_DAY)
        binding.timePicker.minute = calendar.get(Calendar.MINUTE)

        val recurrenceOptions = resources.getStringArray(R.array.recurrence_options)
        val index = recurrenceOptions.indexOf(reminder.recurrence)
        if (index >= 0) {
            binding.spinnerRecurrence.setSelection(index)
        }
    }

    private fun updateReminder() {
        val title = binding.etTitle.text.toString()
        val description = binding.etDescription.text.toString()

        val calendar = Calendar.getInstance()
        calendar.set(
            binding.datePicker.year, binding.datePicker.month, binding.datePicker.dayOfMonth, binding.timePicker.hour, binding.timePicker.minute
        )
        val updatedTime = calendar.timeInMillis

        val recurrence = binding.spinnerRecurrence.selectedItem.toString()

        if (title.isBlank() || description.isBlank()) {
            Toast.makeText(this, "Title and description cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        reminder?.let {
            it.title = title
            it.description = description
            it.dateTime = updatedTime
            it.recurrence = recurrence

            viewModel.updateTodo(it)
            notificationHelper.scheduleNotification(this, it)
            Toast.makeText(this, "Reminder updated", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun deleteReminder() {
        reminder?.let {
            viewModel.delete(it)
            notificationHelper.cancelNotification(this, it)
            Toast.makeText(this, "Reminder deleted", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun speakTodo(reminder: Reminder) {
        val text = "Title: ${reminder.title}, Description: ${reminder.description}"
        Log.d("MainActivity", "speakTodo: $text")
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onPause() {
        super.onPause()
        textToSpeech.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.shutdown()
    }
}
