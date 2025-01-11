package com.example.todo

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.todo.adapter.ReminderAdapter
import com.example.todo.database.ReminderDatabase
import com.example.todo.databinding.ActivityMainBinding
import com.example.todo.model.Reminder
import com.example.todo.repository.ReminderRepository
import com.example.todo.ui.AddNewReminderDialogFragment
import com.example.todo.ui.ReminderDetailsActivity
import com.example.todo.utils.PermissionManager
import com.example.todo.viewmodel.ReminderViewModel
import com.example.todo.viewmodel.ReminderViewModelFactory
import java.util.Locale

class MainActivity : AppCompatActivity(), ReminderAdapter.TodoClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ReminderAdapter
    private var todos: List<Reminder>? = null
    private lateinit var viewModel: ReminderViewModel
    private lateinit var permissionManager: PermissionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionManager = PermissionManager(this)
        permissionManager.checkAndRequestNotificationPermission(this)


        //repository
        val repository = ReminderRepository(ReminderDatabase.getDatabase(this).reminderDao())

        viewModel = ViewModelProvider(this, ReminderViewModelFactory(repository)).get(ReminderViewModel::class.java)
        adapter = ReminderAdapter(this, todos ?: emptyList(), this)
        binding.recyclerView.adapter = adapter


        viewModel.fetchApiTodos(this)

        viewModel.allTodos.observe(this) {
            todos = it
            adapter = ReminderAdapter(this, todos ?: emptyList(), this)
            binding.recyclerView.adapter = adapter
        }

        binding.fab.setOnClickListener {
            val dialog = AddNewReminderDialogFragment()
            dialog.show(supportFragmentManager, "AddNewToDoDialogFragment")
        }

    }




    // Handle the result of permission requests
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        permissionManager.onRequestPermissionsResult(requestCode, grantResults, this)
    }


    override fun onTodoClick(reminder: Reminder) {
        val intent = Intent(this, ReminderDetailsActivity::class.java)
        intent.putExtra("REMINDER", reminder)
        startActivity(intent)
    }
}