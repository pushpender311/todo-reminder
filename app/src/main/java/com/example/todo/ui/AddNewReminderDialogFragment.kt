package com.example.todo.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.todo.R
import com.example.todo.database.ReminderDatabase
import com.example.todo.databinding.FragmentAddNewTodoBinding
import com.example.todo.model.Reminder
import com.example.todo.repository.ReminderRepository
import com.example.todo.utils.NotificationHelper
import com.example.todo.viewmodel.ReminderViewModel
import com.example.todo.viewmodel.ReminderViewModelFactory
import java.util.Calendar

class AddNewReminderDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentAddNewTodoBinding
    private lateinit var viewModel: ReminderViewModel

    private lateinit var notificationHelper: NotificationHelper

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentAddNewTodoBinding.inflate(layoutInflater)

        val repository = ReminderRepository(ReminderDatabase.getDatabase(requireContext()).reminderDao())
        val factory = ReminderViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ReminderViewModel::class.java]
        notificationHelper = NotificationHelper(requireContext())

        setupRecurrenceSpinner()

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
            .setTitle("Create Reminder")
            .setPositiveButton("Save") { _, _ ->
                saveReminder()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        return builder.create()
    }

    private fun setupRecurrenceSpinner() {
        val recurrenceOptions = resources.getStringArray(R.array.recurrence_options)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, recurrenceOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRecurrence.adapter = adapter
    }

    private fun saveReminder() {
        val title = binding.etTitle.text.toString()
        val description = binding.etDescription.text.toString()

        val calendar = Calendar.getInstance()
        calendar.set(
            binding.datePicker.year,
            binding.datePicker.month,
            binding.datePicker.dayOfMonth,
            binding.timePicker.hour,
            binding.timePicker.minute
        )
        val reminderTime = calendar.timeInMillis

        val recurrence = binding.spinnerRecurrence.selectedItem.toString()


        if (title.isBlank() || description.isBlank()) {
            Toast.makeText(requireContext(), "Title and description cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }


        val reminder = Reminder(
            title = title,
            description = description,
            dateTime = reminderTime,
            isLocal = true,
            recurrence = recurrence
        )

        viewModel.insertTodo(reminder)

        notificationHelper.scheduleNotification(requireActivity(), reminder)
        dismiss()
    }

}